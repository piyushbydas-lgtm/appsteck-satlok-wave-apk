package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkNavyHeading
import com.example.ui.theme.SpiritualOrange

data class SpiritualMessage(
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val date: String
)

@Composable
fun MessagesScreen() {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("सभी") }
    val categories = listOf("सभी", "अमृतवाणी", "घोषणाएं", "सत्य संदेश")

    val messageList = remember {
        listOf(
            SpiritualMessage(
                id = "m1",
                title = "कबीर साहेब जी की अमृतवाणी",
                content = "कबीर, मानुष जनम दुर्लभ है, मिले न बारम्बार।\nतरुवर ते पत्ता टूट गिरै, बहुरि न लागे डार।।\n\nअर्थात: यह अनमोल मानव शरीर 84 लाख योनियों के उपरांत अत्यंत कठिनता से प्राप्त होता है। जैसे वृक्ष से पत्ता टूटकर दोबारा उस डाल पर नहीं जुड़ सकता, उसी प्रकार यह समय बीत जाने पर पुनः नहीं मिलेगा। समय रहते तत्वदर्शी संत की शरण में पूर्ण परमात्मा की भक्ति करें।",
                category = "अमृतवाणी",
                date = "आज का विचार"
            ),
            SpiritualMessage(
                id = "m2",
                title = "वार्षिक विशाल समागम सूचना",
                content = "आदरणीय भक्तजनों को सूचित किया जाता है कि आगामी विशाल सत्संग समागम एवं अखण्ड पाठ साहिब का आयोजन सभी सतलोक आश्रमों (बरवाला, बेतुल, शामली, धुरी, सोजत) में धूमधाम से किया जाएगा। सभी श्रद्धालु सपरिवार सादर आमंत्रित हैं। भंडारा, ठहरने एवं स्वास्थ्य की व्यवस्था पूर्णतः निःशुल्क रहेगी।",
                category = "घोषणाएं",
                date = "नवीनतम घोषणा"
            ),
            SpiritualMessage(
                id = "m3",
                title = "परमात्मा साकार है या निराकार?",
                content = "यजुर्वेद अध्याय 5 मंत्र 1 और यजुर्वेद अध्याय 40 मंत्र 8 में स्पष्ट प्रमाणित है कि 'कविर्मनीषी परिभूः स्वयम्भूः' अर्थात् सर्वशक्तिमान परमात्मा कविर्देव (कबीर साहेब) साकार मनुष्य जैसे रूप में हैं और अपने निजधाम सतलोक में सिंहासन पर विराजमान हैं।",
                category = "सत्य संदेश",
                date = "ज्ञान संदेश"
            ),
            SpiritualMessage(
                id = "m4",
                title = "दहेज मुक्त विवाह अभियान",
                content = "संत रामपाल जी महाराज के पावन सानिध्य में 17 मिनट की सादगीपूर्ण 'असुर निकंदन रमैणी' द्वारा बिना किसी दान-दहेज, बैंड-बाजे और फिजूलखर्ची के लाखों आदर्श विवाह संपन्न हो रहे हैं। समाज को इस कुप्रथा से मुक्त कराने में सहयोग दें।",
                category = "घोषणाएं",
                date = "सामाजिक संदेश"
            )
        )
    }

    val filteredList = if (selectedFilter == "सभी") {
        messageList
    } else {
        messageList.filter { it.category == selectedFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .testTag("messages_screen")
    ) {
        // Top Header
        Surface(modifier = Modifier.fillMaxWidth(), color = Color.Transparent) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0288D1), Color(0xFF0277BD), Color(0xFF01579B))
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "आध्यात्मिक संदेश एवं अमृतवाणी",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 19.sp
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedFilter == cat,
                            onClick = { selectedFilter = cat },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0288D1),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            items(filteredList, key = { it.id }) { msg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (msg.category == "अमृतवाणी") SpiritualOrange.copy(alpha = 0.15f)
                                            else Color(0xFF0288D1).copy(alpha = 0.15f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (msg.category == "अमृतवाणी") Icons.Default.FormatQuote else Icons.Default.Campaign,
                                        contentDescription = null,
                                        tint = if (msg.category == "अमृतवाणी") SpiritualOrange else Color(0xFF0288D1),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = msg.title,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DarkNavyHeading
                                        )
                                    )
                                    Text(
                                        text = msg.date,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.outline,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    try {
                                        val shareIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, "${msg.title}\n\n${msg.content}\n\n- SATLOK WAVE")
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(shareIntent, "संदेश शेयर करें"))
                                    } catch (_: Exception) {}
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = msg.content,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 22.sp
                            )
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}
