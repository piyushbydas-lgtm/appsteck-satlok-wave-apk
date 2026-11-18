package com.example

import android.content.Context
import android.net.Uri
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.KnowledgeCategory
import com.example.data.repository.SatlokRepository
import com.example.ui.SatlokMainApp
import com.example.util.AdminAuthManager
import com.example.util.FileStorageHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        AdminAuthManager.clearForTesting(context)
        SatlokRepository.init(context)
    }

    @After
    fun tearDown() {
        runBlocking {
            SatlokRepository.shutdownForTesting()
        }
        AdminAuthManager.clearForTesting(context)
    }

    @Test
    fun testAppBranding() {
        val appConfig = SatlokRepository.appConfig.value
        assertEquals("SATLOK WAVE", appConfig.appName)
    }

    @Test
    fun testSrishtiRachnaLoaded() {
        val sections = SatlokRepository.srishtiRachnaSections.value
        assertTrue("Srishti Rachna sections should be present", sections.isNotEmpty())
        assertEquals("सृष्टि रचना का मूल परिचय", sections.first().title)
    }

    @Test
    fun testSecureAdminAuthSetupLoginChangePasswordAndLogout() {
        assertFalse(SatlokRepository.isAdminAccountConfigured(context))

        val ownerEmail = "owner@satlokwave.com"
        val initialSecret = "Secret#" + UUID.randomUUID().toString().take(8)
        val rotatedSecret = "NewKey#" + UUID.randomUUID().toString().take(8)

        val setupResult = SatlokRepository.setupInitialAdmin(context, ownerEmail, initialSecret, initialSecret)
        assertTrue("Initial owner setup should succeed", setupResult.success)
        assertTrue(SatlokRepository.isAdminAccountConfigured(context))
        assertTrue(SatlokRepository.isAdminLoggedIn.value)

        // Password must never be stored in plaintext inside SharedPreferences
        val rawPrefs = context.getSharedPreferences("satlok_admin_secure_prefs", Context.MODE_PRIVATE).all.toString()
        assertFalse("Raw secret must not appear in prefs", rawPrefs.contains(initialSecret))

        SatlokRepository.logoutAdmin()
        assertFalse(SatlokRepository.isAdminLoggedIn.value)

        // Wrong credentials rejected
        assertFalse(SatlokRepository.loginAdmin(context, ownerEmail, "wrong_secret_123").success)
        assertFalse(SatlokRepository.isAdminLoggedIn.value)

        // Valid login succeeds
        assertTrue(SatlokRepository.loginAdmin(context, ownerEmail, initialSecret).success)
        assertTrue(SatlokRepository.isAdminLoggedIn.value)

        // Change password
        val changeRes = SatlokRepository.changeAdminPassword(context, initialSecret, rotatedSecret, rotatedSecret)
        assertTrue("Password change should succeed", changeRes.success)

        SatlokRepository.logoutAdmin()
        assertFalse("Old password must no longer work", SatlokRepository.loginAdmin(context, ownerEmail, initialSecret).success)
        assertTrue("New password must work", SatlokRepository.loginAdmin(context, ownerEmail, rotatedSecret).success)
    }

    @Test
    fun testGalleryVideoYouTubeShortsPhotoAndPdfPersistAcrossAppRestart(): Unit = runBlocking {
        val ownerEmail = "owner@satlokwave.com"
        val pass = "Pass#" + UUID.randomUUID().toString().take(8)
        assertTrue(SatlokRepository.setupInitialAdmin(context, ownerEmail, pass, pass).success)

        // 1. Add Gallery Video (local MP4 path)
        val galleryVideoTitle = "गैलरी वीडियो टेस्ट ${System.currentTimeMillis()}"
        val galleryPath = "/data/user/0/com.satlokwave.app/files/uploaded_videos/test_gallery.mp4"
        assertTrue(
            SatlokRepository.addVideo(
                title = galleryVideoTitle,
                description = "MP4 गैलरी अपलोड टेस्ट",
                mediaUrl = galleryPath,
                category = KnowledgeCategory.SATSANG,
                playlist = "Latest",
                duration = "08:45",
                storageType = "LOCAL"
            )
        )

        // 2. Add YouTube Shorts video
        val shortsTitle = "शॉर्ट्स सत्संग क्लिप"
        assertTrue(
            SatlokRepository.addVideo(
                title = shortsTitle,
                description = "YouTube Shorts लिंक टेस्ट",
                mediaUrl = "https://www.youtube.com/shorts/W-rC6bZ6z5I",
                category = KnowledgeCategory.BHAKTI_BODH,
                playlist = "Shorts"
            )
        )

        // 3. Add Draft Video (must be excluded from public search)
        val draftTitle = "गोपनीय ड्राफ्ट वीडियो"
        assertTrue(
            SatlokRepository.addVideo(
                title = draftTitle,
                description = "केवल एडमिन के लिए",
                mediaUrl = "https://www.youtube.com/watch?v=0k5j-eLgX2E",
                category = KnowledgeCategory.SATSANG,
                playlist = "Drafts",
                isDraft = true
            )
        )

        // 4. Update section photo & add album photo
        val sectionPhotoPath = "/data/user/0/com.satlokwave.app/files/section_photos/about_custom.jpg"
        SatlokRepository.updateSectionPhoto("about", sectionPhotoPath)
        val firstAlbumId = SatlokRepository.photoAlbums.value.first().id
        val albumPhotoTitle = "नया आश्रम फोटो"
        assertTrue(SatlokRepository.addPhotoToAlbum(firstAlbumId, albumPhotoTitle, "/data/user/0/files/ashram.jpg", "दर्शन"))

        // 5. Add PDF Document
        val pdfTitle = "सतलोक ज्ञान पुस्तिका PDF"
        assertTrue(
            SatlokRepository.addDocument(
                title = pdfTitle,
                description = "सद्ग्रंथ प्रमाण संग्रह",
                mediaUrl = "/data/user/0/com.satlokwave.app/files/uploaded_docs/gyan.pdf"
            )
        )

        // Ensure all Room writes finish before simulating full app restart
        SatlokRepository.flushWritesForTesting()
        SatlokRepository.logoutAdmin()
        SatlokRepository.resetForTesting(context)

        val persistedGallery = SatlokRepository.videos.value.find { it.title == galleryVideoTitle }
        assertNotNull("Gallery video should persist across restart", persistedGallery)
        assertEquals("", persistedGallery?.youtubeVideoId)
        assertEquals("LOCAL", persistedGallery?.storageType)

        val persistedShorts = SatlokRepository.videos.value.find { it.title == shortsTitle }
        assertNotNull("YouTube Shorts video should persist across restart", persistedShorts)
        assertEquals("W-rC6bZ6z5I", persistedShorts?.youtubeVideoId)

        assertEquals(sectionPhotoPath, SatlokRepository.appConfig.value.aboutSectionPhoto)
        assertTrue(
            "Uploaded album photo should persist across restart",
            SatlokRepository.photoAlbums.value.first { it.id == firstAlbumId }.photos.any { it.title == albumPhotoTitle }
        )
        assertTrue(
            "Uploaded PDF document should persist across restart",
            SatlokRepository.documents.value.any { it.title == pdfTitle }
        )

        // Draft video must NOT appear in public search
        val draftSearch = SatlokRepository.search("गोपनीय ड्राफ्ट")
        assertTrue("Draft videos must remain hidden from public search", draftSearch.isEmpty())
    }

    @Test
    fun testFileCopyWithProgressVerifiesNonEmptyFile(): Unit = runBlocking {
        val sampleFile = File(context.cacheDir, "sample_source.mp4")
        sampleFile.writeBytes(ByteArray(4096) { 7 })
        var lastProgress = 0f

        val result = FileStorageHelper.copyMediaWithProgress(
            context = context,
            uri = Uri.fromFile(sampleFile),
            subfolder = "test_videos",
            prefix = "vid",
            defaultExtension = "mp4",
            extractVideoMetadata = false,
            onProgress = { pct, _ -> lastProgress = pct }
        )

        assertTrue("Copy should succeed", result.isSuccess)
        val saved = result.getOrThrow()
        assertEquals(4096L, saved.sizeBytes)
        assertEquals(1f, lastProgress, 0.01f)
        assertTrue(File(saved.absolutePath).exists())
        sampleFile.delete()
        File(saved.absolutePath).delete()
    }

    @Test
    fun testApkDownloadBannerOpensDialogAndHandlesClicksSafely() {
        composeTestRule.setContent {
            SatlokMainApp()
        }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("home_screen")
            .performScrollToNode(androidx.compose.ui.test.hasTestTag("home_apk_download_banner"))
        composeTestRule.onNodeWithTag("home_apk_download_banner")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("apk_download_dialog").assertIsDisplayed()
        composeTestRule.onNodeWithTag("confirm_download_apk_btn")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitForIdle()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodes(androidx.compose.ui.test.hasTestTag("apk_status_banner"))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("apk_status_banner").performScrollTo().assertIsDisplayed()

        composeTestRule.onNodeWithTag("share_apk_file_btn")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun testTopHeaderApkCategoryOpensDialog() {
        composeTestRule.setContent {
            SatlokMainApp()
        }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("category_strip_APK डाउनलोड")
            .performScrollTo()
            .performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("apk_download_dialog").assertIsDisplayed()
    }
}
