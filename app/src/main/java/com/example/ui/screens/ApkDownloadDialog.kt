package com.example.ui.screens

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import com.example.R
import com.example.ui.theme.AashmaniBlue
import com.example.ui.theme.DarkNavyHeading
import com.example.ui.theme.OceanBlue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Locale

private data class ApkActionStatus(
    val success: Boolean,
    val message: String
)

@Composable
fun ApkDownloadDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isExporting by remember { mutableStateOf(false) }
    var isSharing by remember { mutableStateOf(false) }
    var actionStatus by remember { mutableStateOf<ApkActionStatus?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("apk_download_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(AashmaniBlue.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Android,
                                contentDescription = null,
                                tint = AashmaniBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "APK डाउनलोड / सेव करें",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkNavyHeading,
                                fontSize = 17.sp
                            )
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // App Logo Preview
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE1F5FE))
                        .border(2.dp, AashmaniBlue, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.satlok_wave_logo),
                        contentDescription = "SATLOK WAVE App Icon",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "SATLOK WAVE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = OceanBlue,
                        fontSize = 18.sp
                    )
                )

                Text(
                    text = "संस्करण v1.0 • ${context.packageName}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Details Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0F7FC))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AashmaniBlue,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "100% सुरक्षित एवं आधिकारिक APK फाइल",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.5.sp
                                )
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AashmaniBlue,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "सीधे Downloads फ़ोल्डर में सेव और शेयर करें",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.5.sp
                                )
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = AashmaniBlue,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "कोई विज्ञापन नहीं, पूर्णतः निःशुल्क",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.5.sp
                                )
                            )
                        }
                    }
                }

                // Inline Status Banner Card
                actionStatus?.let { status ->
                    Spacer(modifier = Modifier.height(10.dp))
                    val bgColor = if (status.success) Color(0xFFE1F5FE) else Color(0xFFFFF3E0)
                    val borderColor = if (status.success) AashmaniBlue else OceanBlue
                    val textColor = if (status.success) Color(0xFF01579B) else DarkNavyHeading

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor)
                            .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                            .testTag("apk_status_banner"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (status.success) Icons.Default.CheckCircle else Icons.Default.Info,
                            contentDescription = null,
                            tint = borderColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = status.message,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = textColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Primary Save APK to Downloads Button
                Button(
                    onClick = {
                        if (!isExporting && !isSharing) {
                            isExporting = true
                            actionStatus = null
                            scope.launch {
                                val result = saveInstalledApkToDownloads(context)
                                actionStatus = result
                                isExporting = false
                                try {
                                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                                } catch (_: Exception) {}
                            }
                        }
                    },
                    enabled = !isExporting && !isSharing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_download_apk_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AashmaniBlue
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isExporting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    } else {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (isExporting) "APK सेव हो रही है..." else "APK फोन में सेव करें",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Share Actual APK File Button via FileProvider
                OutlinedButton(
                    onClick = {
                        if (!isExporting && !isSharing) {
                            isSharing = true
                            actionStatus = null
                            scope.launch {
                                val result = shareInstalledApkFile(context)
                                actionStatus = result
                                isSharing = false
                                try {
                                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                                } catch (_: Exception) {}
                            }
                        }
                    },
                    enabled = !isExporting && !isSharing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("share_apk_file_btn"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (isSharing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = OceanBlue,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    } else {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = OceanBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = if (isSharing) "APK तैयार हो रही है..." else "असली APK फाइल शेयर करें",
                        color = OceanBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Instructions
                Text(
                    text = "नोट: फोन में इंस्टॉल करते समय 'Unknown Apps' अनुमति मांगी जाए तो 'Allow from this source' ऑन करें। वेब ब्राउज़र से APK डाउनलोड करने के लिए ऊपर Export / Settings मेनू का उपयोग करें।",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

private suspend fun saveInstalledApkToDownloads(context: Context): ApkActionStatus = withContext(Dispatchers.IO) {
    val fileName = "SATLOK_WAVE_v1.0.apk"
    return@withContext try {
        val sourcePath = context.applicationInfo?.sourceDir
        if (sourcePath.isNullOrBlank()) {
            return@withContext ApkActionStatus(
                success = false,
                message = "APK स्रोत पथ उपलब्ध नहीं है। कृपया AI Studio Export मेनू से APK डाउनलोड करें।"
            )
        }
        val sourceFile = File(sourcePath)
        if (!sourceFile.exists() || !sourceFile.canRead()) {
            return@withContext ApkActionStatus(
                success = false,
                message = "डिवाइस पर इंस्टॉल्ड APK फ़ाइल पढ़ी नहीं जा सकी।"
            )
        }

        val sizeMb = String.format(Locale.US, "%.1f MB", sourceFile.length() / (1024.0 * 1024.0))

        // Attempt 1: Public MediaStore Downloads (Android 10+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, "application/vnd.android.package-archive")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/SatlokWave")
                }
                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    var copied = 0L
                    resolver.openOutputStream(uri)?.use { out ->
                        FileInputStream(sourceFile).use { input ->
                            copied = input.copyTo(out)
                        }
                    }
                    if (copied > 0L) {
                        return@withContext ApkActionStatus(
                            success = true,
                            message = "APK सफलतापूर्वक Downloads/SatlokWave/$fileName ($sizeMb) में सेव हो गई!"
                        )
                    } else {
                        try {
                            resolver.delete(uri, null, null)
                        } catch (_: Exception) {}
                    }
                }
            } catch (_: Exception) {
                // Fall back to app-specific Downloads / filesDir below
            }
        }

        // Attempt 2: App-accessible Downloads or internal files directory fallback
        val baseDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?: File(context.filesDir, "SatlokWave")
        if (!baseDir.exists()) baseDir.mkdirs()
        val destFile = File(baseDir, fileName)
        var copied = 0L
        FileInputStream(sourceFile).use { input ->
            FileOutputStream(destFile).use { out ->
                copied = input.copyTo(out)
            }
        }
        if (copied > 0L && destFile.exists()) {
            ApkActionStatus(
                success = true,
                message = "APK सेव हो गई ($sizeMb): ${destFile.absolutePath}"
            )
        } else {
            ApkActionStatus(
                success = false,
                message = "APK सेव नहीं हो सकी। स्टोरेज उपलब्ध नहीं है।"
            )
        }
    } catch (e: Exception) {
        ApkActionStatus(
            success = false,
            message = "APK सेव करने में समस्या: ${e.localizedMessage ?: "अज्ञात त्रुटि"}"
        )
    }
}

private suspend fun shareInstalledApkFile(context: Context): ApkActionStatus {
    val exportResult = withContext(Dispatchers.IO) {
        try {
            val sourcePath = context.applicationInfo?.sourceDir ?: return@withContext null
            val sourceFile = File(sourcePath)
            if (!sourceFile.exists() || !sourceFile.canRead()) return@withContext null

            val exportDir = File(context.cacheDir, "apk_exports")
            if (!exportDir.exists()) exportDir.mkdirs()
            val exportFile = File(exportDir, "SATLOK_WAVE_v1.0.apk")
            FileInputStream(sourceFile).use { input ->
                FileOutputStream(exportFile).use { out ->
                    input.copyTo(out)
                }
            }
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", exportFile)
            Pair(uri, exportFile.absolutePath)
        } catch (_: Exception) {
            null
        }
    }

    if (exportResult == null) {
        return ApkActionStatus(
            success = false,
            message = "APK शेयर करने के लिए तैयार नहीं हो सकी।"
        )
    }

    val (sharedUri, exportPath) = exportResult
    return try {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/vnd.android.package-archive"
            putExtra(Intent.EXTRA_STREAM, sharedUri)
            putExtra(Intent.EXTRA_SUBJECT, "SATLOK WAVE App APK")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val chooser = Intent.createChooser(shareIntent, "SATLOK WAVE APK शेयर करें").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
        ApkActionStatus(
            success = true,
            message = "APK शेयर मेनू खोला गया ($exportPath)।"
        )
    } catch (_: Exception) {
        ApkActionStatus(
            success = false,
            message = "इस डिवाइस/एम्युलेटर में शेयर ऐप उपलब्ध नहीं है। फाइल तैयार है: $exportPath"
        )
    }
}
