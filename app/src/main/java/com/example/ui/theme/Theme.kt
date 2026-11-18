package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SatlokDarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkBg,
    primaryContainer = Color(0xFF0F3554),
    onPrimaryContainer = Color(0xFFBAE6FD),
    secondary = DarkSecondary,
    onSecondary = Color(0xFF001F3F),
    secondaryContainer = Color(0xFF01579B),
    onSecondaryContainer = Color(0xFFB3E5FC),
    tertiary = SacredGold,
    onTertiary = Color.Black,
    background = DarkBg,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkMuted,
    outline = DarkBorder
)

private val SatlokLightColorScheme = lightColorScheme(
    primary = SkyBluePrimary,
    onPrimary = SkyBlueOnPrimary,
    primaryContainer = SkyBlueContainer,
    onPrimaryContainer = SkyBlueOnContainer,
    secondary = SpiritualOrange,
    onSecondary = Color.White,
    secondaryContainer = OrangeContainer,
    onSecondaryContainer = OrangeOnContainer,
    tertiary = SacredGold,
    onTertiary = DarkNavyText,
    background = LightSkyBackground,
    onBackground = DarkNavyText,
    surface = SoftCardWhite,
    onSurface = DarkNavyText,
    surfaceVariant = Color(0xFFE8F2F8),
    onSurfaceVariant = Color(0xFF3B566E),
    outline = SoftBorderSky
)

@Composable
fun SatlokWaveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) SatlokDarkColorScheme else SatlokLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SatlokTypography,
        content = content
    )
}
