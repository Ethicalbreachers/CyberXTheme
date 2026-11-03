package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class AppAppearanceMode {
    AMOLED_BLACK,
    CYBER_DARK,
    LIGHT_MODE
}

val CyberShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun CyberThemeXTheme(
    appearanceMode: AppAppearanceMode = AppAppearanceMode.AMOLED_BLACK,
    primaryAccentHex: String = "#00FF66",
    secondaryAccentHex: String = "#00E5FF",
    fontPreset: String = "Cyber",
    fontScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val primaryAccent = parseHexColor(primaryAccentHex, NeonGreen)
    val secondaryAccent = parseHexColor(secondaryAccentHex, ElectricCyan)

    val colorScheme = when (appearanceMode) {
        AppAppearanceMode.AMOLED_BLACK -> darkColorScheme(
            primary = primaryAccent,
            onPrimary = Color(0xFF001A09),
            primaryContainer = primaryAccent.copy(alpha = 0.18f),
            onPrimaryContainer = primaryAccent,
            secondary = secondaryAccent,
            onSecondary = Color(0xFF001F24),
            secondaryContainer = secondaryAccent.copy(alpha = 0.18f),
            onSecondaryContainer = secondaryAccent,
            tertiary = NeonPurple,
            background = AmoledBlack,
            onBackground = TextPrimaryDark,
            surface = Color(0xFF090D14),
            onSurface = TextPrimaryDark,
            surfaceVariant = Color(0xFF121924),
            onSurfaceVariant = TextSecondaryDark,
            outline = CyberBorderSubtle
        )
        AppAppearanceMode.CYBER_DARK -> darkColorScheme(
            primary = primaryAccent,
            onPrimary = Color(0xFF001A09),
            primaryContainer = primaryAccent.copy(alpha = 0.22f),
            onPrimaryContainer = primaryAccent,
            secondary = secondaryAccent,
            onSecondary = Color(0xFF001F24),
            secondaryContainer = secondaryAccent.copy(alpha = 0.22f),
            onSecondaryContainer = secondaryAccent,
            tertiary = NeonPurple,
            background = CyberObsidian,
            onBackground = TextPrimaryDark,
            surface = CyberSurface,
            onSurface = TextPrimaryDark,
            surfaceVariant = CyberSurfaceElevated,
            onSurfaceVariant = TextSecondaryDark,
            outline = CyberBorderSubtle
        )
        AppAppearanceMode.LIGHT_MODE -> lightColorScheme(
            primary = Color(0xFF008F39),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFD6FFE5),
            onPrimaryContainer = Color(0xFF003914),
            secondary = Color(0xFF007B8A),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFD4F8FF),
            onSecondaryContainer = Color(0xFF002A30),
            tertiary = Color(0xFF6200EA),
            background = Color(0xFFF3F6FA),
            onBackground = TextPrimaryLight,
            surface = Color.White,
            onSurface = TextPrimaryLight,
            surfaceVariant = Color(0xFFE2E8F0),
            onSurfaceVariant = TextSecondaryLight,
            outline = Color(0xFFCBD5E1)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = buildCyberTypography(fontPreset = fontPreset, scale = fontScale),
        shapes = CyberShapes,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    CyberThemeXTheme(content = content)
}
