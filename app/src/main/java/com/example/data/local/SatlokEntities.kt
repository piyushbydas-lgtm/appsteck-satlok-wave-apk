package com.example.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.data.model.AppConfig
import com.example.data.model.ContentItem
import com.example.data.model.KnowledgeCategory
import com.example.data.model.MediaType
import com.example.data.model.PhotoAlbum
import com.example.data.model.PhotoItem

@Entity(tableName = "content_items")
data class ContentItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val type: String,
    val category: String,
    val speaker: String,
    val playlist: String,
    val thumbnailUrl: String,
    val mediaUrl: String,
    val duration: String,
    val scriptureRef: String?,
    val hindiExplanation: String?,
    val isFeatured: Boolean,
    val isDraft: Boolean,
    val viewsCount: Int,
    val createdAt: Long,
    val mimeType: String,
    val fileSizeBytes: Long,
    val storageType: String,
    val youtubeVideoId: String
) {
    fun toModel(): ContentItem = ContentItem(
        id = id,
        title = title,
        description = description,
        type = runCatching { MediaType.valueOf(type) }.getOrDefault(MediaType.VIDEO),
        category = runCatching { KnowledgeCategory.valueOf(category) }.getOrDefault(KnowledgeCategory.SATSANG),
        speaker = speaker,
        playlist = playlist,
        thumbnailUrl = thumbnailUrl,
        mediaUrl = mediaUrl,
        duration = duration,
        scriptureRef = scriptureRef,
        hindiExplanation = hindiExplanation,
        isFeatured = isFeatured,
        isDraft = isDraft,
        viewsCount = viewsCount,
        createdAt = createdAt,
        mimeType = mimeType,
        fileSizeBytes = fileSizeBytes,
        storageType = storageType,
        youtubeVideoId = youtubeVideoId
    )

    companion object {
        fun fromModel(item: ContentItem): ContentItemEntity = ContentItemEntity(
            id = item.id,
            title = item.title,
            description = item.description,
            type = item.type.name,
            category = item.category.name,
            speaker = item.speaker,
            playlist = item.playlist,
            thumbnailUrl = item.thumbnailUrl,
            mediaUrl = item.mediaUrl,
            duration = item.duration,
            scriptureRef = item.scriptureRef,
            hindiExplanation = item.hindiExplanation,
            isFeatured = item.isFeatured,
            isDraft = item.isDraft,
            viewsCount = item.viewsCount,
            createdAt = item.createdAt,
            mimeType = item.mimeType,
            fileSizeBytes = item.fileSizeBytes,
            storageType = item.storageType,
            youtubeVideoId = item.youtubeVideoId
        )
    }
}

@Entity(tableName = "photo_albums")
data class PhotoAlbumEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val coverUrl: String,
    val createdAt: Long
)

@Entity(
    tableName = "photo_items",
    foreignKeys = [
        ForeignKey(
            entity = PhotoAlbumEntity::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["albumId"])]
)
data class PhotoItemEntity(
    @PrimaryKey val id: String,
    val albumId: String,
    val title: String,
    val imageUrl: String,
    val caption: String,
    val createdAt: Long
) {
    fun toModel(): PhotoItem = PhotoItem(
        id = id,
        title = title,
        imageUrl = imageUrl,
        caption = caption
    )
}

data class PhotoAlbumWithPhotos(
    @Embedded val album: PhotoAlbumEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val photos: List<PhotoItemEntity>
) {
    fun toModel(): PhotoAlbum = PhotoAlbum(
        id = album.id,
        title = album.title,
        description = album.description,
        coverUrl = album.coverUrl,
        photos = photos.sortedBy { it.createdAt }.map { it.toModel() }
    )
}

@Entity(tableName = "app_config")
data class AppConfigEntity(
    @PrimaryKey val id: Int = 1,
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
) {
    fun toModel(): AppConfig = AppConfig(
        appName = appName,
        customLogoUrl = customLogoUrl,
        aboutSectionPhoto = aboutSectionPhoto,
        srishtiRachnaPhoto = srishtiRachnaPhoto,
        allReligionsPhoto = allReligionsPhoto,
        granthSaarPhoto = granthSaarPhoto,
        shastrarthPhoto = shastrarthPhoto,
        srishtiRachnaMainVideoUrl = srishtiRachnaMainVideoUrl,
        contactEmail = contactEmail,
        officialWebsite = officialWebsite,
        helplineNumber = helplineNumber
    )

    companion object {
        fun fromModel(config: AppConfig): AppConfigEntity = AppConfigEntity(
            id = 1,
            appName = config.appName,
            customLogoUrl = config.customLogoUrl,
            aboutSectionPhoto = config.aboutSectionPhoto,
            srishtiRachnaPhoto = config.srishtiRachnaPhoto,
            allReligionsPhoto = config.allReligionsPhoto,
            granthSaarPhoto = config.granthSaarPhoto,
            shastrarthPhoto = config.shastrarthPhoto,
            srishtiRachnaMainVideoUrl = config.srishtiRachnaMainVideoUrl,
            contactEmail = config.contactEmail,
            officialWebsite = config.officialWebsite,
            helplineNumber = config.helplineNumber
        )
    }
}

@Entity(tableName = "db_meta")
data class DbMetaEntity(
    @PrimaryKey val key: String,
    val value: String
)
