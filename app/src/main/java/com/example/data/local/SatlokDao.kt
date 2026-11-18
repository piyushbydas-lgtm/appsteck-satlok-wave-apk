package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SatlokDao {

    // Content Items (Videos, Audios, AudioBooks, Shastrarth, Documents/PDFs)
    @Query("SELECT * FROM content_items WHERE type = :mediaType ORDER BY createdAt DESC")
    fun observeContentByType(mediaType: String): Flow<List<ContentItemEntity>>

    @Query("SELECT * FROM content_items WHERE type = :mediaType ORDER BY createdAt DESC")
    suspend fun getContentByType(mediaType: String): List<ContentItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertContent(item: ContentItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertContents(items: List<ContentItemEntity>)

    @Query("DELETE FROM content_items WHERE id = :id")
    suspend fun deleteContentById(id: String)

    @Query("UPDATE content_items SET isFeatured = NOT isFeatured WHERE id = :id")
    suspend fun toggleFeatured(id: String)

    @Query("UPDATE content_items SET isDraft = NOT isDraft WHERE id = :id")
    suspend fun toggleDraft(id: String)

    // Photo Albums & Photos
    @Transaction
    @Query("SELECT * FROM photo_albums ORDER BY createdAt DESC")
    fun observePhotoAlbums(): Flow<List<PhotoAlbumWithPhotos>>

    @Transaction
    @Query("SELECT * FROM photo_albums ORDER BY createdAt DESC")
    suspend fun getPhotoAlbums(): List<PhotoAlbumWithPhotos>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAlbum(album: PhotoAlbumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAlbums(albums: List<PhotoAlbumEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPhoto(photo: PhotoItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPhotos(photos: List<PhotoItemEntity>)

    @Query("DELETE FROM photo_items WHERE id = :photoId AND albumId = :albumId")
    suspend fun deletePhoto(albumId: String, photoId: String)

    @Query("DELETE FROM photo_albums WHERE id = :albumId")
    suspend fun deleteAlbum(albumId: String)

    // App Configuration
    @Query("SELECT * FROM app_config WHERE id = 1")
    fun observeAppConfig(): Flow<AppConfigEntity?>

    @Query("SELECT * FROM app_config WHERE id = 1")
    suspend fun getAppConfig(): AppConfigEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAppConfig(config: AppConfigEntity)

    // Metadata for one-time initial seeding
    @Query("SELECT value FROM db_meta WHERE `key` = :key LIMIT 1")
    suspend fun getMetaValue(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setMeta(meta: DbMetaEntity)
}
