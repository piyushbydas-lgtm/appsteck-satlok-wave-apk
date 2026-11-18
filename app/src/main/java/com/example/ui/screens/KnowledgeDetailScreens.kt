package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BhaktiBodhTopic
import com.example.data.model.ContentItem
import com.example.data.model.KnowledgeCategory
import com.example.data.model.MediaType
import com.example.data.model.ScriptureEvidence
import com.example.data.model.SrishtiRachnaSection
import com.example.data.repository.SatlokRepository
import com.example.ui.theme.DarkNavyHeading
import com.example.ui.theme.SpiritualOrange
import com.example.util.AudioPlaybackManager

// ----------------------------------------------------
// SECTION 1: सृष्टि रचना (Creation of the Universe)
// ----------------------------------------------------
@Composable
fun SrishtiRachnaScreen(
    onBack: () -> Unit,
    onPlayVideo: (ContentItem) -> Unit
) {
    val sections by SatlokRepository.srishtiRachnaSections.collectAsState()
    val appConfig by SatlokRepository.appConfig.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("srishti_rachna_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "सृष्टि रचना (Creation of Universe)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            // Hero Graphic & Description
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_satlok_hero_banner),
                                contentDescription = "Satlok Amar Lok",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.45f))
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = "अमरलोक (सतलोक) एवं सृष्टि उत्पत्ति",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "परमेश्वर कबीर साहेब द्वारा रचित सम्पूर्ण सृष्टि का यथार्थ विवरण",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.LightGray)
                                )
                            }
                        }

                        // Main Dedicated Video Button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable {
                                    onPlayVideo(
                                        ContentItem(
                                            id = "srishti_main_vid",
                                            title = "सृष्टि रचना का सम्पूर्ण रहस्य - संत रामपाल जी महाराज",
                                            description = "सद्ग्रंथों के अकाट्य प्रमाणों द्वारा सृष्टि रचना का विस्तृत ज्ञान।",
                                            type = MediaType.VIDEO,
                                            category = KnowledgeCategory.SRISHTI_RACHNA,
                                            playlist = "Srishti Rachna",
                                            mediaUrl = appConfig.srishtiRachnaMainVideoUrl,
                                            duration = "48:15"
                                        )
                                    )
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(SpiritualOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play Dedicated Video",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "सृष्टि रचना आधिकारिक वीडियो देखें",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                                Text(
                                    text = "संत रामपाल जी महाराज यूट्यूब चैनल द्वारा अधिकृत",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = SpiritualOrange,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Step-by-step Srishti Rachna Sections
            items(sections) { section ->
                SrishtiRachnaItemCard(section = section, onPlayVideo = onPlayVideo)
            }
        }
    }
}

@Composable
private fun SrishtiRachnaItemCard(
    section: SrishtiRachnaSection,
    onPlayVideo: (ContentItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("srishti_step_${section.stepNumber}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${section.stepNumber}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = section.subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = SpiritualOrange,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expand"
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = section.detailedText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "सद्ग्रंथ संदर्भ:",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    section.scripturalReferences.forEach { ref ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = SpiritualOrange,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = ref,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    if (section.keyPoints.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "मुख्य आध्यात्मिक बिंदु:",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        section.keyPoints.forEach { point ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(14.dp).padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = point,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }

                    if (section.videoUrl.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onPlayVideo(
                                        ContentItem(
                                            id = "section_vid_${section.id}",
                                            title = section.title,
                                            description = section.subtitle,
                                            type = MediaType.VIDEO,
                                            category = KnowledgeCategory.SRISHTI_RACHNA,
                                            mediaUrl = section.videoUrl,
                                            duration = "45:00"
                                        )
                                    )
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.PlayCircleFilled, contentDescription = "Play", tint = SpiritualOrange)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "संबंधित वीडियो सत्संग देखें",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
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

// ------------------------------------------------------------------------
// SECTION 2: वेदों, गीता, पुराणों एवं शास्त्रों में प्रमाण (Scripture Evidence)
// ------------------------------------------------------------------------
@Composable
fun ScriptureEvidenceScreen(
    onBack: () -> Unit,
    onPlayVideo: (ContentItem) -> Unit
) {
    val evidences by SatlokRepository.scriptureEvidences.collectAsState()
    val subcategories = listOf(
        "All (सभी)",
        "वेदों में प्रमाण",
        "श्रीमद्भगवद्गीता में प्रमाण",
        "पुराणों में प्रमाण",
        "अन्य पवित्र शास्त्रों में प्रमाण",
        "कबीर साहिब परमात्मा के प्रमाण"
    )
    var selectedCategory by remember { mutableStateOf("All (सभी)") }

    val filtered = if (selectedCategory == "All (सभी)") {
        evidences
    } else {
        evidences.filter { it.subcategory == selectedCategory }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("scripture_evidence_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "शास्त्रों में प्रमाण (Scriptures Evidence)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            // Subcategory Filter Chips
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(subcategories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            items(filtered) { evidence ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("evidence_card_${evidence.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SpiritualOrange.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = evidence.subcategory,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SpiritualOrange,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Text(
                                text = "${evidence.scriptureName} - ${evidence.chapterVerse}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Original Verse (Sanskrit / Gurmukhi / Arabic)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "“${evidence.originalVerse}”",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    lineHeight = 22.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Hindi Explanation
                        Text(
                            text = "हिन्दी सरल व्याख्या:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )

                        Text(
                            text = evidence.hindiExplanation,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 22.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        if (evidence.videoUrl.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        onPlayVideo(
                                            ContentItem(
                                                id = "ev_vid_${evidence.id}",
                                                title = "${evidence.scriptureName} (${evidence.chapterVerse}) प्रमाण",
                                                description = evidence.hindiExplanation,
                                                type = MediaType.VIDEO,
                                                category = KnowledgeCategory.SHASTRARTH,
                                                mediaUrl = evidence.videoUrl,
                                                duration = "50:00"
                                            )
                                        )
                                    }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.PlayCircleFilled, contentDescription = "Play", tint = SpiritualOrange)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "संत रामपाल जी महाराज द्वारा वीडियो व्याख्या देखें",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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

// ----------------------------------------------------
// SECTION 3: पवित्र ग्रंथों का सार (Granth Saar)
// ----------------------------------------------------
@Composable
fun GranthSaarScreen(
    onBack: () -> Unit,
    onPlayVideo: (ContentItem) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("granth_saar_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "पवित्र ग्रंथों का सार (Granth Saar)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "पवित्र चारों वेदों का यथार्थ सार",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "ऋग्वेद, यजुर्वेद, सामवेद एवं अथर्ववेद में सर्वत्र यही प्रमाणित है कि पूर्ण परमात्मा एक है, जिसका वास्तविक अमर नाम 'कविर्देव' है। वह सहशरीर है और सतलोक का वासी है। वेदों में यज्ञ, दान और सत्य नाम स्मरण को ही मोक्ष का साधन बताया गया है।",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "श्रीमद्भगवद्गीता का यथार्थ सार",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "गीता जी में तीन पुरुषों (देवों) का वर्णन है: क्षर पुरुष (काल ब्रह्म), अक्षर पुरुष (परब्रह्म) और उत्तम पुरुष (परम अक्षर ब्रह्म / कबीर परमात्मा)। गीता अध्याय 18 श्लोक 62 में काल ब्रह्म स्वयं अर्जुन को उत्तम पुरुष की शरण में जाने का आदेश देता है ताकि परम शांति प्राप्त हो।",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "कबीर सागर एवं अनुराग सागर का सार",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "कबीर सागर में परमेश्वर कबीर साहेब ने अपने प्रिय शिष्य आदरणीय धर्मदास जी को सृष्टि के गूढ़ रहस्यों, काल की उत्पत्ति, जीव के बंधन और सतनाम तथा सारनाम द्वारा मोक्ष का संपूर्ण मार्ग विस्तारपूर्वक समझाया है।",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp)
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "सद्गुरु कबीर साहेब जी की अमृत वाणी",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "“माला फेरत जुग भया, फिरा न मन का फेर।\nकर का मनका डारि दे, मन का मनका फेर।।”\n\n“पाहन पूजे हरि मिले, तो मैं पूजौं पहार।\nतातें यह चाकी भली, पीस खाय संसार।।”",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = SpiritualOrange,
                                lineHeight = 24.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

// ----------------------------------------------------
// SECTION 4: सभी धर्मों में कबीर परमात्मा (All Religions)
// ----------------------------------------------------
@Composable
fun AllReligionsScreen(
    onBack: () -> Unit,
    onPlayVideo: (ContentItem) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("all_religions_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = "सभी धर्मों में कबीर परमात्मा",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            // Hindu
            item {
                ReligiousEvidenceCard(
                    religion = "सनातन / हिन्दू धर्म",
                    scripture = "ऋग्वेद मण्डल 9 सूक्त 96 मन्त्र 17-20",
                    verse = "शिशुं जज्ञानं हर्यतं मृजन्ति... कविर्गीर्भिः काव्येना कवीनाम्",
                    meaning = "वेदों में परमात्मा का मूल नाम 'कविर्देव' (कबीर साहेब) बताया गया है जो अमरलोक से प्रकट होते हैं।"
                )
            }

            // Sikh
            item {
                ReligiousEvidenceCard(
                    religion = "सिख धर्म",
                    scripture = "श्री गुरु ग्रंथ साहिब, राग आसा महला १, पृष्ठ ७२१",
                    verse = "हक्का कबीर करीम तू बेऐब परवरदिगार। नानक बुगोयद जनु तुरा तेरे चाकरां पाखाक।।",
                    meaning = "गुरु नानक देव जी ने कबीर साहेब को ही सच्चा परमात्मा, करतार और सृष्टि का सृजनहार बताया है।"
                )
            }

            // Islam
            item {
                ReligiousEvidenceCard(
                    religion = "इस्लाम धर्म",
                    scripture = "पवित्र कुरान शरीफ, सूरह अल-फुरकान 25 आयत 52-59",
                    verse = "अल्लजी खलकस्समावाति वल अर्ज़... अर्रहमानु फस्अल बिही खबीरन।",
                    meaning = "जिसने 6 दिन में सृष्टि बनाई और सातवें दिन तख्त पर जा विराजा, वह दयालु अल्लाह 'कबीरन' (कबीर) है।"
                )
            }

            // Christianity
            item {
                ReligiousEvidenceCard(
                    religion = "ईसाई धर्म",
                    scripture = "पवित्र बाईबल, उत्पत्ति (Genesis) अध्याय 1 आयत 26-27",
                    verse = "God created man in His own image, in the image of God created he him.",
                    meaning = "परमात्मा साकार (नर-आकार) है और उसने मनुष्य को अपने स्वरूप जैसा बनाया, जो पूर्ण परमेश्वर कबीर हैं।"
                )
            }
        }
    }
}

@Composable
private fun ReligiousEvidenceCard(
    religion: String,
    scripture: String,
    verse: String,
    meaning: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = religion,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = scripture,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                    .padding(10.dp)
            ) {
                Text(
                    text = "“$verse”",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = meaning,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp
                )
            )
        }
    }
}

// ----------------------------------------------------
// Bhakti Bodh Detail Screen
// ----------------------------------------------------
@Composable
fun BhaktiBodhDetailScreen(
    topic: BhaktiBodhTopic,
    onBack: () -> Unit,
    onPlayVideo: (ContentItem) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("bhakti_bodh_detail_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = topic.hindiTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "नित्य पाठ समय: ${topic.timing}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                            Text(
                                text = "अवधि: ${topic.duration}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(SpiritualOrange)
                                .clickable {
                                    AudioPlaybackManager.playTrack(
                                        ContentItem(
                                            id = topic.id,
                                            title = topic.hindiTitle,
                                            description = topic.meaning,
                                            type = MediaType.AUDIO,
                                            category = KnowledgeCategory.BHAKTI_BODH,
                                            playlist = "भक्ति बोध",
                                            duration = topic.duration
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play Audio", tint = Color.White)
                        }
                    }
                }
            }

            // Full Text
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "पावन अमृत वाणी पाठ:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = topic.fullText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                lineHeight = 28.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }

            // Meaning
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "भावार्थ एवं आध्यात्मिक लाभ:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SpiritualOrange
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = topic.meaning,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 22.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    }
}
