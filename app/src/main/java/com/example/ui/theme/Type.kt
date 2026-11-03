package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val OrbitronFontFamily = FontFamily(
    Font(R.font.orbitron, FontWeight.Normal),
    Font(R.font.orbitron, FontWeight.Bold)
)

val RajdhaniFontFamily = FontFamily(
    Font(R.font.rajdhani, FontWeight.Normal),
    Font(R.font.rajdhani, FontWeight.Bold)
)

val SpaceGroteskFontFamily = FontFamily(
    Font(R.font.space_grotesk, FontWeight.Normal),
    Font(R.font.space_grotesk, FontWeight.Medium),
    Font(R.font.space_grotesk, FontWeight.Bold)
)

val ShareTechMonoFontFamily = FontFamily(
    Font(R.font.share_tech_mono, FontWeight.Normal)
)

val PacificoFontFamily = FontFamily(
    Font(R.font.pacifico, FontWeight.Normal)
)

val PlayfairDisplayFontFamily = FontFamily(
    Font(R.font.playfair_display, FontWeight.Normal),
    Font(R.font.playfair_display, FontWeight.Bold)
)

fun resolveFontFamilyByName(name: String): FontFamily {
    return when (name.lowercase()) {
        "cyber", "orbitron", "futuristic" -> OrbitronFontFamily
        "gaming", "rajdhani", "bold" -> RajdhaniFontFamily
        "monospace", "hacker", "share tech mono", "terminal" -> ShareTechMonoFontFamily
        "handwriting", "pacifico", "script" -> PacificoFontFamily
        "elegant", "playfair", "royal", "serif" -> PlayfairDisplayFontFamily
        "modern", "minimal", "rounded", "premium", "space grotesk" -> SpaceGroteskFontFamily
        else -> SpaceGroteskFontFamily
    }
}

fun buildCyberTypography(fontPreset: String = "Cyber", scale: Float = 1.0f): Typography {
    val displayFamily = when (fontPreset) {
        "Cyber", "Gaming", "Bold" -> OrbitronFontFamily
        "Monospace" -> ShareTechMonoFontFamily
        "Handwriting" -> PacificoFontFamily
        "Elegant", "Premium" -> PlayfairDisplayFontFamily
        else -> RajdhaniFontFamily
    }
    val bodyFamily = resolveFontFamilyByName(fontPreset)

    return Typography(
        displayLarge = TextStyle(
            fontFamily = displayFamily,
            fontWeight = FontWeight.Bold,
            fontSize = (36 * scale).sp,
            lineHeight = (44 * scale).sp,
            letterSpacing = 0.5.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = displayFamily,
            fontWeight = FontWeight.Bold,
            fontSize = (28 * scale).sp,
            lineHeight = (34 * scale).sp,
            letterSpacing = 0.4.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = displayFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = (22 * scale).sp,
            lineHeight = (28 * scale).sp,
            letterSpacing = 0.3.sp
        ),
        titleLarge = TextStyle(
            fontFamily = displayFamily,
            fontWeight = FontWeight.Bold,
            fontSize = (19 * scale).sp,
            lineHeight = (25 * scale).sp,
            letterSpacing = 0.25.sp
        ),
        titleMedium = TextStyle(
            fontFamily = bodyFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = (16 * scale).sp,
            lineHeight = (22 * scale).sp,
            letterSpacing = 0.15.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = bodyFamily,
            fontWeight = FontWeight.Normal,
            fontSize = (15 * scale).sp,
            lineHeight = (22 * scale).sp,
            letterSpacing = 0.2.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = bodyFamily,
            fontWeight = FontWeight.Normal,
            fontSize = (13 * scale).sp,
            lineHeight = (19 * scale).sp,
            letterSpacing = 0.2.sp
        ),
        labelLarge = TextStyle(
            fontFamily = ShareTechMonoFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = (13 * scale).sp,
            lineHeight = (18 * scale).sp,
            letterSpacing = 0.6.sp
        ),
        labelSmall = TextStyle(
            fontFamily = ShareTechMonoFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = (11 * scale).sp,
            lineHeight = (15 * scale).sp,
            letterSpacing = 0.5.sp
        )
    )
}

val Typography = buildCyberTypography()
