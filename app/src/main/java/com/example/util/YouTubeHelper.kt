package com.example.util

import java.net.URI
import java.net.URLDecoder

object YouTubeHelper {

    private val VIDEO_ID_REGEX = Regex("^[A-Za-z0-9_-]{11}$")
    private val YOUTUBE_HOSTS = setOf(
        "youtube.com",
        "www.youtube.com",
        "m.youtube.com",
        "music.youtube.com",
        "youtu.be",
        "www.youtu.be",
        "youtube-nocookie.com",
        "www.youtube-nocookie.com"
    )
    private val NON_YOUTUBE_EXTENSIONS = listOf(
        ".mp4", ".mkv", ".webm", ".3gp", ".mov", ".avi", ".m4v",
        ".mp3", ".wav", ".ogg", ".m4a",
        ".jpg", ".jpeg", ".png", ".webp", ".pdf"
    )

    fun extractVideoId(rawInput: String): String {
        val trimmed = rawInput.trim()
        if (trimmed.isEmpty()) return ""

        // Never treat local device paths, content URIs, or direct media files as YouTube IDs
        val lower = trimmed.lowercase()
        if (trimmed.startsWith("/") ||
            lower.startsWith("file:") ||
            lower.startsWith("content:") ||
            NON_YOUTUBE_EXTENSIONS.any { ext -> lower.substringBefore("?").endsWith(ext) }
        ) {
            return ""
        }

        // Bare 11-character YouTube video ID without path/scheme characters
        if (!trimmed.contains("/") && !trimmed.contains(".") && !trimmed.contains(":") && VIDEO_ID_REGEX.matches(trimmed)) {
            return trimmed
        }

        val normalizedUrl = if (lower.startsWith("http://") || lower.startsWith("https://")) {
            trimmed
        } else if (lower.contains("youtube.com") || lower.contains("youtu.be") || lower.contains("youtube-nocookie.com")) {
            "https://$trimmed"
        } else {
            return ""
        }

        return try {
            val uri = URI(normalizedUrl)
            val host = uri.host?.lowercase() ?: return ""
            if (host !in YOUTUBE_HOSTS && !host.endsWith(".youtube.com")) {
                return ""
            }

            val segments = (uri.path ?: "")
                .split("/")
                .filter { it.isNotBlank() }

            val candidate = when {
                host.contains("youtu.be") -> segments.firstOrNull().orEmpty()
                segments.size >= 2 && segments[0] in setOf("shorts", "embed", "live", "v", "e") -> segments[1]
                else -> extractQueryParam(uri.rawQuery, "v")
            }

            val clean = candidate.substringBefore("?").substringBefore("&").substringBefore("#").trim()
            if (VIDEO_ID_REGEX.matches(clean)) clean else ""
        } catch (_: Exception) {
            ""
        }
    }

    fun isYouTubeUrl(url: String): Boolean = extractVideoId(url).isNotEmpty()

    fun isShortsUrl(url: String): Boolean {
        val lower = url.trim().lowercase()
        return isYouTubeUrl(url) && (lower.contains("/shorts/"))
    }

    fun getThumbnailUrl(videoId: String): String {
        return if (VIDEO_ID_REGEX.matches(videoId)) {
            "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
        } else {
            ""
        }
    }

    fun getWatchUrl(videoId: String, isShorts: Boolean = false): String {
        return if (!VIDEO_ID_REGEX.matches(videoId)) {
            ""
        } else if (isShorts) {
            "https://www.youtube.com/shorts/$videoId"
        } else {
            "https://www.youtube.com/watch?v=$videoId"
        }
    }

    private fun extractQueryParam(rawQuery: String?, key: String): String {
        if (rawQuery.isNullOrBlank()) return ""
        return rawQuery.split("&")
            .mapNotNull { pair ->
                val parts = pair.split("=", limit = 2)
                if (parts.size == 2) {
                    val decodedKey = try {
                        URLDecoder.decode(parts[0], "UTF-8")
                    } catch (_: Exception) {
                        parts[0]
                    }
                    if (decodedKey == key) {
                        try {
                            URLDecoder.decode(parts[1], "UTF-8")
                        } catch (_: Exception) {
                            parts[1]
                        }
                    } else null
                } else null
            }
            .firstOrNull()
            .orEmpty()
    }
}
