package com.example.data.repository

import android.content.Context
import com.example.data.local.AppConfigEntity
import com.example.data.local.ContentItemEntity
import com.example.data.local.PhotoAlbumEntity
import com.example.data.local.PhotoItemEntity
import com.example.data.local.SatlokDatabase
import com.example.data.model.AppConfig
import com.example.data.model.BhaktiBodhTopic
import com.example.data.model.ContentItem
import com.example.data.model.KnowledgeCategory
import com.example.data.model.MediaType
import com.example.data.model.PhotoAlbum
import com.example.data.model.ScriptureEvidence
import com.example.data.model.SrishtiRachnaSection
import com.example.util.AdminAuthManager
import com.example.util.AdminAuthResult
import com.example.util.YouTubeHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.UUID

object SatlokRepository {

    private var repoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var collectorJob: Job? = null
    @Volatile
    private var database: SatlokDatabase? = null

    // App Configuration & Branding
    private val _appConfig = MutableStateFlow(AppConfig())
    val appConfig: StateFlow<AppConfig> = _appConfig.asStateFlow()

    // Admin Authentication State
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _adminEmail = MutableStateFlow("")
    val adminEmail: StateFlow<String> = _adminEmail.asStateFlow()

    // Content Lists (backed by Room when initialized)
    private val _videos = MutableStateFlow<List<ContentItem>>(InitialData.videos)
    val videos: StateFlow<List<ContentItem>> = _videos.asStateFlow()

    private val _audios = MutableStateFlow<List<ContentItem>>(InitialData.audios)
    val audios: StateFlow<List<ContentItem>> = _audios.asStateFlow()

    private val _documents = MutableStateFlow<List<ContentItem>>(emptyList())
    val documents: StateFlow<List<ContentItem>> = _documents.asStateFlow()

    private val _photoAlbums = MutableStateFlow<List<PhotoAlbum>>(InitialData.photoAlbums)
    val photoAlbums: StateFlow<List<PhotoAlbum>> = _photoAlbums.asStateFlow()

    private val _srishtiRachnaSections = MutableStateFlow<List<SrishtiRachnaSection>>(InitialData.srishtiRachnaSections)
    val srishtiRachnaSections: StateFlow<List<SrishtiRachnaSection>> = _srishtiRachnaSections.asStateFlow()

    private val _scriptureEvidences = MutableStateFlow<List<ScriptureEvidence>>(InitialData.scriptureEvidences)
    val scriptureEvidences: StateFlow<List<ScriptureEvidence>> = _scriptureEvidences.asStateFlow()

    private val _bhaktiBodhTopics = MutableStateFlow<List<BhaktiBodhTopic>>(InitialData.bhaktiBodhTopics)
    val bhaktiBodhTopics: StateFlow<List<BhaktiBodhTopic>> = _bhaktiBodhTopics.asStateFlow()

    private val _audioBooks = MutableStateFlow<List<ContentItem>>(InitialData.audioBooks)
    val audioBooks: StateFlow<List<ContentItem>> = _audioBooks.asStateFlow()

    private val _shastrarthItems = MutableStateFlow<List<ContentItem>>(InitialData.shastrarthItems)
    val shastrarthItems: StateFlow<List<ContentItem>> = _shastrarthItems.asStateFlow()

    private val _isLoadingContent = MutableStateFlow(false)
    val isLoadingContent: StateFlow<Boolean> = _isLoadingContent.asStateFlow()

    fun init(context: Context) {
        _isLoadingContent.value = true

        val db = runBlocking(Dispatchers.IO) {
            flushWritesForTesting()
            collectorJob?.cancelAndJoin()
            collectorJob = null
            val instance = SatlokDatabase.getInstance(context.applicationContext)
            database = instance
            SatlokDatabase.seedIfNeeded(instance)
            val dao = instance.satlokDao()
            _videos.value = dao.getContentByType(MediaType.VIDEO.name).map { it.toModel() }
            _audios.value = dao.getContentByType(MediaType.AUDIO.name).map { it.toModel() }
            _documents.value = dao.getContentByType(MediaType.DOCUMENT.name).map { it.toModel() }
            _photoAlbums.value = dao.getPhotoAlbums().map { it.toModel() }
            dao.getAppConfig()?.let { _appConfig.value = it.toModel() }
            instance
        }
        _isLoadingContent.value = false

        val dao = db.satlokDao()
        collectorJob = repoScope.launch {
            launch {
                dao.observeContentByType(MediaType.VIDEO.name).collect { entities ->
                    _videos.value = entities.map { it.toModel() }
                }
            }
            launch {
                dao.observeContentByType(MediaType.AUDIO.name).collect { entities ->
                    _audios.value = entities.map { it.toModel() }
                }
            }
            launch {
                dao.observeContentByType(MediaType.DOCUMENT.name).collect { entities ->
                    _documents.value = entities.map { it.toModel() }
                }
            }
            launch {
                dao.observePhotoAlbums().collect { entities ->
                    _photoAlbums.value = entities.map { it.toModel() }
                }
            }
            launch {
                dao.observeAppConfig().collect { entity ->
                    if (entity != null) {
                        _appConfig.value = entity.toModel()
                    }
                }
            }
        }
    }

    private val activeWriteJobs = java.util.Collections.synchronizedSet(mutableSetOf<Job>())

    private fun launchPersist(block: suspend (com.example.data.local.SatlokDao) -> Unit) {
        val dao = database?.satlokDao() ?: return
        val job = repoScope.launch {
            block(dao)
        }
        activeWriteJobs.add(job)
        job.invokeOnCompletion { activeWriteJobs.remove(job) }
    }

    internal suspend fun flushWritesForTesting() {
        while (true) {
            val snapshot = synchronized(activeWriteJobs) { activeWriteJobs.toList() }
            if (snapshot.isEmpty()) break
            snapshot.forEach { it.join() }
        }
    }

    internal suspend fun shutdownForTesting() {
        flushWritesForTesting()
        collectorJob?.cancelAndJoin()
        collectorJob = null
        database = null
        _isAdminLoggedIn.value = false
        _adminEmail.value = ""
        SatlokDatabase.resetInstanceForTesting()
    }

    internal suspend fun resetForTesting(context: Context) {
        shutdownForTesting()
        init(context)
    }

    // Secure Admin Authentication (no hardcoded passwords!)
    fun isAdminAccountConfigured(context: Context): Boolean =
        AdminAuthManager.isAdminConfigured(context)

    fun setupInitialAdmin(
        context: Context,
        email: String,
        password: String,
        confirmPassword: String
    ): AdminAuthResult {
        val result = AdminAuthManager.setupInitialAdmin(context, email, password, confirmPassword)
        if (result.success) {
            _isAdminLoggedIn.value = true
            _adminEmail.value = result.normalizedEmail
        }
        return result
    }

    fun loginAdmin(context: Context, email: String, password: String): AdminAuthResult {
        val result = AdminAuthManager.verifyAndLogin(context, email, password)
        if (result.success) {
            _isAdminLoggedIn.value = true
            _adminEmail.value = result.normalizedEmail
        }
        return result
    }

    fun changeAdminPassword(
        context: Context,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): AdminAuthResult {
        if (!_isAdminLoggedIn.value) {
            return AdminAuthResult(false, "पासवर्ड बदलने के लिए एडमिन लॉगिन आवश्यक है।")
        }
        return AdminAuthManager.changePassword(context, currentPassword, newPassword, confirmPassword)
    }

    fun logoutAdmin() {
        _isAdminLoggedIn.value = false
        _adminEmail.value = ""
    }

    // Logo & App Config Management (Admin only)
    fun updateInAppLogo(logoUrl: String) {
        if (!_isAdminLoggedIn.value) return
        val updated = _appConfig.value.copy(customLogoUrl = logoUrl.ifBlank { null })
        _appConfig.value = updated
        persistAppConfig(updated)
    }

    fun updateSectionPhoto(sectionKey: String, photoUrl: String) {
        if (!_isAdminLoggedIn.value) return
        val cleaned = photoUrl.ifBlank { null }
        val updated = when (sectionKey) {
            "about" -> _appConfig.value.copy(aboutSectionPhoto = cleaned)
            "srishti" -> _appConfig.value.copy(srishtiRachnaPhoto = cleaned)
            "religions" -> _appConfig.value.copy(allReligionsPhoto = cleaned)
            "granth" -> _appConfig.value.copy(granthSaarPhoto = cleaned)
            "shastrarth" -> _appConfig.value.copy(shastrarthPhoto = cleaned)
            else -> _appConfig.value
        }
        _appConfig.value = updated
        persistAppConfig(updated)
    }

    fun updateSrishtiRachnaVideo(newUrl: String) {
        if (!_isAdminLoggedIn.value) return
        val updated = _appConfig.value.copy(srishtiRachnaMainVideoUrl = newUrl)
        _appConfig.value = updated
        persistAppConfig(updated)
    }

    private fun persistAppConfig(config: AppConfig) {
        launchPersist { dao ->
            dao.upsertAppConfig(AppConfigEntity.fromModel(config))
        }
    }

    // Video Operations
    fun addVideo(
        title: String,
        description: String,
        mediaUrl: String,
        category: KnowledgeCategory,
        playlist: String,
        thumbnailUrl: String = "",
        duration: String = "45:00",
        isDraft: Boolean = false,
        mimeType: String = "",
        fileSizeBytes: Long = 0L,
        storageType: String = ""
    ): Boolean {
        if (!_isAdminLoggedIn.value) return false
        val trimmedUrl = mediaUrl.trim()
        if (title.isBlank() || trimmedUrl.isBlank()) return false

        val ytId = YouTubeHelper.extractVideoId(trimmedUrl)
        val resolvedThumb = when {
            thumbnailUrl.isNotBlank() -> thumbnailUrl.trim()
            ytId.isNotEmpty() -> YouTubeHelper.getThumbnailUrl(ytId)
            else -> ""
        }
        val resolvedStorage = storageType.ifBlank {
            when {
                ytId.isNotEmpty() -> "YOUTUBE"
                trimmedUrl.startsWith("http://", true) || trimmedUrl.startsWith("https://", true) -> "CLOUD"
                trimmedUrl.startsWith("/") -> "LOCAL"
                else -> "LINK"
            }
        }

        val item = ContentItem(
            id = "v_" + UUID.randomUUID().toString().take(8),
            title = title.trim(),
            description = description.trim(),
            type = MediaType.VIDEO,
            category = category,
            playlist = playlist.ifBlank { "General" },
            thumbnailUrl = resolvedThumb,
            mediaUrl = trimmedUrl,
            duration = duration.ifBlank { "00:00" },
            isDraft = isDraft,
            createdAt = System.currentTimeMillis(),
            mimeType = mimeType,
            fileSizeBytes = fileSizeBytes,
            storageType = resolvedStorage,
            youtubeVideoId = ytId
        )
        _videos.value = listOf(item) + _videos.value
        launchPersist { dao -> dao.upsertContent(ContentItemEntity.fromModel(item)) }
        return true
    }

    fun updateVideo(updated: ContentItem): Boolean {
        if (!_isAdminLoggedIn.value) return false
        _videos.value = _videos.value.map { if (it.id == updated.id) updated else it }
        launchPersist { dao -> dao.upsertContent(ContentItemEntity.fromModel(updated)) }
        return true
    }

    fun deleteVideo(videoId: String): Boolean {
        if (!_isAdminLoggedIn.value) return false
        _videos.value = _videos.value.filterNot { it.id == videoId }
        launchPersist { dao -> dao.deleteContentById(videoId) }
        return true
    }

    fun toggleFeatured(videoId: String): Boolean {
        if (!_isAdminLoggedIn.value) return false
        _videos.value = _videos.value.map {
            if (it.id == videoId) it.copy(isFeatured = !it.isFeatured) else it
        }
        launchPersist { dao -> dao.toggleFeatured(videoId) }
        return true
    }

    fun toggleVideoPublishStatus(videoId: String): Boolean {
        if (!_isAdminLoggedIn.value) return false
        _videos.value = _videos.value.map {
            if (it.id == videoId) it.copy(isDraft = !it.isDraft) else it
        }
        launchPersist { dao -> dao.toggleDraft(videoId) }
        return true
    }

    // Audio Operations
    fun addAudio(
        title: String,
        description: String,
        mediaUrl: String,
        category: KnowledgeCategory,
        playlist: String,
        duration: String = "15:00",
        mimeType: String = "",
        fileSizeBytes: Long = 0L,
        storageType: String = "LINK"
    ): Boolean {
        if (!_isAdminLoggedIn.value) return false
        if (title.isBlank() || mediaUrl.isBlank()) return false
        val item = ContentItem(
            id = "a_" + UUID.randomUUID().toString().take(8),
            title = title.trim(),
            description = description.trim(),
            type = MediaType.AUDIO,
            category = category,
            playlist = playlist.ifBlank { "भक्ति बोध ऑडियो" },
            mediaUrl = mediaUrl.trim(),
            duration = duration.ifBlank { "15:00" },
            createdAt = System.currentTimeMillis(),
            mimeType = mimeType,
            fileSizeBytes = fileSizeBytes,
            storageType = storageType
        )
        _audios.value = listOf(item) + _audios.value
        launchPersist { dao -> dao.upsertContent(ContentItemEntity.fromModel(item)) }
        return true
    }

    fun deleteAudio(audioId: String): Boolean {
        if (!_isAdminLoggedIn.value) return false
        _audios.value = _audios.value.filterNot { it.id == audioId }
        launchPersist { dao -> dao.deleteContentById(audioId) }
        return true
    }

    // Document / PDF Operations
    fun addDocument(
        title: String,
        description: String,
        mediaUrl: String,
        category: KnowledgeCategory = KnowledgeCategory.GRANTH_SAAR,
        playlist: String = "पवित्र पुस्तकें व दस्तावेज़",
        mimeType: String = "application/pdf",
        fileSizeBytes: Long = 0L,
        storageType: String = "LOCAL"
    ): Boolean {
        if (!_isAdminLoggedIn.value) return false
        if (title.isBlank() || mediaUrl.isBlank()) return false
        val item = ContentItem(
            id = "doc_" + UUID.randomUUID().toString().take(8),
            title = title.trim(),
            description = description.trim(),
            type = MediaType.DOCUMENT,
            category = category,
            playlist = playlist.ifBlank { "दस्तावेज़ व PDF" },
            mediaUrl = mediaUrl.trim(),
            createdAt = System.currentTimeMillis(),
            mimeType = mimeType,
            fileSizeBytes = fileSizeBytes,
            storageType = storageType
        )
        _documents.value = listOf(item) + _documents.value
        launchPersist { dao -> dao.upsertContent(ContentItemEntity.fromModel(item)) }
        return true
    }

    fun deleteDocument(docId: String): Boolean {
        if (!_isAdminLoggedIn.value) return false
        _documents.value = _documents.value.filterNot { it.id == docId }
        launchPersist { dao -> dao.deleteContentById(docId) }
        return true
    }

    // Photo Operations
    fun createPhotoAlbum(title: String, description: String, coverUrl: String): Boolean {
        if (!_isAdminLoggedIn.value) return false
        if (title.isBlank()) return false
        val id = "alb_" + UUID.randomUUID().toString().take(8)
        val createdAt = System.currentTimeMillis()
        val newAlbum = PhotoAlbum(
            id = id,
            title = title.trim(),
            description = description.trim(),
            coverUrl = coverUrl,
            photos = emptyList()
        )
        _photoAlbums.value = listOf(newAlbum) + _photoAlbums.value
        launchPersist { dao ->
            dao.upsertAlbum(
                PhotoAlbumEntity(
                    id = id,
                    title = title.trim(),
                    description = description.trim(),
                    coverUrl = coverUrl,
                    createdAt = createdAt
                )
            )
        }
        return true
    }

    fun addPhotoToAlbum(albumId: String, title: String, imageUrl: String, caption: String): Boolean {
        if (!_isAdminLoggedIn.value) return false
        if (albumId.isBlank() || title.isBlank() || imageUrl.isBlank()) return false
        val photoId = "p_" + UUID.randomUUID().toString().take(8)
        val createdAt = System.currentTimeMillis()
        val photo = com.example.data.model.PhotoItem(
            id = photoId,
            title = title.trim(),
            imageUrl = imageUrl.trim(),
            caption = caption.trim()
        )
        _photoAlbums.value = _photoAlbums.value.map { album ->
            if (album.id == albumId) {
                album.copy(photos = album.photos + photo)
            } else {
                album
            }
        }
        launchPersist { dao ->
            dao.upsertPhoto(
                PhotoItemEntity(
                    id = photoId,
                    albumId = albumId,
                    title = photo.title,
                    imageUrl = photo.imageUrl,
                    caption = photo.caption,
                    createdAt = createdAt
                )
            )
        }
        return true
    }

    fun deletePhoto(albumId: String, photoId: String): Boolean {
        if (!_isAdminLoggedIn.value) return false
        _photoAlbums.value = _photoAlbums.value.map { album ->
            if (album.id == albumId) {
                album.copy(photos = album.photos.filterNot { it.id == photoId })
            } else {
                album
            }
        }
        launchPersist { dao -> dao.deletePhoto(albumId, photoId) }
        return true
    }

    fun deleteAlbum(albumId: String): Boolean {
        if (!_isAdminLoggedIn.value) return false
        _photoAlbums.value = _photoAlbums.value.filterNot { it.id == albumId }
        launchPersist { dao -> dao.deleteAlbum(albumId) }
        return true
    }

    // Global Search across Published Videos, Audios, Documents, Shastrarth, Books, Srishti Rachna
    fun search(query: String, filterType: MediaType? = null): List<ContentItem> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()

        val allItems = mutableListOf<ContentItem>()
        allItems.addAll(_videos.value.filter { !it.isDraft })
        allItems.addAll(_audios.value.filter { !it.isDraft })
        allItems.addAll(_documents.value.filter { !it.isDraft })
        allItems.addAll(_audioBooks.value.filter { !it.isDraft })
        allItems.addAll(_shastrarthItems.value.filter { !it.isDraft })

        _srishtiRachnaSections.value.forEach {
            allItems.add(
                ContentItem(
                    id = it.id,
                    title = it.title + " - " + it.subtitle,
                    description = it.detailedText.take(150),
                    type = MediaType.ARTICLE,
                    category = KnowledgeCategory.SRISHTI_RACHNA,
                    playlist = "Srishti Rachna",
                    mediaUrl = it.videoUrl
                )
            )
        }

        _scriptureEvidences.value.forEach {
            allItems.add(
                ContentItem(
                    id = it.id,
                    title = it.scriptureName + " (" + it.chapterVerse + ")",
                    description = it.hindiExplanation,
                    type = MediaType.SCRIPTURE,
                    category = KnowledgeCategory.SHASTRARTH,
                    playlist = it.subcategory,
                    scriptureRef = it.chapterVerse,
                    hindiExplanation = it.hindiExplanation,
                    mediaUrl = it.videoUrl
                )
            )
        }

        return allItems.filter { item ->
            val matchesFilter = filterType == null || item.type == filterType
            val matchesQuery = item.title.lowercase().contains(q) ||
                item.description.lowercase().contains(q) ||
                (item.scriptureRef?.lowercase()?.contains(q) == true) ||
                (item.hindiExplanation?.lowercase()?.contains(q) == true) ||
                item.playlist.lowercase().contains(q)
            matchesFilter && matchesQuery
        }
    }

    fun extractYouTubeThumbnail(url: String): String {
        val videoId = extractYouTubeVideoId(url)
        return if (videoId.isNotEmpty()) {
            YouTubeHelper.getThumbnailUrl(videoId)
        } else {
            ""
        }
    }

    fun extractYouTubeVideoId(url: String): String = YouTubeHelper.extractVideoId(url)
}
