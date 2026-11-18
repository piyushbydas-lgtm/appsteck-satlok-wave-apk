package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.data.model.ContentItem
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SpiritualOrange
import com.example.util.AudioPlaybackManager
import com.example.util.FileStorageHelper
import com.example.util.YouTubeHelper
import kotlinx.coroutines.delay
import java.io.File

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideoPlayerView(
    video: ContentItem,
    onClose: () -> Unit,
    onRelatedVideoClick: (ContentItem) -> Unit = {}
) {
    val context = LocalContext.current
    val youtubeVideoId = remember(video.mediaUrl, video.youtubeVideoId) {
        video.youtubeVideoId.ifBlank { YouTubeHelper.extractVideoId(video.mediaUrl) }
    }
    val isYouTube = youtubeVideoId.isNotEmpty()
    val isShorts = remember(video.mediaUrl) { YouTubeHelper.isShortsUrl(video.mediaUrl) }

    var isFullscreen by remember { mutableStateOf(false) }

    // Pause background radio/audio when video opens
    LaunchedEffect(video.id) {
        if (AudioPlaybackManager.isPlaying.value) {
            AudioPlaybackManager.togglePlayPause()
        }
    }

    BackHandler {
        if (isFullscreen) {
            isFullscreen = false
        } else {
            onClose()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("video_player_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onClose, modifier = Modifier.testTag("close_video_button")) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Video",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = if (isShorts) "सत्संग Shorts प्लेयर" else "सत्संग वीडियो प्लेयर",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                IconButton(
                    onClick = {
                        try {
                            val shareText = if (isYouTube) {
                                "${video.title}\n${video.mediaUrl}\n\nसद्ज्ञान प्राप्त करें 'SATLOK WAVE' ऐप पर।"
                            } else {
                                "${video.title} - ${video.speaker}\n\nसद्ज्ञान प्राप्त करें 'SATLOK WAVE' ऐप पर।"
                            }
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, video.title)
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "वीडियो शेयर करें"))
                        } catch (_: Exception) {}
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Player Surface (YouTube vs Native ExoPlayer Gallery/Cloud Video)
            if (isYouTube) {
                YouTubeInAppPlayer(
                    videoId = youtubeVideoId,
                    originalUrl = video.mediaUrl,
                    isShorts = isShorts,
                    isFullscreen = isFullscreen,
                    onToggleFullscreen = { isFullscreen = !isFullscreen }
                )
            } else if (video.mediaUrl.isNotBlank()) {
                NativeExoVideoPlayer(
                    mediaSource = video.mediaUrl,
                    isFullscreen = isFullscreen,
                    onToggleFullscreen = { isFullscreen = !isFullscreen }
                )
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .testTag("video_player_container"),
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Unavailable",
                                tint = SkyBluePrimary,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "वीडियो स्रोत उपलब्ध नहीं है",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Secondary Metadata & External Fallback Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isYouTube) {
                    OutlinedButton(
                        onClick = {
                            try {
                                val target = video.mediaUrl.ifBlank {
                                    YouTubeHelper.getWatchUrl(youtubeVideoId, isShorts)
                                }
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(target))
                                context.startActivity(intent)
                            } catch (_: Exception) {}
                        },
                        modifier = Modifier.testTag("open_in_youtube_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = "Open in YouTube",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "YouTube ऐप में खोलें", style = MaterialTheme.typography.labelMedium)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (video.mediaUrl.startsWith("/")) "डिवाइस सेव्ड वीडियो (HD प्लेयर)" else "ऑनलाइन वीडियो स्ट्रीम",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Text(
                    text = "अवधि: ${video.duration.ifBlank { "--:--" }}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            // Video Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = video.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = video.playlist,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "${video.viewsCount} दृश्य (Views)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = video.description.ifBlank { "जगतगुरु तत्वदर्शी संत रामपाल जी महाराज के पावन प्रवचन।" },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )
                    )

                    if (!video.scriptureRef.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "सद्ग्रंथ प्रमाण: ${video.scriptureRef}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = SpiritualOrange,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun YouTubeInAppPlayer(
    videoId: String,
    originalUrl: String,
    isShorts: Boolean,
    isFullscreen: Boolean,
    onToggleFullscreen: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember(videoId) { mutableStateOf(true) }
    var playerErrorCode by remember(videoId) { mutableStateOf<String?>(null) }
    var reloadKey by remember(videoId) { mutableIntStateOf(0) }
    var customFullscreenView by remember { mutableStateOf<View?>(null) }
    var customViewCallback by remember { mutableStateOf<WebChromeClient.CustomViewCallback?>(null) }

    val containerAspect = if (isFullscreen) 16f / 9f else if (isShorts) 9f / 14f else 16f / 9f

    val playerContent: @Composable (Modifier) -> Unit = { modifier ->
        Box(
            modifier = modifier
                .background(Color.Black)
                .testTag("video_player_container")
        ) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.mediaPlaybackRequiresUserGesture = false
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.userAgentString =
                            "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Mobile Safari/537.36"

                        addJavascriptInterface(
                            object {
                                @JavascriptInterface
                                fun onPlayerReady() {
                                    post {
                                        isLoading = false
                                        playerErrorCode = null
                                    }
                                }

                                @JavascriptInterface
                                fun onPlayerError(code: Int) {
                                    post {
                                        isLoading = false
                                        playerErrorCode = when (code) {
                                            101, 150, 153 -> "इस वीडियो के मालिक ने ऐप के अंदर एम्बेड (Embed) बंद कर रखा है (Code $code)।"
                                            100 -> "यह YouTube वीडियो हटाया जा चुका है या निजी है।"
                                            2 -> "अमान्य YouTube वीडियो लिंक।"
                                            else -> "YouTube प्लेयर लोड नहीं हो पाया (त्रुटि $code)।"
                                        }
                                    }
                                }
                            },
                            "SatlokBridge"
                        )

                        webChromeClient = object : WebChromeClient() {
                            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                                customFullscreenView = view
                                customViewCallback = callback
                            }

                            override fun onHideCustomView() {
                                customViewCallback?.onCustomViewHidden()
                                customViewCallback = null
                                customFullscreenView = null
                            }
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                isLoading = false
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                if (request?.isForMainFrame == true) {
                                    isLoading = false
                                    playerErrorCode = "नेटवर्क त्रुटि: YouTube वीडियो लोड नहीं हो सका।"
                                }
                            }
                        }

                        loadDataWithBaseURL(
                            "https://www.youtube.com",
                            buildYouTubeEmbedHtml(videoId),
                            "text/html",
                            "utf-8",
                            null
                        )
                    }
                },
                update = { webView ->
                    val currentTag = webView.tag as? String
                    val desiredTag = "${videoId}_$reloadKey"
                    if (currentTag != desiredTag) {
                        webView.tag = desiredTag
                        isLoading = true
                        playerErrorCode = null
                        webView.loadDataWithBaseURL(
                            "https://www.youtube.com",
                            buildYouTubeEmbedHtml(videoId),
                            "text/html",
                            "utf-8",
                            null
                        )
                    }
                },
                onRelease = { webView ->
                    try {
                        customViewCallback?.onCustomViewHidden()
                        webView.stopLoading()
                        webView.loadUrl("about:blank")
                        webView.onPause()
                        webView.destroy()
                    } catch (_: Exception) {}
                },
                modifier = Modifier.fillMaxSize()
            )

            // Fullscreen Toggle Button in Corner
            IconButton(
                onClick = onToggleFullscreen,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.65f))
            ) {
                Icon(
                    imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                    contentDescription = "Fullscreen",
                    tint = Color.White
                )
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = SkyBluePrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("YouTube वीडियो लोड हो रहा है...", color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            if (playerErrorCode != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.88f))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = Color(0xFFFF8A65),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = playerErrorCode.orEmpty(),
                            color = Color.White,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    playerErrorCode = null
                                    reloadKey++
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("पुनः प्रयास")
                            }
                            OutlinedButton(
                                onClick = {
                                    try {
                                        val target = originalUrl.ifBlank { YouTubeHelper.getWatchUrl(videoId, isShorts) }
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(target)))
                                    } catch (_: Exception) {}
                                }
                            ) {
                                Text("YouTube में खोलें", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }

    if (customFullscreenView != null) {
        Dialog(
            onDismissRequest = {
                customViewCallback?.onCustomViewHidden()
                customFullscreenView = null
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = { ctx ->
                        FrameLayout(ctx).apply {
                            val v = customFullscreenView
                            if (v != null) {
                                (v.parent as? ViewGroup)?.removeView(v)
                                addView(v)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    if (isFullscreen) {
        Dialog(
            onDismissRequest = onToggleFullscreen,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                playerContent(Modifier.fillMaxSize())
            }
        }
    } else {
        playerContent(
            Modifier
                .fillMaxWidth()
                .aspectRatio(containerAspect)
        )
    }
}

private fun buildYouTubeEmbedHtml(videoId: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; background-color: #000; }
                html, body, #player { width: 100%; height: 100%; overflow: hidden; }
            </style>
        </head>
        <body>
            <div id="player"></div>
            <script src="https://www.youtube.com/iframe_api"></script>
            <script>
                var player;
                function onYouTubeIframeAPIReady() {
                    player = new YT.Player('player', {
                        width: '100%',
                        height: '100%',
                        videoId: '$videoId',
                        playerVars: {
                            'autoplay': 1,
                            'playsinline': 1,
                            'rel': 0,
                            'modestbranding': 1,
                            'fs': 1
                        },
                        events: {
                            'onReady': function(event) {
                                if (window.SatlokBridge) { SatlokBridge.onPlayerReady(); }
                                try { event.target.playVideo(); } catch(e) {}
                            },
                            'onError': function(event) {
                                if (window.SatlokBridge) { SatlokBridge.onPlayerError(event.data || -1); }
                            }
                        }
                    });
                }
            </script>
        </body>
        </html>
    """.trimIndent()
}

@Composable
private fun NativeExoVideoPlayer(
    mediaSource: String,
    isFullscreen: Boolean,
    onToggleFullscreen: () -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember(mediaSource) { mutableStateOf(false) }
    var isBuffering by remember(mediaSource) { mutableStateOf(true) }
    var durationMs by remember(mediaSource) { mutableLongStateOf(0L) }
    var currentPosMs by remember(mediaSource) { mutableLongStateOf(0L) }
    var volume by remember(mediaSource) { mutableFloatStateOf(1f) }
    var isMuted by remember(mediaSource) { mutableStateOf(false) }
    var errorMessage by remember(mediaSource) { mutableStateOf<String?>(null) }
    var showControls by remember { mutableStateOf(true) }

    val resolvedUri = remember(mediaSource) {
        val trimmed = mediaSource.trim()
        when {
            trimmed.startsWith("/") -> Uri.fromFile(File(trimmed))
            else -> Uri.parse(trimmed)
        }
    }

    val exoPlayer = remember(mediaSource) {
        ExoPlayer.Builder(context).build().apply {
            val fileMissing = mediaSource.startsWith("/") && !File(mediaSource).exists()
            if (fileMissing) {
                errorMessage = "यह वीडियो फ़ाइल डिवाइस स्टोरेज में नहीं मिली।"
                isBuffering = false
            } else {
                setMediaItem(MediaItem.fromUri(resolvedUri))
                prepare()
                playWhenReady = true
            }
        }
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                isBuffering = (playbackState == Player.STATE_BUFFERING)
                if (playbackState == Player.STATE_READY) {
                    durationMs = exoPlayer.duration.coerceAtLeast(0L)
                    errorMessage = null
                } else if (playbackState == Player.STATE_ENDED) {
                    isPlaying = false
                }
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlayerError(error: PlaybackException) {
                isBuffering = false
                isPlaying = false
                errorMessage = "वीडियो चलाने में समस्या: ${error.localizedMessage ?: "फ़ॉर्मेट या नेटवर्क त्रुटि"}"
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    // Periodic progress tracker
    LaunchedEffect(exoPlayer, isPlaying) {
        while (true) {
            currentPosMs = exoPlayer.currentPosition.coerceAtLeast(0L)
            if (exoPlayer.duration > 0) {
                durationMs = exoPlayer.duration
            }
            delay(400L)
        }
    }

    val playerBody: @Composable (Modifier) -> Unit = { modifier ->
        Column(modifier = modifier.background(Color.Black)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = isFullscreen)
                    .then(if (!isFullscreen) Modifier.aspectRatio(16f / 9f) else Modifier)
                    .clickable { showControls = !showControls }
                    .testTag("video_player_container")
            ) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            useController = false
                            player = exoPlayer
                        }
                    },
                    update = { view ->
                        if (view.player !== exoPlayer) {
                            view.player = exoPlayer
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                if (isBuffering && errorMessage == null) {
                    CircularProgressIndicator(
                        color = SkyBluePrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Center Play/Pause overlay when controls visible
                if (showControls && !isBuffering && errorMessage == null) {
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                exoPlayer.seekTo((exoPlayer.currentPosition - 10_000L).coerceAtLeast(0L))
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f))
                        ) {
                            Icon(Icons.Default.Replay10, contentDescription = "Back 10s", tint = Color.White)
                        }

                        IconButton(
                            onClick = {
                                if (exoPlayer.isPlaying) {
                                    exoPlayer.pause()
                                } else {
                                    if (exoPlayer.playbackState == Player.STATE_ENDED) {
                                        exoPlayer.seekTo(0)
                                    }
                                    exoPlayer.play()
                                }
                            },
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(SkyBluePrimary.copy(alpha = 0.9f))
                                .testTag("exo_play_pause_button")
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                val maxDur = exoPlayer.duration.takeIf { it > 0 } ?: Long.MAX_VALUE
                                exoPlayer.seekTo((exoPlayer.currentPosition + 10_000L).coerceAtMost(maxDur))
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.55f))
                        ) {
                            Icon(Icons.Default.Forward10, contentDescription = "Forward 10s", tint = Color.White)
                        }
                    }
                }

                if (errorMessage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.88f))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = Color(0xFFFF8A65),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage.orEmpty(),
                                color = Color.White,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    errorMessage = null
                                    isBuffering = true
                                    exoPlayer.setMediaItem(MediaItem.fromUri(resolvedUri))
                                    exoPlayer.prepare()
                                    exoPlayer.play()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("पुनः प्रयास करें")
                            }
                        }
                    }
                }
            }

            // Custom Control Strip: Seekbar, Time, Volume, Fullscreen
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0A1929))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                val safeDuration = durationMs.coerceAtLeast(1L)
                Slider(
                    value = (currentPosMs.toFloat() / safeDuration.toFloat()).coerceIn(0f, 1f),
                    onValueChange = { fraction ->
                        val target = (fraction * safeDuration).toLong()
                        currentPosMs = target
                        exoPlayer.seekTo(target)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = SkyBluePrimary,
                        activeTrackColor = SkyBluePrimary,
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${FileStorageHelper.formatDurationMs(currentPosMs)} / ${FileStorageHelper.formatDurationMs(durationMs)}",
                        color = Color.White,
                        fontSize = 12.sp
                    )

                    // Volume control + Mute
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    ) {
                        IconButton(
                            onClick = {
                                isMuted = !isMuted
                                exoPlayer.volume = if (isMuted) 0f else volume
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (isMuted || volume == 0f) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Mute Toggle",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Slider(
                            value = if (isMuted) 0f else volume,
                            onValueChange = { newVol ->
                                volume = newVol
                                isMuted = newVol <= 0.01f
                                exoPlayer.volume = newVol
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = Color(0xFF4FC3F7),
                                inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    IconButton(
                        onClick = onToggleFullscreen,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("exo_fullscreen_button")
                    ) {
                        Icon(
                            imageVector = if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                            contentDescription = "Fullscreen Toggle",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }

    if (isFullscreen) {
        Dialog(
            onDismissRequest = onToggleFullscreen,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            playerBody(Modifier.fillMaxSize())
        }
    } else {
        playerBody(Modifier.fillMaxWidth())
    }
}
