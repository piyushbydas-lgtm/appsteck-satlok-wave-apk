package com.example.util

import android.content.Context
import android.net.Uri
import com.example.data.model.CloudUploadConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okio.BufferedSink
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.TimeUnit

sealed class MediaUploadResult {
    data class Success(
        val mediaUrl: String,
        val thumbnailUrl: String,
        val mimeType: String,
        val sizeBytes: Long,
        val storageType: String, // "CLOUD" or "LOCAL"
        val detectedDuration: String,
        val statusMessage: String
    ) : MediaUploadResult()

    data class Failure(val errorMessage: String) : MediaUploadResult()
}

enum class UploadKind(val subfolder: String, val prefix: String, val defaultExt: String, val cloudinaryResourceType: String) {
    VIDEO("uploaded_videos", "vid", "mp4", "video"),
    IMAGE("uploaded_photos", "img", "jpg", "image"),
    THUMBNAIL("uploaded_thumbs", "thumb", "jpg", "image"),
    AUDIO("uploaded_audios", "aud", "mp3", "video"),
    DOCUMENT("uploaded_docs", "doc", "pdf", "raw")
}

object MediaUploadManager {
    private const val PREFS_CLOUD = "satlok_cloud_upload_prefs"
    private const val KEY_CLOUD_NAME = "cloudinary_cloud_name"
    private const val KEY_UPLOAD_PRESET = "cloudinary_upload_preset"

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    fun getCloudConfig(context: Context): CloudUploadConfig {
        val prefs = context.getSharedPreferences(PREFS_CLOUD, Context.MODE_PRIVATE)
        return CloudUploadConfig(
            cloudName = prefs.getString(KEY_CLOUD_NAME, "").orEmpty(),
            uploadPreset = prefs.getString(KEY_UPLOAD_PRESET, "").orEmpty()
        )
    }

    fun saveCloudConfig(context: Context, cloudName: String, uploadPreset: String) {
        context.getSharedPreferences(PREFS_CLOUD, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_CLOUD_NAME, cloudName.trim())
            .putString(KEY_UPLOAD_PRESET, uploadPreset.trim())
            .apply()
    }

    suspend fun uploadPickedFile(
        context: Context,
        uri: Uri,
        kind: UploadKind,
        onProgress: (Float, String) -> Unit
    ): MediaUploadResult = withContext(Dispatchers.IO) {
        // Step 1: Copy and verify file locally so playback & persistence never break on temporary SAF URIs
        val localResult = FileStorageHelper.copyMediaWithProgress(
            context = context,
            uri = uri,
            subfolder = kind.subfolder,
            prefix = kind.prefix,
            defaultExtension = kind.defaultExt,
            extractVideoMetadata = (kind == UploadKind.VIDEO),
            onProgress = { pct, msg ->
                val scaled = if (getCloudConfig(context).isConfigured) pct * 0.35f else pct
                onProgress(scaled, msg)
            }
        )

        val localMedia = localResult.getOrElse { err ->
            return@withContext MediaUploadResult.Failure(
                errorMessage = err.localizedMessage ?: "फ़ाइल डिवाइस में सहेजने में त्रुटि हुई।"
            )
        }

        // Step 2: If Cloudinary unsigned preset is configured by Admin, upload over HTTPS with real byte progress
        val cloudConfig = getCloudConfig(context)
        if (!cloudConfig.isConfigured) {
            return@withContext MediaUploadResult.Success(
                mediaUrl = localMedia.absolutePath,
                thumbnailUrl = localMedia.generatedThumbnailPath,
                mimeType = localMedia.mimeType,
                sizeBytes = localMedia.sizeBytes,
                storageType = "LOCAL",
                detectedDuration = localMedia.detectedDuration,
                statusMessage = "फ़ाइल ऐप डेटाबेस (Room + स्थानीय स्टोरेज) में स्थायी रूप से सेव हो गई (${FileStorageHelper.formatSize(localMedia.sizeBytes)})।"
            )
        }

        return@withContext uploadFileToCloudinary(
            file = File(localMedia.absolutePath),
            mimeType = localMedia.mimeType,
            kind = kind,
            cloudConfig = cloudConfig,
            fallbackThumb = localMedia.generatedThumbnailPath,
            detectedDuration = localMedia.detectedDuration,
            onProgress = onProgress
        )
    }

    private fun uploadFileToCloudinary(
        file: File,
        mimeType: String,
        kind: UploadKind,
        cloudConfig: CloudUploadConfig,
        fallbackThumb: String,
        detectedDuration: String,
        onProgress: (Float, String) -> Unit
    ): MediaUploadResult {
        return try {
            val mediaType = mimeType.toMediaTypeOrNull() ?: "application/octet-stream".toMediaTypeOrNull()
            val progressBody = CountingFileRequestBody(
                file = file,
                contentType = mediaType
            ) { written, total ->
                val ratio = if (total > 0L) (written.toFloat() / total.toFloat()).coerceIn(0f, 1f) else 0.5f
                val overall = 0.35f + ratio * 0.65f
                val pct = (overall * 100).toInt()
                onProgress(overall, "क्लाउड स्टोरेज में अपलोड हो रहा है ($pct%)...")
            }

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("upload_preset", cloudConfig.uploadPreset)
                .addFormDataPart("folder", "satlok_wave/${kind.subfolder}")
                .addFormDataPart("file", file.name, progressBody)
                .build()

            val endpoint = "https://api.cloudinary.com/v1_1/${cloudConfig.cloudName}/${kind.cloudinaryResourceType}/upload"
            val request = Request.Builder()
                .url(endpoint)
                .post(requestBody)
                .build()

            httpClient.newCall(request).execute().use { response ->
                val rawBody = response.body?.string().orEmpty()
                if (!response.isSuccessful) {
                    val serverMsg = runCatching {
                        JSONObject(rawBody).optJSONObject("error")?.optString("message")
                    }.getOrNull().orEmpty()
                    return MediaUploadResult.Failure(
                        errorMessage = "क्लाउड अपलोड विफल (HTTP ${response.code}): ${serverMsg.ifBlank { "नेटवर्क या Cloudinary Preset जांचें" }}"
                    )
                }

                val json = JSONObject(rawBody)
                val secureUrl = json.optString("secure_url")
                if (secureUrl.isBlank()) {
                    return MediaUploadResult.Failure("क्लाउड सर्वर से स्थायी URL प्राप्त नहीं हुआ।")
                }

                val cloudThumb = if (kind == UploadKind.VIDEO) {
                    secureUrl.substringBeforeLast(".") + ".jpg"
                } else if (kind == UploadKind.IMAGE || kind == UploadKind.THUMBNAIL) {
                    secureUrl
                } else {
                    fallbackThumb
                }

                onProgress(1f, "क्लाउड अपलोड पूर्ण!")
                MediaUploadResult.Success(
                    mediaUrl = secureUrl,
                    thumbnailUrl = cloudThumb.ifBlank { fallbackThumb },
                    mimeType = mimeType,
                    sizeBytes = file.length(),
                    storageType = "CLOUD",
                    detectedDuration = detectedDuration,
                    statusMessage = "फ़ाइल सफलतापूर्वक स्थायी क्लाउड स्टोरेज में अपलोड हो गई!"
                )
            }
        } catch (e: Exception) {
            MediaUploadResult.Failure(
                errorMessage = "क्लाउड अपलोड त्रुटि: ${e.localizedMessage ?: "इंटरनेट कनेक्शन जांचें"}"
            )
        }
    }

    private class CountingFileRequestBody(
        private val file: File,
        private val contentType: MediaType?,
        private val onProgress: (Long, Long) -> Unit
    ) : RequestBody() {
        override fun contentType(): MediaType? = contentType
        override fun contentLength(): Long = file.length()

        override fun writeTo(sink: BufferedSink) {
            val total = contentLength()
            val buffer = ByteArray(16 * 1024)
            var uploaded = 0L
            var lastReportedPct = -1
            FileInputStream(file).use { input ->
                while (true) {
                    val read = input.read(buffer)
                    if (read == -1) break
                    sink.write(buffer, 0, read)
                    uploaded += read
                    val pct = if (total > 0L) ((uploaded * 100) / total).toInt() else 50
                    if (pct != lastReportedPct) {
                        lastReportedPct = pct
                        onProgress(uploaded, total)
                    }
                }
            }
        }
    }
}
