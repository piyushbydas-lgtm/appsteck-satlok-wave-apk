package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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

@Composable
fun NaamDikshaScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var stateDistrict by remember { mutableStateOf("") }
    var formSubmitted by remember { mutableStateOf(false) }

    val rules = listOf(
        "मांस, अंडा, मछली एवं अन्य किसी भी प्रकार के मांसाहार का पूर्ण त्याग।",
        "शराब, बीड़ी, सिगरेट, तंबाकू, गुटखा आदि किसी भी प्रकार के नशीले पदार्थों का सेवन न करना।",
        "दहेज न लेना और न देना। सादगीपूर्ण 'रमैणी' विवाह प्रणाली का पालन करना।",
        "चोरी, जारी, जुआ, रिश्वतखोरी व किसी भी अधर्म कर्म से दूर रहना।",
        "केवल एक पूर्ण परमात्मा कबीर साहेब जी की ही एकनिष्ठ भक्ति करना (अन्य देवी-देवताओं का आदर करते हुए साधना केवल पूर्ण परमात्मा की)।",
        "नित्य नियम, संध्या आरती, असुर निकंदन रमैणी एवं सतनाम का दैनिक अभ्यास।"
    )

    val centers = listOf(
        Pair("सतलोक आश्रम बरवाला", "हिसार, हरियाणा | संपर्क: +91 8222880541"),
        Pair("सतलोक आश्रम बेतुल", "मध्य प्रदेश | संपर्क: +91 8222880542"),
        Pair("सतलोक आश्रम शामली", "उत्तर प्रदेश | संपर्क: +91 8222880543"),
        Pair("सतलोक आश्रम धुरी", "संगरूर, पंजाब | संपर्क: +91 8222880544"),
        Pair("सतलोक आश्रम सोजत", "पाली, राजस्थान | संपर्क: +91 8222880545")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .testTag("naam_diksha_screen")
    ) {
        // Curved Top Bar
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
                        text = "नाम दीक्षा जानकारी एवं केंद्र",
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
            // Hero Intro Card
            item {
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
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(SpiritualOrange.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolunteerActivism,
                                    contentDescription = null,
                                    tint = SpiritualOrange,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "नाम दीक्षा क्या है?",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DarkNavyHeading
                                    )
                                )
                                Text(
                                    text = "पूर्ण मोक्ष एवं सतलोक गमन का एकमात्र मार्ग",
                                    style = MaterialTheme.typography.bodySmall.copy(color = SpiritualOrange)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "जगतगुरु तत्वदर्शी संत रामपाल जी महाराज शास्त्रों (चारों वेद, श्रीमद्भगवद्गीता, पवित्र कुरआन, पवित्र बाइबल एवं श्री गुरु ग्रंथ साहिब) के अनुसार प्रमाणित 3 चरणों में पूर्ण मोक्ष की दीक्षा प्रदान करते हैं। प्रथम मंत्र, द्वितीय 'सतनाम' (दो अक्षर का गुप्त मंत्र), और तृतीय 'सारनाम'।",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 21.sp
                            )
                        )
                    }
                }
            }

            // Rules & Disciplines Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = SpiritualOrange)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "नाम दीक्षा के अनिवार्य नियम",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DarkNavyHeading
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        rules.forEachIndexed { index, rule ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier
                                        .size(18.dp)
                                        .padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = rule,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.5.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Toll Free Help & Helpline
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1B5E20)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "निःशुल्क नाम दीक्षा हेल्पलाइन",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "घर बैठे नाम दीक्षा या निकटतम केंद्र की जानकारी के लिए तुरंत कॉल करें:",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.85f))
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                try {
                                    val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+918222880541"))
                                    context.startActivity(callIntent)
                                } catch (_: Exception) {}
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = Color(0xFF1B5E20))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "कॉल करें: +91 8222880541",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            )
                        }
                    }
                }
            }

            // Ashrams and Naam Daan Kendras
            item {
                Text(
                    text = "प्रमुख नाम दान केंद्र एवं आश्रम",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkNavyHeading
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            items(centers) { (ashram, details) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = SpiritualOrange,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = ashram,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = details,
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }
            }

            // Online Naam Diksha Application Request Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ऑनलाइन नाम दीक्षा आवेदन",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkNavyHeading
                            )
                        )
                        Text(
                            text = "अपना विवरण भरें, आश्रम के सेवादार आपसे संपर्क करेंगे:",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (formSubmitted) {
                            Text(
                                text = "धन्यवाद! आपका नाम दीक्षा आवेदन दर्ज हो गया है। आश्रम के सेवादार जल्द आपसे संपर्क करेंगे। साहिब बंदगी साहिब!",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            )
                        } else {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("पूरा नाम") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                label = { Text("मोबाइल नंबर") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = stateDistrict,
                                onValueChange = { stateDistrict = it },
                                label = { Text("शहर / जिला एवं राज्य") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Button(
                                onClick = {
                                    if (name.isBlank() || phone.isBlank()) {
                                        Toast.makeText(context, "कृपया नाम व मोबाइल नंबर दर्ज करें", Toast.LENGTH_SHORT).show()
                                    } else {
                                        formSubmitted = true
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SpiritualOrange),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("आवेदन सबमिट करें", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(70.dp)) }
        }
    }
}
