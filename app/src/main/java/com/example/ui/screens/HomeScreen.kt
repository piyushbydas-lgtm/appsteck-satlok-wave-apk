package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.BhaktiBodhTopic
import com.example.data.model.ContentItem
import com.example.data.repository.SatlokRepository
import com.example.ui.theme.AashmaniBlue
import com.example.ui.theme.AashmaniBlueLight
import com.example.ui.theme.DarkNavyHeading
import com.example.ui.theme.DeepRoyalBlue
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.SkyBluePrimary
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onNavigateToAbout: () -> Unit,
    onNavigateToSrishtiRachna: () -> Unit,
    onNavigateToScriptureEvidence: () -> Unit,
    onNavigateToGranthSaar: () -> Unit,
    onNavigateToAllReligions: () -> Unit,
    onNavigateToBhaktiBodh: (BhaktiBodhTopic) -> Unit,
    onNavigateToNaamDiksha: () -> Unit,
    onNavigateToShorts: () -> Unit,
    onNavigateToShastrarth: () -> Unit,
    onOpenFreeBookDialog: () -> Unit,
    onOpenMalaCounter: () -> Unit,
    onOpenApkDownloadDialog: () -> Unit,
    onPlayVideo: (ContentItem) -> Unit
) {
    val videos by SatlokRepository.videos.collectAsState()
    val publishedVideos = videos.filter { !it.isDraft }
    val featuredVideos = publishedVideos.filter { it.isFeatured }.ifEmpty { publishedVideos.take(4) }
    val bhaktiBodhTopics by SatlokRepository.bhaktiBodhTopics.collectAsState()
    val appConfig by SatlokRepository.appConfig.collectAsState()
    val context = LocalContext.current

    val pagerState = rememberPagerState(pageCount = { featuredVideos.size })

    LaunchedEffect(pagerState, featuredVideos.size) {
        if (featuredVideos.size > 1) {
            while (true) {
                delay(4000)
                val nextPage = (pagerState.currentPage + 1) % featuredVideos.size
                pagerState.animateScrollToPage(nextPage, animationSpec = tween(600))
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // =========================================================================
        // SECTION A: 4 MAIN KNOWLEDGE CARDS IN 2-COLUMN GRID (Sky Blue & Blue)
        // With Sant Rampal Ji Maharaj's photo & Divine Sky Blue Theme
        // =========================================================================
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Top Row: हमारे बारे में & सृष्टि रचना
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Card 1: हमारे बारे में
                    HomePhotoCard(
                        title = "हमारे बारे में",
                        imageRes = R.drawable.satlok_wave_logo,
                        customPhoto = appConfig.aboutSectionPhoto,
                        modifier = Modifier.weight(1f),
                        testTag = "home_card_about",
                        onClick = onNavigateToAbout
                    )

                    // Card 2: सृष्टि रचना
                    HomePhotoCard(
                        title = "सृष्टि रचना",
                        imageRes = R.drawable.img_srishti_rachna,
                        customPhoto = appConfig.srishtiRachnaPhoto,
                        modifier = Modifier.weight(1f),
                        testTag = "home_card_srishti_rachna",
                        onClick = onNavigateToSrishtiRachna
                    )
                }

                // Bottom Row: सभी धर्मों में कबीर परमात्मा & पवित्र ग्रंथों का सार
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Card 3: सभी धर्मों में कबीर परमात्मा
                    HomePhotoCard(
                        title = "सभी धर्मों में कबीर परमात्मा",
                        imageRes = R.drawable.img_all_religions,
                        customPhoto = appConfig.allReligionsPhoto,
                        modifier = Modifier.weight(1f),
                        testTag = "home_card_all_religions",
                        onClick = onNavigateToAllReligions
                    )

                    // Card 4: पवित्र ग्रंथों का सार
                    HomePhotoCard(
                        title = "पवित्र ग्रंथों का सार",
                        imageRes = R.drawable.img_holy_books,
                        customPhoto = appConfig.granthSaarPhoto,
                        modifier = Modifier.weight(1f),
                        testTag = "home_card_granth_saar",
                        onClick = onNavigateToGranthSaar
                    )
                }
            }
        }

        // =========================================================================
        // SECTION B: TWO GRADIENT ACTION BUTTONS (Sky Blue & Ocean Blue)
        // Left: "फ्री पुस्तक मंगाए" | Right: "नाम दीक्षा जानकारी"
        // =========================================================================
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Left Button: फ्री पुस्तक मंगाए (Sky Blue Gradient)
                GradientActionButton(
                    text = "फ्री पुस्तक मंगाए",
                    gradientColors = listOf(Color(0xFF039BE5), Color(0xFF0288D1), Color(0xFF01579B)),
                    modifier = Modifier.weight(1f),
                    testTag = "action_btn_free_book",
                    onClick = onOpenFreeBookDialog
                )

                // Right Button: नाम दीक्षा जानकारी (Ocean Blue Gradient)
                GradientActionButton(
                    text = "नाम दीक्षा जानकारी",
                    gradientColors = listOf(Color(0xFF29B6F6), Color(0xFF0288D1), Color(0xFF0277BD)),
                    modifier = Modifier.weight(1f),
                    testTag = "action_btn_naam_diksha",
                    onClick = onNavigateToNaamDiksha
                )
            }
        }

        // =========================================================================
        // SECTION C: DIRECT APK DOWNLOAD PROMOTION BANNER (आधिकारिक APK डाउनलोड)
        // =========================================================================
        item {
            Card(
                onClick = onOpenApkDownloadDialog,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("home_apk_download_banner"),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF0288D1), // Sky Blue
                                    Color(0xFF0277BD),
                                    Color(0xFF01579B)  // Deep Royal Blue
                                )
                            )
                        )
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Android,
                                    contentDescription = null,
                                    tint = OceanBlue,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "SATLOK WAVE आधिकारिक APK",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = "सीधे ऐप में डाउनलोड व इंस्टॉल करें (v1.0)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFFE1F5FE),
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null,
                                    tint = OceanBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "डाउनलोड",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = OceanBlue
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // SECTION D: CIRCULAR FIGURES ROW (Sky Blue / Ocean Blue Halo)
        // With Sant Rampal Ji Maharaj's portrait & Scriptural Evidence
        // =========================================================================
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val spiritualFigures = listOf(
                        Pair("प्रमुख हिंदू देवी-देवता", "ब्रह्मा, विष्णु, शिव, दुर्गा जी"),
                        Pair("प्रमुख हिंदू महापुरुष", "संत गरीबदास जी, संत घीसादास जी"),
                        Pair("प्रमुख मुस्लिम महापुरुष", "हजरत मुहम्मद, आदम, मूसा, इब्राहीम"),
                        Pair("प्रमुख सिख महापुरुष", "श्री गुरु नानक देव जी महाराज"),
                        Pair("प्रमुख ईसाई महापुरुष", "ईसा मसीह (जीसस क्राइस्ट)")
                    )

                    items(spiritualFigures) { (title, subtitle) ->
                        CircularFigureItem(
                            title = title,
                            subtitle = subtitle,
                            onClick = onNavigateToScriptureEvidence
                        )
                    }
                }
            }
        }

        // =========================================================================
        // SECTION E: TOP HERO BANNER / VIDEO SLIDER
        // =========================================================================
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.5.dp, Color(0xFFB3E5FC), RoundedCornerShape(16.dp))
                    .testTag("home_hero_banner")
            ) {
                if (featuredVideos.isNotEmpty()) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val video = featuredVideos.getOrNull(page) ?: return@HorizontalPager
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { onPlayVideo(video) }
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

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.4f),
                                                Color.Black.copy(alpha = 0.85f)
                                            )
                                        )
                                    )
                            )

                            // Play button overlay in Sky Blue
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.Center)
                                    .clip(CircleShape)
                                    .background(AashmaniBlue)
                                    .border(2.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = video.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${video.speaker} • ${video.duration}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFE1F5FE))
                                )
                            }
                        }
                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.img_satlok_hero_banner),
                        contentDescription = "SATLOK WAVE",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // =========================================================================
        // SECTION F: पुस्तकें (BOOKS HORIZONTAL DISPLAY)
        // Realistic book covers with Sant Rampal Ji Maharaj's photo & authentic titles
        // =========================================================================
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "पुस्तकें (Sant Rampal Ji Maharaj Books)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = OceanBlue,
                            fontSize = 19.sp
                        )
                    )

                    Text(
                        text = "सभी निःशुल्क",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = AashmaniBlue,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val books = listOf(
                        Triple(
                            "जीने की राह",
                            "सर्व मानव समाज के कल्याणकारी नियम",
                            listOf(Color(0xFF0277BD), Color(0xFF01579B))
                        ),
                        Triple(
                            "ज्ञान गंगा",
                            "सत्य भक्ति व मोक्ष मार्ग का गूढ़ रहस्य",
                            listOf(Color(0xFF00838F), Color(0xFF006064))
                        ),
                        Triple(
                            "गीता तेरा ज्ञान अमृत",
                            "श्रीमद्भगवद्गीता का यथार्थ भावार्थ",
                            listOf(Color(0xFF283593), Color(0xFF1A237E))
                        ),
                        Triple(
                            "मुसलमान नहीं समझे ज्ञान कुरआन",
                            "पवित्र कुरआन में कबीर प्रभु का प्रमाण",
                            listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))
                        ),
                        Triple(
                            "हिंदू साहेबान! तुम कैसे...",
                            "वेद, गीता व पुराणों में ईश्वर का प्रमाण",
                            listOf(Color(0xFF37474F), Color(0xFF263238))
                        ),
                        Triple(
                            "भक्ति से भगवान तक",
                            "परम शांति एवं पूर्ण मोक्ष की ओर यात्रा",
                            listOf(Color(0xFF0288D1), Color(0xFF01579B))
                        )
                    )

                    items(books) { (title, subtitle, colors) ->
                        BookCard3D(
                            title = title,
                            subtitle = subtitle,
                            coverColors = colors,
                            onClick = onOpenFreeBookDialog
                        )
                    }
                }
            }
        }

        // =========================================================================
        // SECTION G: आध्यात्मिक ज्ञान चर्चा (VS BANNER CARD) (Sky Blue & Navy)
        // =========================================================================
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "आध्यात्मिक ज्ञान चर्चा",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = OceanBlue,
                        fontSize = 19.sp
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToShastrarth() }
                        .testTag("home_shastrarth_vs_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFFFFFFFF),
                                        Color(0xFFE1F5FE),
                                        Color(0xFFB3E5FC)
                                    )
                                )
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left: Sant Rampal Ji Maharaj Portrait with Blue Border
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .border(2.5.dp, AashmaniBlue, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (!appConfig.shastrarthPhoto.isNullOrBlank()) {
                                        AsyncImage(
                                            model = if (appConfig.shastrarthPhoto!!.startsWith("/")) java.io.File(appConfig.shastrarthPhoto!!) else appConfig.shastrarthPhoto,
                                            contentDescription = "Sant Rampal Ji Maharaj",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Image(
                                            painter = painterResource(id = R.drawable.satlok_wave_logo),
                                            contentDescription = "Sant Rampal Ji Maharaj",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "संत रामपाल जी\nमहाराज",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DarkNavyHeading,
                                        fontSize = 13.5.sp
                                    )
                                )
                            }

                            // Center: Bold 3D "VS" Emblem in Royal Blue
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(Color(0xFF4FC3F7), Color(0xFF0288D1), Color(0xFF01579B))
                                        )
                                    )
                                    .border(2.5.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "VS",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        fontSize = 20.sp
                                    )
                                )
                            }

                            // Right: Religious scholars / acharyas representation
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "विद्वान,\nपंडित व आचार्य",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DarkNavyHeading,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.End
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.img_holy_books),
                                    contentDescription = "Scriptures",
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(RoundedCornerShape(10.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }
        }

        // =========================================================================
        // SECTION H: SIDE-BY-SIDE CARDS: भक्ति बोध & शॉर्ट्स (Sky Blue / Ocean Navy)
        // =========================================================================
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Left Card: भक्ति बोध (Sky Blue to Deep Ocean Blue)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(130.dp)
                        .clickable {
                            if (bhaktiBodhTopics.isNotEmpty()) {
                                onNavigateToBhaktiBodh(bhaktiBodhTopics.first())
                            }
                        }
                        .testTag("home_card_bhakti_bodh"),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF039BE5), Color(0xFF0288D1), Color(0xFF01579B))
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "भक्ति बोध",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ऑडियो | वीडियो\nपुस्तकें | काउंटर",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFE1F5FE),
                                    fontSize = 11.5.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.35f),
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

                // Right Card: शॉर्ट्स (Shorts) (Sky Blue Gradient)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(130.dp)
                        .clickable { onNavigateToShorts() }
                        .testTag("home_card_shorts"),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF29B6F6), Color(0xFF0288D1), Color(0xFF0277BD))
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = OceanBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Shorts",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        fontSize = 20.sp
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "सत्संग के मुख्य अंश\nज्ञानवर्धक शॉर्ट क्लिप्स",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFE1F5FE),
                                    fontSize = 11.5.sp,
                                    lineHeight = 16.sp
                                )
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.PlayCircleFilled,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.35f),
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }
            }
        }

        // =========================================================================
        // SECTION I: BHAKTI BODH TOPICS ROW (Nitya Niyam, Aarti, Ramaini)
        // =========================================================================
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "नित्य नियम एवं आरतियां",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OceanBlue
                    ),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(bhaktiBodhTopics) { topic ->
                        Card(
                            modifier = Modifier
                                .width(220.dp)
                                .clickable { onNavigateToBhaktiBodh(topic) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = topic.titleHindi,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DarkNavyHeading
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = topic.timing,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = AashmaniBlue,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// Sub-components with Sky Blue & Ocean Blue Theme
// -------------------------------------------------------------------------

@Composable
private fun HomePhotoCard(
    title: String,
    imageRes: Int,
    customPhoto: String? = null,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(160.dp)
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (!customPhoto.isNullOrBlank()) {
                    AsyncImage(
                        model = if (customPhoto.startsWith("/")) java.io.File(customPhoto) else customPhoto,
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE1F5FE))
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OceanBlue,
                        fontSize = 13.5.sp,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun GradientActionButton(
    text: String,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.horizontalGradient(gradientColors))
            .clickable { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 15.sp
            )
        )
    }
}

@Composable
private fun CircularFigureItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(105.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(86.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF81D4FA), Color(0xFF0288D1), Color(0xFF01579B))
                    )
                )
                .border(2.5.dp, Color(0xFFB3E5FC), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.satlok_wave_logo),
                contentDescription = title,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.5.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
                color = DarkNavyHeading
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun BookCard3D(
    title: String,
    subtitle: String,
    coverColors: List<Color>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Realistic 3D Book Cover with Sant Rampal Ji Maharaj's photo & authentic title
            Box(
                modifier = Modifier
                    .width(124.dp)
                    .height(160.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.verticalGradient(coverColors)
                    )
                    .border(1.dp, Color(0xFFE1F5FE), RoundedCornerShape(6.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header text
                    Text(
                        text = "अमर ज्ञान",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFE1F5FE),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Sant Rampal Ji Maharaj's Portrait on the book
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.5.dp, Color(0xFF81D4FA), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.satlok_wave_logo),
                            contentDescription = "Sant Rampal Ji Maharaj",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Book Title on the cover
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 11.5.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 13.sp
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "संत रामपाल जी",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFB3E5FC),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp,
                    textAlign = TextAlign.Center,
                    color = DarkNavyHeading
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFE1F5FE))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "निःशुल्क मंगवाएं",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = OceanBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.5.sp
                    )
                )
            }
        }
    }
}
