package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.ApplyComponentStatus
import com.example.data.ThemeEntity
import com.example.ui.OneClickApplyReportState
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.RainbowCyberColors
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.StatusCompatible
import com.example.ui.theme.StatusRestricted
import com.example.ui.theme.StatusUserAction
import com.example.ui.theme.parseHexColor

@Composable
fun CyberGlassCard(
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.primary,
    borderAlpha: Float = 0.38f,
    cornerRadius: Dp = 20.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val baseModifier = modifier
        .clip(shape)
        .background(
            Brush.verticalGradient(
                listOf(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.78f)
                )
            )
        )
        .border(
            width = 1.dp,
            brush = Brush.linearGradient(
                listOf(
                    accentColor.copy(alpha = borderAlpha),
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                    accentColor.copy(alpha = borderAlpha * 0.6f)
                )
            ),
            shape = shape
        )
        .let { mod ->
            if (onClick != null) mod.clickable { onClick() } else mod
        }
        .padding(16.dp)

    Column(modifier = baseModifier, content = content)
}

@Composable
fun CyberSectionHeader(
    title: String,
    subtitle: String? = null,
    badgeText: String? = null,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (!badgeText.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(accentColor.copy(alpha = 0.16f))
                    .border(1.dp, accentColor.copy(alpha = 0.65f), RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun resolveIconShape(shapeName: String): Shape {
    return when (shapeName) {
        "Square" -> RoundedCornerShape(6.dp)
        "Circular" -> CircleShape
        "Squircle" -> RoundedCornerShape(34)
        "Hexagon" -> CutCornerShape(28)
        else -> RoundedCornerShape(16.dp)
    }
}

@Composable
fun InteractivePhoneThemePreview(
    theme: ThemeEntity,
    iconShapeName: String = "Rounded",
    modifier: Modifier = Modifier,
    onApplyClick: (() -> Unit)? = null
) {
    val primary = parseHexColor(theme.primaryColorHex, NeonGreen)
    val secondary = parseHexColor(theme.secondaryColorHex, ElectricCyan)
    val bg = parseHexColor(theme.backgroundColorHex, Color(0xFF04080C))
    val iconShape = resolveIconShape(iconShapeName)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.linearGradient(
                    listOf(bg, primary.copy(alpha = 0.18f), secondary.copy(alpha = 0.14f), bg)
                )
            )
            .border(2.dp, Brush.linearGradient(listOf(primary, secondary)), RoundedCornerShape(26.dp))
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Simulated Status Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "09:41 • VIVO 5G",
                    style = MaterialTheme.typography.labelSmall,
                    color = primary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = theme.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Icon(
                        imageVector = Icons.Default.BatteryChargingFull,
                        contentDescription = null,
                        tint = primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "88%",
                        style = MaterialTheme.typography.labelSmall,
                        color = primary
                    )
                }
            }

            // Simulated Clock + Battery Widget Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1.3f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .border(1.dp, primary.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "09:41",
                            style = MaterialTheme.typography.headlineMedium,
                            color = primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = theme.clockWidgetStyle,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.75f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                        .border(1.dp, secondary.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "88% ⚡",
                            style = MaterialTheme.typography.titleLarge,
                            color = secondary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = theme.batteryWidgetStyle,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.75f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Simulated Music + Notification Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.07f))
                    .border(1.dp, primary.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = null,
                        tint = primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "${theme.musicWidgetStyle} • ${theme.notificationStyle}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Control Center: ${theme.controlCenterStyle} • Font: ${theme.fontFamilyName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.65f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Simulated Custom Dock with Icon Pack
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(alpha = 0.68f))
                    .border(1.dp, primary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val sampleIcons = listOf(
                    Icons.Default.Phone to "Dialer",
                    Icons.Default.Terminal to "Cyber",
                    Icons.Default.PhotoCamera to "Camera",
                    Icons.Default.Settings to "Setup"
                )
                sampleIcons.forEachIndexed { idx, (iconVec, label) ->
                    val tint = if (idx % 2 == 0) primary else secondary
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(iconShape)
                                .background(tint.copy(alpha = 0.18f))
                                .border(1.5.dp, tint, iconShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = iconVec,
                                contentDescription = label,
                                tint = tint,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            if (onApplyClick != null) {
                Button(
                    onClick = onApplyClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("preview_apply_theme_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primary,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "ONE-CLICK APPLY • ${theme.name.uppercase()}",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun RgbEdgeLightingOverlay(
    enabled: Boolean,
    presetName: String,
    speedMultiplier: Float,
    thicknessDp: Float,
    reduceMotion: Boolean
) {
    if (!enabled) return

    val colors = when {
        presetName.contains("Matrix", ignoreCase = true) -> listOf(NeonGreen, Color(0xFF004D20), NeonGreen)
        presetName.contains("Cyan-Magenta", ignoreCase = true) -> listOf(ElectricCyan, Color(0xFFFF007F), ElectricCyan)
        presetName.contains("Nothing", ignoreCase = true) -> listOf(Color(0xFFD71921), Color.White, Color(0xFFD71921))
        presetName.contains("Ultraviolet", ignoreCase = true) -> listOf(NeonPurple, Color(0xFF6200EA), NeonPurple)
        presetName.contains("Gold", ignoreCase = true) -> listOf(RoyalGold, Color(0xFFFF8F00), RoyalGold)
        else -> RainbowCyberColors
    }

    val durationMs = ((3200f / speedMultiplier.coerceIn(0.4f, 3.0f)).toInt()).coerceAtLeast(800)
    val transition = rememberInfiniteTransition(label = "rgb_edge_transition")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (reduceMotion) 0f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rgb_phase"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokePx = thicknessDp.dp.toPx().coerceIn(2f, 24f)
        val brush = Brush.linearGradient(
            colors = colors,
            start = Offset(size.width * phase, 0f),
            end = Offset(size.width * (1f - phase), size.height)
        )
        drawRoundRect(
            brush = brush,
            topLeft = Offset(strokePx / 2f, strokePx / 2f),
            size = androidx.compose.ui.geometry.Size(
                width = (size.width - strokePx).coerceAtLeast(10f),
                height = (size.height - strokePx).coerceAtLeast(10f)
            ),
            cornerRadius = CornerRadius(28.dp.toPx(), 28.dp.toPx()),
            style = Stroke(width = strokePx)
        )
    }
}

@Composable
fun OneClickApplyReportModal(
    report: OneClickApplyReportState,
    onDismiss: () -> Unit,
    onOpenLauncher: () -> Unit,
    onOpenCompatibilityCenter: () -> Unit
) {
    if (!report.isVisible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0A1018),
        titleContentColor = NeonGreen,
        textContentColor = Color.White,
        title = {
            Text(
                text = "ONE-CLICK APPLY ENGINE • ${report.themeName}",
                style = MaterialTheme.typography.titleLarge,
                color = NeonGreen
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = report.summaryBanner,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )

                if (report.isApplying) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NeonGreen)
                    }
                } else {
                    report.steps.forEach { step ->
                        val (badgeColor, iconVec, statusLabel) = when (step.status) {
                            ApplyComponentStatus.APPLIED_SYSTEM -> Triple(StatusCompatible, Icons.Default.CheckCircle, "APPLIED TO OS")
                            ApplyComponentStatus.APPLIED_LAUNCHER_AND_APP -> Triple(ElectricCyan, Icons.Default.CheckCircle, "APPLIED IN SUITE")
                            ApplyComponentStatus.REQUIRES_USER_ACTION -> Triple(StatusUserAction, Icons.Default.Warning, "OPTIONAL ACTION")
                            ApplyComponentStatus.OS_RESTRICTED_PREVIEW -> Triple(StatusRestricted, Icons.Default.Info, "VIVO OS BOUNDARY")
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .border(1.dp, badgeColor.copy(alpha = 0.55f), RoundedCornerShape(14.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = iconVec,
                                        contentDescription = null,
                                        tint = badgeColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = step.componentName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = statusLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = badgeColor
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = step.detailMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.78f)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = {
                    onDismiss()
                    onOpenCompatibilityCenter()
                }) {
                    Text("Vivo Audit", color = ElectricCyan)
                }
                Button(
                    onClick = {
                        onDismiss()
                        onOpenLauncher()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonGreen,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Open Cyber Launcher", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("close_apply_report_btn")
            ) {
                Text("Done", color = Color.White)
            }
        }
    )
}
