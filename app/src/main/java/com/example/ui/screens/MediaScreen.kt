package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.ContentItem
import com.example.data.model.PhotoAlbum
import com.example.data.model.PhotoItem
import com.example.data.repository.SatlokRepository
import com.example.ui.theme.DarkNavyHeading
import com.example.ui.theme.SpiritualOrange
import com.example.util.AudioPlaybackManager

enum class MediaSection {
    HUB,
    VIDEOS,
    AUDIOS,
    PHOTOS,
    DOCUMENTS,
    BLOG
}

@Composable
fun MediaScreen(
    onPlayVideo: (ContentItem) -> Unit,
    onViewPhoto: (PhotoAlbum, Int) -> Unit,
    onOpenDocument: (ContentItem) -> Unit = {}
) {
    var currentSubSection by remember { mutableStateOf(MediaSection.HUB) }

    BackHandler(enabled = currentSubSection != MediaSection.HUB) {
        currentSubSection = MediaSection.HUB
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA))
            .testTag("media_screen")
    ) {
        // Curved Orange/Red Top Bar matching Screenshot 3
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF29B6F6), // Sky Blue (आसमानी)
                                Color(0xFF0288D1),
                                Color(0xFF0277BD),
                                Color(0xFF01579B)  // Royal Ocean Blue (नीला)
                            )
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentSubSection != MediaSection.HUB) {
                        IconButton(
                            onClick = { currentSubSection = MediaSection.HUB },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Text(
                        text = when (currentSubSection) {
                            MediaSection.HUB -> "मीडिया"
                            MediaSection.VIDEOS -> "वीडियो सत्संग"
                            MediaSection.AUDIOS -> "ऑडियो सत्संग व अमृतवाणी"
                            MediaSection.PHOTOS -> "फोटो गैलरी एवं वॉलपेपर्स"
                            MediaSection.DOCUMENTS -> "दस्तावेज़ एवं PDF पुस्तकें"
                            MediaSection.BLOG -> "आध्यात्मिक ज्ञान ब्लॉग"
                        },
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp
                        ),
                        modifier = Modifier.padding(start = if (currentSubSection == MediaSection.HUB) 12.dp else 0.dp)
                    )
                }
            }
        }

        when (currentSubSection) {
            MediaSection.HUB -> {
                MediaHubView(
                    onSelectSection = { currentSubSection = it }
                )
            }
            MediaSection.VIDEOS -> {
                VideoTabContent(onPlayVideo = onPlayVideo)
            }
            MediaSection.AUDIOS -> {
                AudioTabContent()
            }
            MediaSection.PHOTOS -> {
                PhotoTabContent(onViewPhoto = onViewPhoto)
            }
            MediaSection.DOCUMENTS -> {
                DocumentTabContent(onOpenDocument = onOpenDocument)
            }
            MediaSection.BLOG -> {
                BlogTabContent()
            }
        }
    }
}

@Composable
private fun MediaHubView(
    onSelectSection: (MediaSection) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Card 1: वीडियो (Screenshot 3)
        item {
            MediaHubCard(
                title = "वीडियो",
                subtitle = "देखें सत्संग, ज्ञान चर्चा, शब्द और भक्ति बोध",
                accentColor = Color(0xFFE53935),
                imageContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF212121))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_satlok_hero_banner),
                            contentDescription = "Videos",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.35f))
                        )
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.Center)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.75f))
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                },
                onClick = { onSelectSection(MediaSection.VIDEOS) }
            )
        }

        // Card 2: ऑडियो (Screenshot 3)
        item {
            MediaHubCard(
                title = "ऑडियो",
                subtitle = "सुने सत्संग, ज्ञान चर्चा, शब्द और भक्ति बोध",
                accentColor = Color(0xFF1E88E5),
                imageContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF90CAF9), Color(0xFF1565C0))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Turntable / Vinyl record stylized graphic
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF111111))
                                    .border(3.dp, Color(0xFF424242), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0288D1)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Audiotrack,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Image(
                                painter = painterResource(id = R.drawable.satlok_wave_logo),
                                contentDescription = "Sant Rampal Ji Maharaj Audio",
                                modifier = Modifier
                                    .size(84.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                },
                onClick = { onSelectSection(MediaSection.AUDIOS) }
            )
        }

        // Card 3: फोटो गैलरी (Screenshot 3)
        item {
            MediaHubCard(
                title = "फोटो गैलरी",
                subtitle = "यहाँ मोबाईल वॉलपेपर्स, आध्यात्मिक ज्ञान व प्रमाण के फोटोज पाएँ",
                accentColor = Color(0xFF0288D1),
                imageContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF4FC3F7), Color(0xFF0288D1), Color(0xFF01579B))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.satlok_wave_logo),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(85.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.5.dp, Color.White, RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Image(
                                painter = painterResource(id = R.drawable.img_all_religions),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(85.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.5.dp, Color.White, RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Image(
                                painter = painterResource(id = R.drawable.img_holy_books),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(85.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.5.dp, Color.White, RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                },
                onClick = { onSelectSection(MediaSection.PHOTOS) }
            )
        }

        // Card 4: दस्तावेज़ व PDF
        item {
            MediaHubCard(
                title = "दस्तावेज़ व PDF",
                subtitle = "प्रकाशन, प्रमाण पत्र एवं आध्यात्मिक PDF पुस्तकें पढ़ें",
                accentColor = Color(0xFF0288D1),
                imageContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE1F5FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                tint = Color(0xFF0277BD),
                                modifier = Modifier.size(38.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "PDF / ग्रंथ",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF01579B)
                                )
                            )
                        }
                    }
                },
                onClick = { onSelectSection(MediaSection.DOCUMENTS) }
            )
        }

        // Card 5: ब्लॉग (Screenshot 3)
        item {
            MediaHubCard(
                title = "ब्लॉग",
                subtitle = "ब्लॉग पर जाएँ",
                accentColor = Color(0xFF0277BD),
                imageContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE1F5FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = Color(0xFF0277BD),
                                modifier = Modifier.size(38.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "blog",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF01579B),
                                    fontSize = 32.sp
                                )
                            )
                        }
                    }
                },
                onClick = { onSelectSection(MediaSection.BLOG) }
            )
        }
    }
}

@Composable
private fun MediaHubCard(
    title: String,
    subtitle: String,
    accentColor: Color,
    imageContent: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("media_card_$title"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.5.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            imageContent()
        }
    }
}

@Composable
private fun VideoTabContent(onPlayVideo: (ContentItem) -> Unit) {
    val allVideos by SatlokRepository.videos.collectAsState()
    val isLoading by SatlokRepository.isLoadingContent.collectAsState()
    val videos = allVideos.filter { !it.isDraft }
    var selectedFilter by remember { mutableStateOf("सभी") }
    val categories = listOf("सभी", "सत्संग", "ज्ञान चर्चा", "शब्द", "भक्ति बोध")

    val filteredVideos = if (selectedFilter == "सभी") {
        videos
    } else {
        videos.filter {
            when (selectedFilter) {
                "सत्संग" -> it.category == com.example.data.model.KnowledgeCategory.SATSANG
                "ज्ञान चर्चा" -> it.category == com.example.data.model.KnowledgeCategory.SHASTRARTH
                "शब्द" -> it.category == com.example.data.model.KnowledgeCategory.SPIRITUAL_MUSIC
                "भक्ति बोध" -> it.category == com.example.data.model.KnowledgeCategory.BHAKTI_BODH
                else -> true
            }
        }
    }

    if (isLoading && videos.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = SpiritualOrange)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = selectedFilter == cat,
                        onClick = { selectedFilter = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SpiritualOrange,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        items(filteredVideos, key = { it.id }) { video ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPlayVideo(video) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp, 68.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        if (video.thumbnailUrl.isNotBlank()) {
                            AsyncImage(
                                model = if (video.thumbnailUrl.startsWith("/")) java.io.File(video.thumbnailUrl) else video.thumbnailUrl,
                                contentDescription = video.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.img_satlok_hero_banner),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.PlayCircleFilled,
                            contentDescription = "Play",
                            tint = SpiritualOrange,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.title,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = video.speaker,
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = when (video.category) {
                                com.example.data.model.KnowledgeCategory.SATSANG -> "सत्संग"
                                com.example.data.model.KnowledgeCategory.SHASTRARTH -> "ज्ञान चर्चा"
                                com.example.data.model.KnowledgeCategory.BHAKTI_BODH -> "भक्ति बोध"
                                com.example.data.model.KnowledgeCategory.SRISHTI_RACHNA -> "सृष्टि रचना"
                                else -> "आध्यात्मिक ज्ञान"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SpiritualOrange,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AudioTabContent() {
    val allAudios by SatlokRepository.audios.collectAsState()
    val audios = allAudios.filter { !it.isDraft }
    val currentlyPlaying by AudioPlaybackManager.currentAudio.collectAsState()
    val isPlaying by AudioPlaybackManager.isPlaying.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(audios, key = { it.id }) { audio ->
            val isCurrent = currentlyPlaying?.id == audio.id

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        AudioPlaybackManager.play(
                            title = audio.title,
                            speaker = audio.speaker,
                            audioUrl = audio.mediaUrl,
                            id = audio.id
                        )
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrent) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isCurrent) SpiritualOrange else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isCurrent && isPlaying) Icons.Default.Audiotrack else Icons.Default.PlayArrow,
                            contentDescription = "Audio Play",
                            tint = if (isCurrent) Color.White else MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = audio.title,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = audio.speaker,
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }

                    Text(
                        text = audio.duration,
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.outline)
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotoTabContent(
    onViewPhoto: (PhotoAlbum, Int) -> Unit
) {
    val photoAlbums by SatlokRepository.photoAlbums.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(photoAlbums, key = { it.id }) { album ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = album.titleHindi,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = album.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    userScrollEnabled = false
                ) {
                    items(album.photos.take(6)) { photo ->
                        val index = album.photos.indexOf(photo)
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onViewPhoto(album, index) }
                        ) {
                            if (photo.url.isNotBlank()) {
                                AsyncImage(
                                    model = if (photo.url.startsWith("/")) java.io.File(photo.url) else photo.url,
                                    contentDescription = photo.caption,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.img_satlok_icon),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DocumentTabContent(onOpenDocument: (ContentItem) -> Unit) {
    val allDocs by SatlokRepository.documents.collectAsState()
    val docs = allDocs.filter { !it.isDraft }

    if (docs.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.PictureAsPdf,
                    contentDescription = null,
                    tint = SpiritualOrange.copy(alpha = 0.6f),
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "अभी कोई PDF दस्तावेज़ प्रकाशित नहीं हुआ है",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "एडमिन द्वारा अपलोड की गई PDF पुस्तकें व दस्तावेज़ यहाँ सभी के लिए उपलब्ध होंगे।",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(docs, key = { it.id }) { doc ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenDocument(doc) }
                    .testTag("public_doc_${doc.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE1F5FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = "PDF",
                            tint = Color(0xFF0277BD),
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = doc.title,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (doc.description.isNotBlank()) {
                            Text(
                                text = doc.description,
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Text(
                            text = "खोलने के लिए टैप करें (PDF)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SpiritualOrange,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BlogTabContent() {
    val context = LocalContext.current
    val blogPosts = listOf(
        Pair("सृष्टि रचना का वास्तविक रहस्य क्या है?", "वेदों और पवित्र शास्त्रों के अनुसार सर्वशक्तिमान परमात्मा कबीर साहेब जी ने सर्व ब्रह्मांडों की रचना किस प्रकार की।"),
        Pair("मनुष्य जन्म का मूल उद्देश्य", "84 लाख योनियों के चक्र से मुक्ति पाकर अमर लोक सतलोक में स्थाई निवास प्राप्त करना ही मानव जीवन का परम कर्तव्य है।"),
        Pair("नशा मुक्ति एवं सामाजिक क्रांति", "जगतगुरु तत्वदर्शी संत रामपाल जी महाराज के सानिध्य में लाखों परिवार नशा मुक्त और दहेज मुक्त समाज का निर्माण कर रहे हैं।"),
        Pair("श्रीमद्भगवद्गीता जी में पूर्ण परमात्मा का प्रमाण", "गीता अध्याय 18 श्लोक 62 और 66 में जिस परमेश्वर की शरण में जाने को कहा है, वह पूर्णब्रह्म कौन हैं?")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(blogPosts) { (title, snippet) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.jagatgururampalji.org/articles"))
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = snippet,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "विस्तार से पढ़ें →",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = SpiritualOrange,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
