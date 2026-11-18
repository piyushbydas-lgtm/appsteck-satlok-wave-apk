package com.example.util

import android.media.AudioAttributes
import android.media.MediaPlayer
import com.example.data.model.ContentItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class PlaybackState(
    val currentTrack: ContentItem? = null,
    val isPlaying: Boolean = false,
    val currentPositionSeconds: Int = 0,
    val totalDurationSeconds: Int = 180,
    val playlist: List<ContentItem> = emptyList(),
    val currentIndex: Int = 0,
    val playbackSpeed: Float = 1.0f
)

object AudioPlaybackManager {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var tickerJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null
    @Volatile
    private var isPrepared: Boolean = false

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _currentAudio = MutableStateFlow<ContentItem?>(null)
    val currentAudio: StateFlow<ContentItem?> = _currentAudio.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    fun play(
        title: String,
        speaker: String = "संत रामपाल जी महाराज",
        audioUrl: String,
        id: String = "audio_${System.currentTimeMillis()}"
    ) {
        val item = ContentItem(
            id = id,
            title = title,
            description = speaker,
            type = com.example.data.model.MediaType.AUDIO,
            category = com.example.data.model.KnowledgeCategory.SPIRITUAL_MUSIC,
            mediaUrl = audioUrl,
            duration = "10:00",
            speaker = speaker
        )
        playTrack(item)
    }

    fun playTrack(track: ContentItem, queue: List<ContentItem> = listOf(track)) {
        val index = queue.indexOfFirst { it.id == track.id }.let { if (it == -1) 0 else it }
        val durationSec = parseDurationToSeconds(track.duration)

        releasePlayer()

        _playbackState.value = _playbackState.value.copy(
            currentTrack = track,
            isPlaying = true,
            currentPositionSeconds = 0,
            totalDurationSeconds = if (durationSec > 0) durationSec else 300,
            playlist = if (queue.isNotEmpty()) queue else listOf(track),
            currentIndex = index
        )
        _currentAudio.value = track
        _isPlaying.value = true

        val url = track.mediaUrl.trim()
        val isPlayableSource = url.startsWith("http://", ignoreCase = true) ||
            url.startsWith("https://", ignoreCase = true) ||
            url.startsWith("/") ||
            url.startsWith("file://", ignoreCase = true)
        if (isPlayableSource) {
            try {
                isPrepared = false
                val player = MediaPlayer()
                mediaPlayer = player

                player.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                player.setDataSource(url)

                player.setOnPreparedListener { mp ->
                    if (mediaPlayer === mp) {
                        isPrepared = true
                        val durMs = try { mp.duration } catch (_: Exception) { 0 }
                        if (durMs > 0) {
                            _playbackState.value = _playbackState.value.copy(
                                totalDurationSeconds = durMs / 1000
                            )
                        }
                        if (_playbackState.value.isPlaying) {
                            try {
                                mp.start()
                            } catch (_: Exception) {}
                        }
                    }
                }

                player.setOnCompletionListener { mp ->
                    if (mediaPlayer === mp) {
                        playNext()
                    }
                }

                player.setOnErrorListener { mp, what, extra ->
                    // Log error safely and return true so unhandled error callbacks don't fire
                    isPrepared = false
                    try {
                        mp.reset()
                    } catch (_: Exception) {}
                    true
                }

                player.prepareAsync()
            } catch (e: Throwable) {
                isPrepared = false
                mediaPlayer = null
            }
        }

        startTicker()
    }

    fun togglePlayPause() {
        val current = _playbackState.value
        if (current.currentTrack == null) return
        val newState = !current.isPlaying
        _playbackState.value = current.copy(isPlaying = newState)
        _isPlaying.value = newState

        try {
            val mp = mediaPlayer
            if (mp != null && isPrepared) {
                if (newState) {
                    if (!mp.isPlaying) {
                        mp.start()
                    }
                    startTicker()
                } else {
                    if (mp.isPlaying) {
                        mp.pause()
                    }
                    tickerJob?.cancel()
                }
            } else {
                if (newState) startTicker() else tickerJob?.cancel()
            }
        } catch (e: Exception) {
            if (newState) startTicker() else tickerJob?.cancel()
        }
    }

    fun seekTo(seconds: Int) {
        val current = _playbackState.value
        val clamped = seconds.coerceIn(0, current.totalDurationSeconds)
        _playbackState.value = current.copy(currentPositionSeconds = clamped)

        try {
            val mp = mediaPlayer
            if (mp != null && isPrepared) {
                mp.seekTo(clamped * 1000)
            }
        } catch (_: Exception) {}
    }

    fun playNext() {
        val state = _playbackState.value
        if (state.playlist.isEmpty()) return
        val nextIndex = (state.currentIndex + 1) % state.playlist.size
        val nextTrack = state.playlist[nextIndex]
        playTrack(nextTrack, state.playlist)
    }

    fun playPrevious() {
        val state = _playbackState.value
        if (state.playlist.isEmpty()) return
        val prevIndex = if (state.currentIndex - 1 < 0) state.playlist.size - 1 else state.currentIndex - 1
        val prevTrack = state.playlist[prevIndex]
        playTrack(prevTrack, state.playlist)
    }

    fun setSpeed(speed: Float) {
        _playbackState.value = _playbackState.value.copy(playbackSpeed = speed)
        try {
            val mp = mediaPlayer
            if (mp != null && isPrepared) {
                mp.playbackParams = mp.playbackParams.setSpeed(speed)
            }
        } catch (_: Exception) {}
    }

    fun stopPlayback() {
        releasePlayer()
        _playbackState.value = PlaybackState()
        _currentAudio.value = null
        _isPlaying.value = false
    }

    private fun releasePlayer() {
        tickerJob?.cancel()
        val player = mediaPlayer ?: return
        mediaPlayer = null
        isPrepared = false

        try {
            // Remove all listener references to prevent callbacks during disposal
            player.setOnPreparedListener(null)
            player.setOnCompletionListener(null)
            player.setOnErrorListener(null)

            // Never call stop() directly when state may be Paused/Preparing (state 4)
            // reset() is valid from ANY state and transitions safely to Idle
            player.reset()
            player.release()
        } catch (_: Exception) {}
    }

    private fun startTicker() {
        tickerJob?.cancel()
        tickerJob = scope.launch {
            while (isActive) {
                delay(1000)
                val current = _playbackState.value
                if (current.isPlaying) {
                    val mpPos = try {
                        val mp = mediaPlayer
                        if (mp != null && isPrepared && mp.isPlaying) {
                            mp.currentPosition / 1000
                        } else null
                    } catch (_: Exception) {
                        null
                    }

                    val nextPos = mpPos ?: (current.currentPositionSeconds + 1)
                    if (nextPos >= current.totalDurationSeconds) {
                        playNext()
                    } else {
                        _playbackState.value = current.copy(currentPositionSeconds = nextPos)
                    }
                }
            }
        }
    }

    private fun parseDurationToSeconds(duration: String): Int {
        val parts = duration.split(":")
        return when (parts.size) {
            2 -> (parts[0].toIntOrNull() ?: 0) * 60 + (parts[1].toIntOrNull() ?: 0)
            3 -> (parts[0].toIntOrNull() ?: 0) * 3600 + (parts[1].toIntOrNull() ?: 0) * 60 + (parts[2].toIntOrNull() ?: 0)
            else -> 180
        }
    }

    fun formatTime(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return "%02d:%02d".format(mins, secs)
    }
}
