package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val AmoledBlack = Color(0xFF000000)
val CyberObsidian = Color(0xFF070A0E)
val CyberSurface = Color(0xFF10161F)
val CyberSurfaceElevated = Color(0xFF17202C)
val CyberGlassWhite = Color(0x1FFFFFFF)
val CyberBorderSubtle = Color(0xFF1E293B)

val NeonGreen = Color(0xFF00FF66)
val NeonGreenDim = Color(0xFF00C853)
val ElectricCyan = Color(0xFF00E5FF)
val NeonPurple = Color(0xFFB388FF)
val CyberMagenta = Color(0xFFFF007F)
val RoyalGold = Color(0xFFFFD700)
val NothingRed = Color(0xFFD71921)
val IosBlue = Color(0xFF0A84FF)

val StatusCompatible = Color(0xFF00E676)
val StatusUserAction = Color(0xFFFFAB00)
val StatusRestricted = Color(0xFFFF5252)

val TextPrimaryDark = Color(0xFFF8FAFC)
val TextSecondaryDark = Color(0xFF94A3B8)
val TextPrimaryLight = Color(0xFF0F172A)
val TextSecondaryLight = Color(0xFF475569)

val RainbowCyberColors = listOf(
    Color(0xFF00FF66),
    Color(0xFF00E5FF),
    Color(0xFF7C4DFF),
    Color(0xFFFF007F),
    Color(0xFFFFD600),
    Color(0xFF00FF66)
)

val RainbowGradientBrush = Brush.linearGradient(colors = RainbowCyberColors)

fun parseHexColor(hex: String, fallback: Color = NeonGreen): Color {
    return try {
        val clean = hex.trim().removePrefix("#")
        val value = when (clean.length) {
            6 -> ("FF$clean").toLong(16)
            8 -> clean.toLong(16)
            else -> return fallback
        }
        Color(value)
    } catch (_: Exception) {
        fallback
    }
}
