package com.example.util

import android.content.Context
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

data class AdminAuthResult(
    val success: Boolean,
    val message: String,
    val normalizedEmail: String = ""
)

object AdminAuthManager {
    private const val PREFS_NAME = "satlok_admin_secure_prefs"
    private const val KEY_EMAIL = "admin_email"
    private const val KEY_SALT = "admin_salt"
    private const val KEY_HASH = "admin_hash"
    private const val KEY_FAILED_ATTEMPTS = "failed_attempts"
    private const val KEY_LOCKOUT_UNTIL = "lockout_until_ms"

    private const val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val ITERATIONS = 100_000
    private const val KEY_LENGTH_BITS = 256
    private const val MAX_FAILED_ATTEMPTS = 5
    private const val LOCKOUT_DURATION_MS = 30_000L

    fun isAdminConfigured(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val email = prefs.getString(KEY_EMAIL, null)
        val salt = prefs.getString(KEY_SALT, null)
        val hash = prefs.getString(KEY_HASH, null)
        return !email.isNullOrBlank() && !salt.isNullOrBlank() && !hash.isNullOrBlank()
    }

    fun getConfiguredEmail(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_EMAIL, "") ?: ""
    }

    fun setupInitialAdmin(
        context: Context,
        email: String,
        password: String,
        confirmPassword: String
    ): AdminAuthResult {
        if (isAdminConfigured(context)) {
            return AdminAuthResult(
                success = false,
                message = "एडमिन खाता पहले से पंजीकृत है। कृपया लॉगिन करें।"
            )
        }
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.length < 4 || !cleanEmail.contains("@") || !cleanEmail.contains(".")) {
            return AdminAuthResult(
                success = false,
                message = "कृपया एक मान्य एडमिन ईमेल पता दर्ज करें।"
            )
        }
        if (password.length < 6) {
            return AdminAuthResult(
                success = false,
                message = "सुरक्षा के लिए पासवर्ड कम से कम 6 अक्षरों का होना चाहिए।"
            )
        }
        if (password != confirmPassword) {
            return AdminAuthResult(
                success = false,
                message = "दोनों पासवर्ड मेल नहीं खा रहे हैं।"
            )
        }

        saveHashedCredential(context, cleanEmail, password)
        return AdminAuthResult(
            success = true,
            message = "सुरक्षित एडमिन खाता सफलतापूर्वक सेट हो गया!",
            normalizedEmail = cleanEmail
        )
    }

    fun verifyAndLogin(
        context: Context,
        email: String,
        password: String,
        nowMs: Long = System.currentTimeMillis()
    ): AdminAuthResult {
        if (!isAdminConfigured(context)) {
            return AdminAuthResult(
                success = false,
                message = "पहले एडमिन खाता सेटअप करें।"
            )
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val lockoutUntil = prefs.getLong(KEY_LOCKOUT_UNTIL, 0L)
        if (nowMs < lockoutUntil) {
            val remainingSec = ((lockoutUntil - nowMs) / 1000L).coerceAtLeast(1L)
            return AdminAuthResult(
                success = false,
                message = "बहुत अधिक गलत प्रयास! कृपया $remainingSec सेकंड बाद पुनः प्रयास करें।"
            )
        }

        val cleanEmail = email.trim().lowercase()
        val storedEmail = prefs.getString(KEY_EMAIL, "").orEmpty().lowercase()
        val saltB64 = prefs.getString(KEY_SALT, "").orEmpty()
        val hashB64 = prefs.getString(KEY_HASH, "").orEmpty()

        val emailMatches = cleanEmail.isNotEmpty() && cleanEmail == storedEmail
        val passwordMatches = verifyPassword(password, saltB64, hashB64)

        return if (emailMatches && passwordMatches) {
            prefs.edit()
                .putInt(KEY_FAILED_ATTEMPTS, 0)
                .putLong(KEY_LOCKOUT_UNTIL, 0L)
                .apply()
            AdminAuthResult(
                success = true,
                message = "एडमिन लॉगिन सफल!",
                normalizedEmail = storedEmail
            )
        } else {
            val failures = prefs.getInt(KEY_FAILED_ATTEMPTS, 0) + 1
            val editor = prefs.edit().putInt(KEY_FAILED_ATTEMPTS, failures)
            if (failures >= MAX_FAILED_ATTEMPTS) {
                editor.putLong(KEY_LOCKOUT_UNTIL, nowMs + LOCKOUT_DURATION_MS)
                editor.putInt(KEY_FAILED_ATTEMPTS, 0)
            }
            editor.apply()
            AdminAuthResult(
                success = false,
                message = "अमान्य ईमेल या पासवर्ड! केवल अधिकृत एडमिन ही लॉगिन कर सकते हैं।"
            )
        }
    }

    fun changePassword(
        context: Context,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): AdminAuthResult {
        if (!isAdminConfigured(context)) {
            return AdminAuthResult(success = false, message = "कोई एडमिन खाता पंजीकृत नहीं है।")
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val storedEmail = prefs.getString(KEY_EMAIL, "").orEmpty()
        val saltB64 = prefs.getString(KEY_SALT, "").orEmpty()
        val hashB64 = prefs.getString(KEY_HASH, "").orEmpty()

        if (!verifyPassword(currentPassword, saltB64, hashB64)) {
            return AdminAuthResult(success = false, message = "वर्तमान पासवर्ड गलत है।")
        }
        if (newPassword.length < 6) {
            return AdminAuthResult(success = false, message = "नया पासवर्ड कम से कम 6 अक्षरों का होना चाहिए।")
        }
        if (newPassword != confirmNewPassword) {
            return AdminAuthResult(success = false, message = "नए पासवर्ड मेल नहीं खा रहे हैं।")
        }

        saveHashedCredential(context, storedEmail, newPassword)
        return AdminAuthResult(
            success = true,
            message = "एडमिन पासवर्ड सफलतापूर्वक बदल दिया गया!",
            normalizedEmail = storedEmail
        )
    }

    internal fun clearForTesting(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    private fun saveHashedCredential(context: Context, email: String, password: String) {
        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        val hash = pbkdf2(password, salt)
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_EMAIL, email)
            .putString(KEY_SALT, Base64.encodeToString(salt, Base64.NO_WRAP))
            .putString(KEY_HASH, Base64.encodeToString(hash, Base64.NO_WRAP))
            .putInt(KEY_FAILED_ATTEMPTS, 0)
            .putLong(KEY_LOCKOUT_UNTIL, 0L)
            .apply()
    }

    private fun verifyPassword(password: String, saltB64: String, expectedHashB64: String): Boolean {
        if (password.isEmpty() || saltB64.isBlank() || expectedHashB64.isBlank()) return false
        return try {
            val salt = Base64.decode(saltB64, Base64.NO_WRAP)
            val expectedHash = Base64.decode(expectedHashB64, Base64.NO_WRAP)
            val actualHash = pbkdf2(password, salt)
            MessageDigest.isEqual(actualHash, expectedHash)
        } catch (_: Exception) {
            false
        }
    }

    private fun pbkdf2(password: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS)
        return try {
            SecretKeyFactory.getInstance(PBKDF2_ALGORITHM).generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }
}
