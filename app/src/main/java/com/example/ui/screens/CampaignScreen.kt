package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NoDrinks
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkNavyHeading
import com.example.ui.theme.SpiritualOrange

data class SocialCampaign(
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
fun CampaignScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val campaigns = listOf(
        SocialCampaign(
            title = "दहेज मुक्त भारत अभियान",
            subtitle = "17 मिनट में बिना एक रुपये के दहेज के आदर्श विवाह",
            description = "संत रामपाल जी महाराज के सानिध्य में 17 मिनट की गुरुवाणी (असुर निकंदन रमैणी) द्वारा बिना दहेज, बिना बैंड-बाजा, बिना फिजूलखर्ची के लाखों सादगीपूर्ण विवाह कराए जा रहे हैं। समाज को दहेज रूपी दानव से पूर्ण मुक्त कराना इस अभियान का मुख्य लक्ष्य है।",
            icon = Icons.Default.VolunteerActivism,
            accentColor = Color(0xFFE91E63)
        ),
        SocialCampaign(
            title = "नशा मुक्त समाज अभियान",
            subtitle = "शराब, बीड़ी, तंबाकू, ड्रग्स से पूर्ण मुक्ति",
            description = "सतभक्ति और गुरु मर्यादा के प्रभाव से लाखों लोग जो वर्षों से गंभीर नशे के शिकार थे, उन्होंने एक ही दिन में शराब, सिगरेट, मांस आदि का हमेशा के लिए त्याग कर सुखी और सम्मानजनक जीवन शुरू किया है।",
            icon = Icons.Default.NoDrinks,
            accentColor = Color(0xFFF57C00)
        ),
        SocialCampaign(
            title = "रक्त दान महाकुंभ अभियान",
            subtitle = "मानव सेवा ही परमो धर्म - लाखों यूनिट स्वैच्छिक रक्तदान",
            description = "सतलोक आश्रम द्वारा आयोजित समागमों में लाखों श्रद्धालु निस्वार्थ भाव से रक्तदान करते हैं, जिससे जरूरतमंद मरीजों को जीवनदान मिलता है। इसे विश्व में सबसे बड़े रक्तदान अभियानों में गिना जाता है।",
            icon = Icons.Default.Favorite,
            accentColor = Color(0xFFC62828)
        ),
        SocialCampaign(
            title = "अंधविश्वास एवं पाखंड मुक्ति",
            subtitle = "पवित्र शास्त्रों के अनुसार तत्वज्ञान का प्रकाश",
            description = "भूत-प्रेत, तांत्रिक ढोंग, झाड़-फूंक और मनमाने पूजा-पाठ से भटके हुए जनमानस को चारों वेदों, गीता, कुरआन, बाइबल के आधार पर सत्य भक्ति प्रदान कर वास्तविक सुख और मोक्ष की प्राप्ति कराना।",
            icon = Icons.Default.Campaign,
            accentColor = Color(0xFF1976D2)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .testTag("campaign_screen")
    ) {
        Surface(modifier = Modifier.fillMaxWidth(), color = Color.Transparent) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFFE65100), Color(0xFFF57C00), Color(0xFFFFB300))
                        )
                    )
                    .padding(horizontal = 10.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "सामाजिक उत्थान अभियान",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 19.sp
                        )
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(campaigns) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(item.accentColor.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = item.accentColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DarkNavyHeading
                                    )
                                )
                                Text(
                                    text = item.subtitle,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = item.accentColor,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 21.sp
                            )
                        )
                    }
                }
            }

            // Volunteer / Join Campaign Call-To-Action
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SpiritualOrange
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "अभियान में सहभागी बनें",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "दहेज मुक्त विवाह, रक्तदान व नशामुक्ति से जुड़ने के लिए हमारे सेवादारों से संपर्क करें:",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.9f))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+918222880541"))
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "कॉल करें: +91 8222880541",
                                color = SpiritualOrange,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(70.dp)) }
        }
    }
}
