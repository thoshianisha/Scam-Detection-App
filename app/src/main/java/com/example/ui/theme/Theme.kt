package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ScamShieldDarkColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = CyberObsidian,
    primaryContainer = ElectricBlue,
    onPrimaryContainer = TextPrimaryDark,
    secondary = NeonPurple,
    onSecondary = Color.White,
    secondaryContainer = CyberSurfaceElevated,
    onSecondaryContainer = TextPrimaryDark,
    tertiary = SafeEmerald,
    onTertiary = CyberObsidian,
    background = CyberObsidian,
    onBackground = TextPrimaryDark,
    surface = CyberSlateDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = CyberSurfaceCard,
    onSurfaceVariant = TextSecondaryDark,
    error = ThreatCrimson,
    onError = Color.White,
    outline = GlassBorderWhite
)

private val ScamShieldLightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = NeonPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE9FE),
    onSecondaryContainer = Color(0xFF4C1D95),
    tertiary = SafeEmerald,
    onTertiary = Color.White,
    background = CyberLightBg,
    onBackground = Color(0xFF0F172A),
    surface = CyberLightSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = CyberLightCard,
    onSurfaceVariant = Color(0xFF475569),
    error = ThreatCrimson,
    onError = Color.White,
    outline = Color(0xFFCBD5E1)
)

@Composable
fun ScamShieldTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ScamShieldDarkColorScheme else ScamShieldLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
