package com.example.data.model

enum class MediaType {
    VIDEO,
    AUDIO,
    PHOTO,
    ARTICLE,
    SCRIPTURE,
    BOOK,
    DOCUMENT
}

enum class KnowledgeCategory {
    SRISHTI_RACHNA,
    SHASTRARTH,
    BHAKTI_BODH,
    GRANTH_SAAR,
    ALL_RELIGIONS,
    SATSANG,
    SPIRITUAL_MUSIC,
    AUDIO_BOOK
}

data class ContentItem(
    val id: String,
    val title: String,
    val description: String,
    val type: MediaType,
    val category: KnowledgeCategory,
    val speaker: String = "संत रामपाल जी महाराज",
    val playlist: String = "General",
    val thumbnailUrl: String = "",
    val mediaUrl: String = "", // YouTube URL, MP4 link, local file path, PDF path, or MP3 stream
    val duration: String = "",
    val scriptureRef: String? = null,
    val hindiExplanation: String? = null,
    val isFeatured: Boolean = false,
    val isDraft: Boolean = false,
    val viewsCount: Int = 1250,
    val createdAt: Long = System.currentTimeMillis(),
    val mimeType: String = "",
    val fileSizeBytes: Long = 0L,
    val storageType: String = "LINK", // "YOUTUBE", "CLOUD", "LOCAL", "LINK"
    val youtubeVideoId: String = ""
)

data class CloudUploadConfig(
    val cloudName: String = "",
    val uploadPreset: String = ""
) {
    val isConfigured: Boolean
        get() = cloudName.isNotBlank() && uploadPreset.isNotBlank()
}

data class PhotoAlbum(
    val id: String,
    val title: String,
    val description: String,
    val coverUrl: String,
    val photos: List<PhotoItem> = emptyList()
) {
    val titleHindi: String get() = title
}

data class PhotoItem(
    val id: String,
    val title: String,
    val imageUrl: String,
    val caption: String
) {
    val url: String get() = imageUrl
}

data class ScriptureEvidence(
    val id: String,
    val subcategory: String, // "वेदों में प्रमाण", "गीता में प्रमाण", etc.
    val scriptureName: String, // e.g. "ऋग्वेद मण्डल 9"
    val chapterVerse: String, // e.g. "मण्डल 9 सूक्त 96 मन्त्र 17-20"
    val originalVerse: String,
    val hindiExplanation: String,
    val videoUrl: String = "",
    val relatedTeachings: String = ""
)

data class BhaktiBodhTopic(
    val id: String,
    val title: String,
    val hindiTitle: String,
    val timing: String, // "प्रातः काल", "संध्या काल", etc.
    val fullText: String,
    val meaning: String,
    val audioUrl: String = "",
    val videoUrl: String = "",
    val duration: String = "15:00"
) {
    val titleHindi: String get() = hindiTitle
}

data class SrishtiRachnaSection(
    val id: String,
    val stepNumber: Int,
    val title: String,
    val subtitle: String,
    val detailedText: String,
    val scripturalReferences: List<String>,
    val videoUrl: String = "",
    val keyPoints: List<String> = emptyList()
)

data class AppConfig(
    val appName: String = "SATLOK WAVE",
    val customLogoUrl: String? = null,
    val aboutSectionPhoto: String? = null,
    val srishtiRachnaPhoto: String? = null,
    val allReligionsPhoto: String? = null,
    val granthSaarPhoto: String? = null,
    val shastrarthPhoto: String? = null,
    val srishtiRachnaMainVideoUrl: String = "https://www.youtube.com/watch?v=0k5j-eLgX2E",
    val contactEmail: String = "info@satlokwave.com",
    val officialWebsite: String = "https://www.jagatgururampalji.org",
    val helplineNumber: String = "+91 82228 80541"
)
