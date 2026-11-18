package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SpiritualOrange

@Composable
fun AboutScreen(onBack: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize().testTag("about_screen"), color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                    Text(text = "हमारे बारे में (About SATLOK WAVE)", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "SATLOK WAVE का पावन उद्देश्य", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "SATLOK WAVE एक समर्पित आध्यात्मिक मंच है, जिसका उद्देश्य जगतगुरु तत्वदर्शी संत रामपाल जी महाराज के सानिध्य में सर्व सद्ग्रंथों (चारों वेद, श्रीमद्भगवद्गीता, पवित्र कुरान शरीफ, पवित्र बाईबल एवं श्री गुरु ग्रंथ साहिब) के आधार पर प्रमाणित सत्य ज्ञान और पूर्ण परमात्मा कबीर साहेब की अमर वाणी को जन-जन तक पहुँचाना है।",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "इस ऐप के माध्यम से प्रत्येक जिज्ञासु आत्मा बिना किसी अंधविश्वास और पाखंड के सृष्टि रचना, मुक्ति का मार्ग, भक्ति बोध और शास्त्र सम्मत साधना को समझ सकती है।",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContactScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize().testTag("contact_screen"), color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                    Text(text = "संपर्क करें (Contact Us)", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ContactInfoItem(icon = Icons.Default.Language, title = "आधिकारिक वेबसाइट", value = "www.jagatgururampalji.org") {
                            try {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.jagatgururampalji.org")))
                            } catch (_: Exception) {}
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        ContactInfoItem(icon = Icons.Default.Phone, title = "निःशुल्क आध्यात्मिक हेल्पलाइन", value = "+91 82228 80541") {
                            try {
                                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:+918222880541")))
                            } catch (_: Exception) {}
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        ContactInfoItem(icon = Icons.Default.Email, title = "ईमेल संपर्क", value = "info@satlokwave.com") {
                            try {
                                context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:info@satlokwave.com")))
                            } catch (_: Exception) {}
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        ContactInfoItem(icon = Icons.Default.Place, title = "आश्रम पता", value = "सतलोक आश्रम बरवाला / मुंडका / धुरी / रोहतक") {}
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactInfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, value: String, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }, verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = SpiritualOrange)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
        }
    }
}

@Composable
fun PolicyScreen(title: String, content: String, onBack: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize().testTag("policy_screen"), color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                    Text(text = title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = content, style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp))
                    }
                }
            }
        }
    }
}
