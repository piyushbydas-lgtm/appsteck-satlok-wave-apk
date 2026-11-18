package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PermMedia
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SpiritualOrange
import com.example.util.LanguageManager

enum class NavDestination(
    val route: String,
    val hindiLabel: String,
    val englishLabel: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME(
        route = "home",
        hindiLabel = "होम",
        englishLabel = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    MEDIA(
        route = "media",
        hindiLabel = "मीडिया",
        englishLabel = "Media",
        selectedIcon = Icons.Filled.PermMedia,
        unselectedIcon = Icons.Outlined.PermMedia
    ),
    NAAM_DIKSHA(
        route = "naam_diksha",
        hindiLabel = "नाम दीक्षा",
        englishLabel = "Naam Diksha",
        selectedIcon = Icons.Filled.VolunteerActivism,
        unselectedIcon = Icons.Outlined.VolunteerActivism
    ),
    SHORTS(
        route = "shorts",
        hindiLabel = "शॉर्ट्स",
        englishLabel = "Shorts",
        selectedIcon = Icons.Filled.PlayArrow,
        unselectedIcon = Icons.Outlined.PlayCircleOutline
    ),
    MESSAGES(
        route = "messages",
        hindiLabel = "संदेश",
        englishLabel = "Messages",
        selectedIcon = Icons.Filled.ChatBubble,
        unselectedIcon = Icons.Outlined.ChatBubbleOutline
    )
}

@Composable
fun SatlokBottomNavBar(
    currentDestination: NavDestination,
    onNavigate: (NavDestination) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .testTag("bottom_nav_bar"),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 12.dp,
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavDestination.entries.forEach { destination ->
                val isSelected = currentDestination == destination
                val label = LanguageManager.get(destination.hindiLabel, destination.englishLabel)

                if (destination == NavDestination.NAAM_DIKSHA) {
                    // Center highlighted black pill button as in the screenshots
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color(0xFF181818))
                            .clickable { onNavigate(destination) }
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .testTag("nav_item_naam_diksha"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.VolunteerActivism,
                                contentDescription = label,
                                tint = if (isSelected) Color(0xFF4FC3F7) else Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color(0xFF4FC3F7) else Color.White
                                )
                            )
                        }
                    }
                } else {
                    // Standard navigation tabs
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onNavigate(destination) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("nav_item_${destination.route}"),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                            contentDescription = label,
                            tint = if (isSelected) SpiritualOrange else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) SpiritualOrange else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    }
}
