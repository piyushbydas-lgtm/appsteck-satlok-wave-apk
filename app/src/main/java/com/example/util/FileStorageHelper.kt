package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

data class LocalSavedMedia(
    val absolutePath: String,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val generatedThumbnailPath: String = "",
    val detectedDuration: String = ""
)

object FileStorageHelper {

    fun saveMediaToInternalStorage(
        context: Context,
        uri: Uri,
        subfolder: String,
        prefix: String,
        defaultExtension: String
    ): String? {
        return try {
            val saved = copyUriWithProgressBlocking(
                context = context,
                uri = uri,
                subfolder = subfolder,
                prefix = prefix,
                defaultExtension = defaultExtension,
                onProgress = { _, _ -> }
            )
            saved.absolutePath
        } catch (_: Exception) {
            null
        }
    }

    suspend fun copyMediaWithProgress(
        context: Context,
        uri: Uri,
        subfolder: String,
        prefix: String,
        defaultExtension: String,
        extractVideoMetadata: Boolean = false,
        onProgress: (Float, String) -> Unit
    ): Result<LocalSavedMedia> = withContext(Dispatchers.IO) {
        runCatching {
            val saved = copyUriWithProgressBlocking(
                context = context,
                uri = uri,
                subfolder = subfolder,
                prefix = prefix,
                defaultExtension = defaultExtension,
                onProgress = onProgress
            )
            if (extractVideoMetadata) {
                val meta = extractVideoFrameAndDuration(context, saved.absolutePath)
                saved.copy(
                    generatedThumbnailPath = meta.first,
                    detectedDuration = meta.second
                )
            } else {
                saved
            }
        }
    }

    private fun copyUriWithProgressBlocking(
        context: Context,
        uri: Uri,
        subfolder: String,
        prefix: String,
        defaultExtension: String,
        onProgress: (Float, String) -> Unit
    ): LocalSavedMedia {
        val resolver = context.contentResolver
        val resolvedMime = resolver.getType(uri).orEmpty().ifBlank { guessMimeFromUri(uri, defaultExtension) }
        val extension = getExtensionFromUri(context, uri, defaultExtension)
        val displayName = queryDisplayName(context, uri) ?: "${prefix}_${System.currentTimeMillis()}.$extension"
        val declaredSize = queryFileSize(context, uri)

        val dir = File(context.filesDir, subfolder)
        if (!dir.exists() && !dir.mkdirs()) {
            throw IllegalStateException("स्टोरेज फ़ोल्डर नहीं बन सका: ${dir.absolutePath}")
        }
        val fileName = "${prefix}_${System.currentTimeMillis()}.$extension"
        val destFile = File(dir, fileName)

        val input = resolver.openInputStream(uri)
            ?: throw IllegalStateException("चयनित फाइल को पढ़ा नहीं जा सका। कृपया पुनः चुनें।")

        var totalBytes = 0L
        try {
            input.use { stream ->
                FileOutputStream(destFile).use { output ->
                    val buffer = ByteArray(16 * 1024)
                    var lastReportedPct = -1
                    while (true) {
                        val read = stream.read(buffer)
                        if (read == -1) break
                        output.write(buffer, 0, read)
                        totalBytes += read
                        if (declaredSize > 0L) {
                            val fraction = (totalBytes.toFloat() / declaredSize.toFloat()).coerceIn(0f, 1f)
                            val pct = (fraction * 100).toInt()
                            if (pct != lastReportedPct) {
                                lastReportedPct = pct
                                onProgress(fraction, "फ़ाइल सुरक्षित की जा रही है ($pct%)...")
                            }
                        } else {
                            onProgress(0.5f, "फ़ाइल पढ़ी जा रही है (${formatSize(totalBytes)})...")
                        }
                    }
                    output.flush()
                }
            }
        } catch (t: Throwable) {
            if (destFile.exists()) destFile.delete()
            throw t
        }

        if (!destFile.exists() || destFile.length() <= 0L) {
            if (destFile.exists()) destFile.delete()
            throw IllegalStateException("चयनित फाइल खाली (0 bytes) है या सेव नहीं हो सकी।")
        }

        onProgress(1f, "फ़ाइल सत्यापित (${formatSize(destFile.length())})")
        return LocalSavedMedia(
            absolutePath = destFile.absolutePath,
            fileName = displayName,
            mimeType = resolvedMime,
            sizeBytes = destFile.length()
        )
    }

    private fun extractVideoFrameAndDuration(context: Context, filePath: String): Pair<String, String> {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(filePath)
            val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
            val formattedDuration = if (durationMs > 0L) formatDurationMs(durationMs) else ""

            val frame = retriever.getFrameAtTime(1_000_000L, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                ?: retriever.frameAtTime
            val thumbPath = if (frame != null) {
                val thumbDir = File(context.filesDir, "video_thumbs")
                if (!thumbDir.exists()) thumbDir.mkdirs()
                val thumbFile = File(thumbDir, "thumb_${System.currentTimeMillis()}.jpg")
                FileOutputStream(thumbFile).use { out ->
                    frame.compress(Bitmap.CompressFormat.JPEG, 85, out)
                }
                frame.recycle()
                if (thumbFile.exists() && thumbFile.length() > 0L) thumbFile.absolutePath else ""
            } else {
                ""
            }
            Pair(thumbPath, formattedDuration)
        } catch (_: Exception) {
            Pair("", "")
        } finally {
            try {
                retriever.release()
            } catch (_: Exception) {}
        }
    }

    private fun queryFileSize(context: Context, uri: Uri): Long {
        return try {
            context.contentResolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val idx = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (idx >= 0 && !cursor.isNull(idx)) cursor.getLong(idx) else -1L
                } else -1L
            } ?: -1L
        } catch (_: Exception) {
            -1L
        }
    }

    private fun queryDisplayName(context: Context, uri: Uri): String? {
        return try {
            context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (idx >= 0 && !cursor.isNull(idx)) cursor.getString(idx) else null
                } else null
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun getExtensionFromUri(context: Context, uri: Uri, defaultExtension: String): String {
        val type = context.contentResolver.getType(uri)
        if (!type.isNullOrBlank()) {
            val fromMime = MimeTypeMap.getSingleton().getExtensionFromMimeType(type)
            if (!fromMime.isNullOrBlank()) return fromMime
            when {
                type.contains("video/mp4") -> return "mp4"
                type.contains("video/x-matroska") || type.contains("mkv") -> return "mkv"
                type.contains("video/webm") -> return "webm"
                type.contains("video/3gpp") -> return "3gp"
                type.contains("video/quicktime") -> return "mov"
                type.contains("image/png") -> return "png"
                type.contains("image/jpeg") -> return "jpg"
                type.contains("image/webp") -> return "webp"
                type.contains("application/pdf") -> return "pdf"
                type.contains("audio/mpeg") -> return "mp3"
                type.contains("audio/mp4") -> return "m4a"
            }
        }
        val path = uri.toString().substringBefore("?").lowercase()
        val ext = path.substringAfterLast(".", "")
        if (ext.length in 2..5 && ext.all { it.isLetterOrDigit() }) {
            return ext
        }
        return defaultExtension
    }

    private fun guessMimeFromUri(uri: Uri, defaultExtension: String): String {
        val ext = uri.toString().substringBefore("?").substringAfterLast(".", defaultExtension).lowercase()
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ?: when (ext) {
            "mp4", "m4v" -> "video/mp4"
            "mkv" -> "video/x-matroska"
            "webm" -> "video/webm"
            "3gp" -> "video/3gpp"
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "webp" -> "image/webp"
            "pdf" -> "application/pdf"
            "mp3" -> "audio/mpeg"
            else -> "application/octet-stream"
        }
    }

    fun formatSize(bytes: Long): String {
        return when {
            bytes < 1024L -> "$bytes B"
            bytes < 1024L * 1024L -> String.format("%.1f KB", bytes / 1024.0)
            else -> String.format("%.2f MB", bytes / (1024.0 * 1024.0))
        }
    }

    fun formatDurationMs(ms: Long): String {
        val totalSeconds = (ms / 1000L).coerceAtLeast(0L)
        val hours = totalSeconds / 3600L
        val mins = (totalSeconds % 3600L) / 60L
        val secs = totalSeconds % 60L
        return if (hours > 0L) {
            String.format("%02d:%02d:%02d", hours, mins, secs)
        } else {
            String.format("%02d:%02d", mins, secs)
        }
    }
}
