package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AppConfig
import com.example.data.repository.InitialData

@Database(
    entities = [
        ContentItemEntity::class,
        PhotoAlbumEntity::class,
        PhotoItemEntity::class,
        AppConfigEntity::class,
        DbMetaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SatlokDatabase : RoomDatabase() {
    abstract fun satlokDao(): SatlokDao

    companion object {
        private const val DB_NAME = "satlok_wave.db"
        private const val SEED_KEY = "seeded_initial_v1"

        @Volatile
        private var INSTANCE: SatlokDatabase? = null
        @Volatile
        private var instanceDbPath: String? = null

        fun getInstance(context: Context): SatlokDatabase {
            val appCtx = context.applicationContext
            val dbPath = appCtx.getDatabasePath(DB_NAME).absolutePath
            val existing = INSTANCE
            if (existing != null && instanceDbPath == dbPath && existing.isOpen) {
                return existing
            }
            return synchronized(this) {
                val current = INSTANCE
                if (current != null && instanceDbPath == dbPath && current.isOpen) {
                    current
                } else {
                    current?.close()
                    Room.databaseBuilder(
                        appCtx,
                        SatlokDatabase::class.java,
                        DB_NAME
                    ).build().also {
                        INSTANCE = it
                        instanceDbPath = dbPath
                    }
                }
            }
        }

        internal fun resetInstanceForTesting() {
            synchronized(this) {
                INSTANCE?.close()
                INSTANCE = null
                instanceDbPath = null
            }
        }

        suspend fun seedIfNeeded(db: SatlokDatabase) {
            val dao = db.satlokDao()
            if (dao.getMetaValue(SEED_KEY) == "true") return

            val baseTime = 1_700_000_000_000L
            val allContents = buildList {
                InitialData.videos.forEachIndexed { idx, item ->
                    val ytId = com.example.util.YouTubeHelper.extractVideoId(item.mediaUrl)
                    add(
                        ContentItemEntity.fromModel(
                            item.copy(
                                createdAt = baseTime - (idx * 1000L),
                                storageType = if (ytId.isNotEmpty()) "YOUTUBE" else "LINK",
                                youtubeVideoId = ytId
                            )
                        )
                    )
                }
                InitialData.audios.forEachIndexed { idx, item ->
                    add(
                        ContentItemEntity.fromModel(
                            item.copy(
                                createdAt = baseTime - (idx * 1000L),
                                storageType = "LINK"
                            )
                        )
                    )
                }
                InitialData.audioBooks.forEachIndexed { idx, item ->
                    add(
                        ContentItemEntity.fromModel(
                            item.copy(
                                createdAt = baseTime - (idx * 1000L),
                                storageType = "LINK"
                            )
                        )
                    )
                }
                InitialData.shastrarthItems.forEachIndexed { idx, item ->
                    val ytId = com.example.util.YouTubeHelper.extractVideoId(item.mediaUrl)
                    add(
                        ContentItemEntity.fromModel(
                            item.copy(
                                createdAt = baseTime - (idx * 1000L),
                                storageType = if (ytId.isNotEmpty()) "YOUTUBE" else "LINK",
                                youtubeVideoId = ytId
                            )
                        )
                    )
                }
            }
            dao.upsertContents(allContents)

            val albumEntities = mutableListOf<PhotoAlbumEntity>()
            val photoEntities = mutableListOf<PhotoItemEntity>()
            InitialData.photoAlbums.forEachIndexed { aIdx, album ->
                albumEntities += PhotoAlbumEntity(
                    id = album.id,
                    title = album.title,
                    description = album.description,
                    coverUrl = album.coverUrl,
                    createdAt = baseTime - (aIdx * 1000L)
                )
                album.photos.forEachIndexed { pIdx, photo ->
                    photoEntities += PhotoItemEntity(
                        id = photo.id,
                        albumId = album.id,
                        title = photo.title,
                        imageUrl = photo.imageUrl,
                        caption = photo.caption,
                        createdAt = baseTime + (pIdx * 1000L)
                    )
                }
            }
            dao.upsertAlbums(albumEntities)
            dao.upsertPhotos(photoEntities)

            if (dao.getAppConfig() == null) {
                dao.upsertAppConfig(AppConfigEntity.fromModel(AppConfig()))
            }

            dao.setMeta(DbMetaEntity(key = SEED_KEY, value = "true"))
        }
    }
}
