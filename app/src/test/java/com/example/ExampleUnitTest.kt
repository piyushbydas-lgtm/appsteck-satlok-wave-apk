package com.example

import com.example.data.model.KnowledgeCategory
import com.example.data.model.MediaType
import com.example.data.repository.SatlokRepository
import com.example.util.AppLanguage
import com.example.util.LanguageManager
import com.example.util.YouTubeHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testInitialSpiritualDataLoaded() {
        val videos = SatlokRepository.videos.value
        val audios = SatlokRepository.audios.value
        val sections = SatlokRepository.srishtiRachnaSections.value
        val scriptures = SatlokRepository.scriptureEvidences.value
        val books = SatlokRepository.audioBooks.value

        assertTrue("Videos should not be empty", videos.isNotEmpty())
        assertTrue("Audios should not be empty", audios.isNotEmpty())
        assertTrue("Srishti Rachna sections should be present", sections.isNotEmpty())
        assertTrue("Scripture evidences should be present", scriptures.isNotEmpty())
        assertTrue("Audio books should be present", books.isNotEmpty())
    }

    @Test
    fun testYouTubeUrlAndShortsExtractionAndLocalPathRejection() {
        assertEquals("0k5j-eLgX2E", YouTubeHelper.extractVideoId("https://www.youtube.com/watch?v=0k5j-eLgX2E"))
        assertEquals("0k5j-eLgX2E", YouTubeHelper.extractVideoId("https://youtu.be/0k5j-eLgX2E?si=abc123"))
        assertEquals("W-rC6bZ6z5I", YouTubeHelper.extractVideoId("https://www.youtube.com/shorts/W-rC6bZ6z5I?feature=share"))
        assertEquals("kYQG2L0hGZs", YouTubeHelper.extractVideoId("https://m.youtube.com/live/kYQG2L0hGZs"))
        assertEquals("4yJc9q-2a7w", YouTubeHelper.extractVideoId("https://www.youtube.com/embed/4yJc9q-2a7w"))

        assertTrue(YouTubeHelper.isShortsUrl("https://youtube.com/shorts/W-rC6bZ6z5I"))
        assertFalse(YouTubeHelper.isShortsUrl("https://youtube.com/watch?v=0k5j-eLgX2E"))

        // Critical regression checks: local gallery files & direct MP4 URLs MUST NOT be treated as YouTube IDs
        assertEquals("", YouTubeHelper.extractVideoId("/data/user/0/com.satlokwave.app/files/uploaded_videos/vid_123.mp4"))
        assertEquals("", YouTubeHelper.extractVideoId("file:///storage/emulated/0/Movies/satsang.mp4"))
        assertEquals("", YouTubeHelper.extractVideoId("content://media/external/video/media/1024"))
        assertEquals("", YouTubeHelper.extractVideoId("https://res.cloudinary.com/demo/video/upload/satsang.mp4"))
        assertFalse(YouTubeHelper.isYouTubeUrl("/data/user/0/com.satlokwave.app/files/uploaded_videos/vid_123.mp4"))
    }

    @Test
    fun testUnauthenticatedAddBlocked() {
        SatlokRepository.logoutAdmin()
        val addAttempt = SatlokRepository.addVideo(
            title = "Unauthorized Video",
            description = "Should fail",
            mediaUrl = "https://youtube.com/watch?v=0k5j-eLgX2E",
            category = KnowledgeCategory.SATSANG,
            playlist = "General"
        )
        assertFalse("Unauthenticated user must not be able to add video", addAttempt)
    }

    @Test
    fun testSearchFunctionality() {
        val kabirResults = SatlokRepository.search("कबीर")
        assertTrue("Search for 'कबीर' should return results", kabirResults.isNotEmpty())

        val vedaResults = SatlokRepository.search("ऋग्वेद")
        assertTrue("Search for 'ऋग्वेद' should return scriptural results", vedaResults.isNotEmpty())

        val filteredResults = SatlokRepository.search("सत्संग", MediaType.VIDEO)
        assertTrue(filteredResults.all { it.type == MediaType.VIDEO && !it.isDraft })
    }

    @Test
    fun testLanguageManager() {
        LanguageManager.setLanguage(AppLanguage.HINDI)
        assertEquals("होम", LanguageManager.get("होम", "Home"))

        LanguageManager.setLanguage(AppLanguage.ENGLISH)
        assertEquals("Home", LanguageManager.get("होम", "Home"))

        LanguageManager.setLanguage(AppLanguage.HINDI)
    }

    @Test
    fun testAudioPlaybackManager() {
        val testTitle = "सत्संग अमृतवाणी"
        com.example.util.AudioPlaybackManager.play(
            title = testTitle,
            speaker = "संत रामपाल जी महाराज",
            audioUrl = "https://example.com/audio.mp3"
        )

        val state = com.example.util.AudioPlaybackManager.playbackState.value
        assertNotNull(state.currentTrack)
        assertEquals(testTitle, state.currentTrack?.title)
        assertTrue(state.isPlaying)

        assertEquals("03:00", com.example.util.AudioPlaybackManager.formatTime(180))
        assertEquals("00:45", com.example.util.AudioPlaybackManager.formatTime(45))

        com.example.util.AudioPlaybackManager.stopPlayback()
        val stoppedState = com.example.util.AudioPlaybackManager.playbackState.value
        assertFalse(stoppedState.isPlaying)
        assertEquals(null, stoppedState.currentTrack)
    }
}
