package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AashmaniBlue
import com.example.ui.theme.DarkNavyHeading
import com.example.ui.theme.OceanBlue
import com.example.util.AppLanguage

@Composable
fun SatlokNavDrawerContent(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    currentLanguage: AppLanguage,
    onToggleLanguage: () -> Unit,
    isAdminLoggedIn: Boolean,
    onNavigateHome: () -> Unit,
    onNavigateAbout: () -> Unit,
    onNavigateCounter: () -> Unit,
    onNavigateContact: () -> Unit,
    onNavigateCampaigns: () -> Unit,
    onNavigateNaamDiksha: () -> Unit,
    onNavigateFreeBooks: () -> Unit,
    onNavigateBloodDonation: () -> Unit,
    onNavigateBhaktiBodh: () -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateAdminLogin: () -> Unit,
    onNavigateAdminDashboard: () -> Unit,
    onDownloadApk: () -> Unit,
    onAdminLogout: () -> Unit,
    onCloseDrawer: () -> Unit
) {
    val context = LocalContext.current

    ModalDrawerSheet(
        modifier = Modifier
            .width(310.dp)
            .fillMaxHeight()
            .testTag("nav_drawer_content"),
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            // Header with Sky Blue & Deep Ocean Blue gradient (आसमानी व नीला)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF29B6F6), // Light Sky Blue
                                Color(0xFF0288D1), // Sky Blue Primary
                                Color(0xFF0277BD),
                                Color(0xFF01579B)  // Deep Royal Ocean Blue
                            )
                        )
                    )
                    .padding(vertical = 26.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Official App Logo with Sant Rampal Ji Maharaj
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(2.5.dp, Color(0xFFE1F5FE), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.satlok_wave_logo),
                            contentDescription = "Sant Rampal Ji Maharaj Logo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "जगत गुरु तत्वदर्शी",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE1F5FE),
                            fontSize = 16.sp,
                            letterSpacing = 0.3.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "संत रामपाल जी महाराज",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 20.sp,
                            lineHeight = 24.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Menu Items List with circular Sky Blue icons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                DrawerCircleIconItem(
                    icon = Icons.Default.Info,
                    label = "हमारे बारे में",
                    onClick = {
                        onCloseDrawer()
                        onNavigateAbout()
                    }
                )

                DrawerCircleIconItem(
                    icon = Icons.Default.Timer,
                    label = "काउंटर",
                    onClick = {
                        onCloseDrawer()
                        onNavigateCounter()
                    }
                )

                DrawerCircleIconItem(
                    icon = Icons.Default.ContactPhone,
                    label = "संपर्क करें",
                    onClick = {
                        onCloseDrawer()
                        onNavigateContact()
                    }
                )

                DrawerCircleIconItem(
                    icon = Icons.Default.Campaign,
                    label = "अभियान",
                    onClick = {
                        onCloseDrawer()
                        onNavigateCampaigns()
                    }
                )

                DrawerCircleIconItem(
                    icon = Icons.Default.VolunteerActivism,
                    label = "नाम दीक्षा",
                    onClick = {
                        onCloseDrawer()
                        onNavigateNaamDiksha()
                    }
                )

                DrawerCircleIconItem(
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    label = "फ्री पुस्तक मंगाए",
                    onClick = {
                        onCloseDrawer()
                        onNavigateFreeBooks()
                    }
                )

                DrawerCircleIconItem(
                    icon = Icons.Default.Favorite,
                    label = "रक्त दान करें",
                    onClick = {
                        onCloseDrawer()
                        onNavigateBloodDonation()
                    }
                )

                DrawerCircleIconItem(
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    label = "भक्ति बोध",
                    onClick = {
                        onCloseDrawer()
                        onNavigateBhaktiBodh()
                    }
                )

                // Direct APK Download Option
                DrawerCircleIconItem(
                    icon = Icons.Default.Android,
                    label = "APK डाउनलोड करें (Download APK)",
                    badge = "OFFICIAL",
                    onClick = {
                        onCloseDrawer()
                        onDownloadApk()
                    }
                )

                DrawerCircleIconItem(
                    icon = Icons.Default.Settings,
                    label = "सेटिंग्स",
                    onClick = {
                        onCloseDrawer()
                        onNavigateSettings()
                    }
                )

                DrawerCircleIconItem(
                    icon = Icons.Default.Share,
                    label = "शेयर करें",
                    onClick = {
                        onCloseDrawer()
                        try {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "सत्य ज्ञान एवं पूर्ण मोक्ष के लिए SATLOK WAVE ऐप डाउनलोड करें: https://satlokashram.org/"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "SATLOK WAVE शेयर करें"))
                        } catch (_: Exception) {}
                    }
                )

                // Admin Action in Drawer
                if (isAdminLoggedIn) {
                    DrawerCircleIconItem(
                        icon = Icons.Default.AdminPanelSettings,
                        label = "एडमिन डैशबोर्ड (कंटेंट अपलोड)",
                        badge = "ACTIVE",
                        onClick = {
                            onCloseDrawer()
                            onNavigateAdminDashboard()
                        }
                    )
                } else {
                    DrawerCircleIconItem(
                        icon = Icons.Default.Lock,
                        label = "एडमिन लॉगिन",
                        onClick = {
                            onCloseDrawer()
                            onNavigateAdminLogin()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Footer Section: "हमारे साथ जुड़ें" (Social links in Sky Blue)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "हमारे साथ जुड़ें",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkNavyHeading,
                        fontSize = 15.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SocialButton(label = "f") {
                        openWebUrl(context, "https://facebook.com/spiritualleadersantrampalji")
                    }
                    SocialButton(label = "𝕏") {
                        openWebUrl(context, "https://twitter.com/saintrampaljim")
                    }
                    SocialButton(label = "📷") {
                        openWebUrl(context, "https://instagram.com/santrampaljimaharaj")
                    }
                    SocialButton(label = "▶") {
                        openWebUrl(context, "https://www.youtube.com/@SantRampalJiMaharajOfficial")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun DrawerCircleIconItem(
    icon: ImageVector,
    label: String,
    badge: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Sky Blue circle icon
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(2.dp, AashmaniBlue, CircleShape)
                .background(Color(0xFFE1F5FE)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = AashmaniBlue,
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = DarkNavyHeading
            ),
            modifier = Modifier.weight(1f)
        )

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(AashmaniBlue)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun SocialButton(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(1.5.dp, AashmaniBlue.copy(alpha = 0.4f), CircleShape)
            .background(Color(0xFFF0F7FC))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = OceanBlue,
                fontSize = 15.sp
            )
        )
    }
}

private fun openWebUrl(context: android.content.Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (_: Exception) {}
}
