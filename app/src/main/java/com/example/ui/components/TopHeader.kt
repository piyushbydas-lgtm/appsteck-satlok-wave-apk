package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.AashmaniBlue
import com.example.ui.theme.AashmaniCyan
import com.example.ui.theme.DarkNavyHeading
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.SkyBluePrimary
import com.example.util.AudioPlaybackManager

@Composable
fun TopHeader(
    appName: String = "SATLOK WAVE",
    customLogoUrl: String? = null,
    isAdmin: Boolean = false,
    onSearchClick: () -> Unit,
    onMenuClick: () -> Unit,
    onAdminBadgeClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("top_header_section")
    ) {
        // Main curved header bar in pristine white & soft sky-blue
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: 3-line Sky-Blue Hamburger Menu
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier
                        .size(42.dp)
                        .testTag("header_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = AashmaniBlue,
                        modifier = Modifier.size(30.dp)
                    )
                }

                // Official App Logo with Sant Rampal Ji Maharaj
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE1F5FE))
                        .border(1.5.dp, AashmaniBlue, CircleShape)
                        .clickable { onMenuClick() },
                    contentAlignment = Alignment.Center
                ) {
                    if (!customLogoUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = if (customLogoUrl.startsWith("/")) java.io.File(customLogoUrl) else customLogoUrl,
                            contentDescription = "SATLOK WAVE Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.satlok_wave_logo),
                            contentDescription = "SATLOK WAVE Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Center: Two-line Title in Deep Ocean Blue
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "जगत गुरु तत्वदर्शी",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OceanBlue,
                            fontSize = 15.sp,
                            letterSpacing = 0.2.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "संत रामपाल जी महाराज",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkNavyHeading,
                            fontSize = 17.5.sp,
                            letterSpacing = 0.2.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    if (isAdmin) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AashmaniBlue.copy(alpha = 0.15f))
                                .clickable { onAdminBadgeClick() }
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Admin",
                                    tint = AashmaniBlue,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "ADMIN ACTIVE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AashmaniBlue
                                    )
                                )
                            }
                        }
                    }
                }

                // Right: Green Phone Call button + Blue Live Radio button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 1. Green Circular Call Button (Helpline: +918222880541)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2E7D32))
                            .clickable {
                                try {
                                    val callIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:+918222880541")
                                    }
                                    context.startActivity(callIntent)
                                } catch (_: Exception) {}
                            }
                            .testTag("header_helpline_call"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Helpline",
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    // 2. Sky Blue / Ocean Blue Live Radio Button
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0277BD))
                            .clickable {
                                AudioPlaybackManager.play(
                                    title = "लाइव सत्संग एवं आध्यात्मिक प्रसारण",
                                    speaker = "संत रामपाल जी महाराज",
                                    audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3"
                                )
                            }
                            .testTag("header_live_radio"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Radio,
                            contentDescription = "Live Radio",
                            tint = Color(0xFFE1F5FE),
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }

        // Sky Blue & Ocean Blue horizontal category strip (आसमानी व नीला)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF039BE5), // Sky Blue (आसमानी)
                            Color(0xFF0288D1),
                            Color(0xFF0277BD),
                            Color(0xFF01579B)  // Royal Ocean Blue (नीला)
                        )
                    )
                )
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .testTag("header_red_category_strip")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Scrollable category text items
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val categories = listOf("समागम", "चमत्कार", "ई-पुस्तकें", "लाइव", "शंका समाधान", "APK डाउनलोड")
                    categories.forEach { cat ->
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.5.sp
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onCategoryClick(cat) }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .testTag("category_strip_$cat")
                        )
                    }
                }

                // Search Icon on the right edge
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("header_strip_search_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
