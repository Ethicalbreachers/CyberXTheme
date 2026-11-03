package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CyberCatalogData
import com.example.engine.WallpaperAndWidgetRenderer
import com.example.ui.CyberThemeViewModel
import com.example.ui.CyberUiState
import com.example.ui.components.CyberGlassCard
import com.example.ui.components.CyberSectionHeader
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.RainbowCyberColors
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.StatusCompatible
import com.example.ui.theme.StatusRestricted
import com.example.ui.theme.StatusUserAction
import com.example.ui.theme.parseHexColor
import com.example.ui.theme.resolveFontFamilyByName

// ============================================================================
// 15. FONT STUDIO
// ============================================================================
@Composable
fun FontStudioSection(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel
) {
    val prefs = uiState.prefs
    var selectedFont by remember(prefs.activeFontStyle) { mutableStateOf(prefs.activeFontStyle) }
    var fontScale by remember(prefs.fontScale) { mutableFloatStateOf(prefs.fontScale) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("font_studio_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CyberSectionHeader(
                title = "FONT & TYPOGRAPHY STUDIO",
                subtitle = "10 Bundled Google Font Styles • Size Scale & Launcher/Widget Sync",
                badgeText = selectedFont
            )
            Spacer(modifier = Modifier.height(8.dp))
            CyberGlassCard(accentColor = NeonGreen) {
                val previewFamily = resolveFontFamilyByName(selectedFont)
                Text(
                    text = "CYBER THEME X // 09:41",
                    fontFamily = previewFamily,
                    fontSize = (28 * fontScale).sp,
                    color = NeonGreen,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "The quick brown cyber fox jumps over the neon matrix grid 0123456789.",
                    fontFamily = previewFamily,
                    fontSize = (15 * fontScale).sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("Typography Scale: ${(fontScale * 100).toInt()}%", color = Color.White)
                Slider(value = fontScale, onValueChange = { fontScale = it }, valueRange = 0.85f..1.25f)

                Button(
                    onClick = { viewModel.updateFontPreferences(selectedFont, fontScale) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                ) {
                    Text("Apply '$selectedFont' to App, Launcher & Widgets", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Vivo Funtouch OS Note: Fonts apply immediately to Cyber Theme X, Cyber Launcher, and custom widgets. System-wide OEM font replacement requires Vivo iTheme font store packages.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(CyberCatalogData.fontStyles) { style ->
            val fam = resolveFontFamilyByName(style)
            val isSelected = selectedFont == style
            CyberGlassCard(
                accentColor = if (isSelected) NeonGreen else ElectricCyan,
                onClick = {
                    selectedFont = style
                    viewModel.updateFontPreferences(style, fontScale)
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "$style Typography",
                            fontFamily = fam,
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "0123456789 • VIVO T2X 5G HUD",
                            fontFamily = fam,
                            fontSize = 13.sp,
                            color = if (isSelected) NeonGreen else Color.White.copy(alpha = 0.7f)
                        )
                    }
                    Text(
                        text = if (isSelected) "ACTIVE" else "APPLY",
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonGreen
                    )
                }
            }
        }
    }
}

// ============================================================================
// 16. RGB & EDGE LIGHTING + 17. CHARGING ANIMATION STUDIO
// ============================================================================
@Composable
fun RgbAndChargingStudioSection(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel,
    initialMode: String = "RGB" // "RGB" or "CHARGING"
) {
    val context = LocalContext.current
    val prefs = uiState.prefs
    var tabMode by remember(initialMode) { mutableStateOf(initialMode) }

    var rgbEnabled by remember(prefs.rgbEdgeEnabled) { mutableStateOf(prefs.rgbEdgeEnabled) }
    var rgbPreset by remember(prefs.rgbEdgePreset) { mutableStateOf(prefs.rgbEdgePreset) }
    var rgbSpeed by remember(prefs.rgbEdgeSpeed) { mutableFloatStateOf(prefs.rgbEdgeSpeed) }
    var rgbThickness by remember(prefs.rgbEdgeThicknessDp) { mutableFloatStateOf(prefs.rgbEdgeThicknessDp) }

    var selectedChargingAnim by remember(prefs.activeChargingAnimation) {
        mutableStateOf(prefs.activeChargingAnimation)
    }
    val (batteryPct, isCharging) = remember { WallpaperAndWidgetRenderer.getRealBatteryTelemetry(context) }

    val infiniteTransition = rememberInfiniteTransition(label = "charging_pulse")
    val pulseAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_angle"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = tabMode == "RGB",
                    onClick = { tabMode = "RGB" },
                    label = { Text("RGB & Edge Lighting (12)") }
                )
                FilterChip(
                    selected = tabMode == "CHARGING",
                    onClick = { tabMode = "CHARGING" },
                    label = { Text("Charging Studio (10)") }
                )
            }
        }

        if (tabMode == "RGB") {
            item {
                CyberSectionHeader(
                    title = "RGB & EDGE LIGHTING STUDIO",
                    subtitle = "12 Chroma Presets • Speed, Thickness & Battery-Friendly Rendering",
                    badgeText = if (rgbEnabled) "ACTIVE" else "PAUSED"
                )
                Spacer(modifier = Modifier.height(8.dp))
                CyberGlassCard(accentColor = NeonGreen) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Enable Full-Screen RGB Edge Border",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Switch(
                            checked = rgbEnabled,
                            onCheckedChange = {
                                rgbEnabled = it
                                viewModel.updateRgbEdgePreferences(it, rgbPreset, rgbSpeed, rgbThickness)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Animation Speed: ${String.format("%.1fx", rgbSpeed)}", color = Color.White)
                    Slider(
                        value = rgbSpeed,
                        onValueChange = { rgbSpeed = it },
                        onValueChangeFinished = {
                            viewModel.updateRgbEdgePreferences(rgbEnabled, rgbPreset, rgbSpeed, rgbThickness)
                        },
                        valueRange = 0.5f..2.5f
                    )

                    Text("Border Thickness: ${String.format("%.1f dp", rgbThickness)}", color = Color.White)
                    Slider(
                        value = rgbThickness,
                        onValueChange = { rgbThickness = it },
                        onValueChangeFinished = {
                            viewModel.updateRgbEdgePreferences(rgbEnabled, rgbPreset, rgbSpeed, rgbThickness)
                        },
                        valueRange = 1.5f..8.0f
                    )
                }
            }

            items(CyberCatalogData.rgbEdgePresets) { preset ->
                val isSelected = rgbPreset == preset
                CyberGlassCard(
                    accentColor = if (isSelected) NeonGreen else ElectricCyan,
                    onClick = {
                        rgbPreset = preset
                        rgbEnabled = true
                        viewModel.updateRgbEdgePreferences(true, preset, rgbSpeed, rgbThickness)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(preset, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(if (isSelected) "ACTIVE PRESET" else "ACTIVATE", color = NeonGreen, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        } else {
            // 17. CHARGING ANIMATION STUDIO
            item {
                CyberSectionHeader(
                    title = "CHARGING ANIMATION STUDIO",
                    subtitle = "10 Live Power Reactor Designs • Real BatteryManager Telemetry",
                    badgeText = selectedChargingAnim
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Live Animated Charging Reactor Canvas
                CyberGlassCard(accentColor = NeonGreen) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(175.dp)) {
                            val strokePx = 14.dp.toPx()
                            drawCircle(
                                color = Color.White.copy(alpha = 0.1f),
                                style = Stroke(width = strokePx)
                            )
                            drawArc(
                                brush = Brush.sweepGradient(RainbowCyberColors),
                                startAngle = pulseAngle,
                                sweepAngle = (batteryPct / 100f) * 320f + 25f,
                                useCenter = false,
                                style = Stroke(width = strokePx, cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.BatteryChargingFull, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(28.dp))
                            Text(
                                text = "$batteryPct%",
                                style = MaterialTheme.typography.displayLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isCharging) "CHARGING ACTIVE ⚡" else "STANDBY PREVIEW",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonGreen
                            )
                            Text(
                                text = selectedChargingAnim,
                                style = MaterialTheme.typography.labelSmall,
                                color = ElectricCyan
                            )
                        }
                    }
                }
            }

            items(CyberCatalogData.chargingAnimations) { anim ->
                val isSelected = selectedChargingAnim == anim
                CyberGlassCard(
                    accentColor = if (isSelected) NeonGreen else ElectricCyan,
                    onClick = {
                        selectedChargingAnim = anim
                        viewModel.selectChargingAnimation(anim)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(anim, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(if (isSelected) "ACTIVE" else "SELECT", color = NeonGreen, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

// ============================================================================
// 18. SOUND STUDIO & 19. ANIMATION STUDIO
// ============================================================================
@Composable
fun SoundAndAnimationStudioSection(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel,
    initialMode: String = "SOUNDS" // "SOUNDS" or "ANIMATIONS"
) {
    val prefs = uiState.prefs
    var tabMode by remember(initialMode) { mutableStateOf(initialMode) }
    var selectedSoundPack by remember(prefs.activeSoundPack) { mutableStateOf(prefs.activeSoundPack) }
    var selectedAnimPreset by remember(prefs.activeAnimationPreset) { mutableStateOf(prefs.activeAnimationPreset) }
    var animSpeed by remember(prefs.animationSpeedMultiplier) { mutableFloatStateOf(prefs.animationSpeedMultiplier) }
    var reduceMotion by remember(prefs.reduceMotion) { mutableStateOf(prefs.reduceMotion) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = tabMode == "SOUNDS",
                    onClick = { tabMode = "SOUNDS" },
                    label = { Text("Sound Studio (10 Packs)") }
                )
                FilterChip(
                    selected = tabMode == "ANIMATIONS",
                    onClick = { tabMode = "ANIMATIONS" },
                    label = { Text("Animation Studio (10)") }
                )
            }
        }

        if (tabMode == "SOUNDS") {
            item {
                CyberSectionHeader(
                    title = "CYBER SOUND STUDIO",
                    subtitle = "10 Original Synthesized Audio Packs • Ringtone, Notification, Alarm & UI Player",
                    badgeText = selectedSoundPack
                )
                Spacer(modifier = Modifier.height(8.dp))
                CyberGlassCard(accentColor = NeonGreen) {
                    Text(
                        text = "Interactive Audio Synthesizer ($selectedSoundPack)",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Tap any tone channel below to synthesize and play real PCM audio via Android AudioTrack.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Ringtone", "Notification", "UI Click").forEach { role ->
                            Button(
                                onClick = { viewModel.selectAndPlaySoundPack(selectedSoundPack, role) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(role, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { viewModel.openSystemPermissionScreen("SOUND_SETTINGS") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = ElectricCyan)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Open Android System Sound Settings", color = Color.White)
                    }
                }
            }

            items(CyberCatalogData.soundPacks) { pack ->
                val isSelected = selectedSoundPack == pack
                CyberGlassCard(
                    accentColor = if (isSelected) NeonGreen else ElectricCyan,
                    onClick = {
                        selectedSoundPack = pack
                        viewModel.selectAndPlaySoundPack(pack, "Ringtone")
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(pack, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Includes Ringtone, Notification, Alarm & UI FX", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.65f))
                        }
                        Icon(Icons.Default.PlayArrow, contentDescription = "Play Preview", tint = NeonGreen)
                    }
                }
            }
        } else {
            // 19. ANIMATION STUDIO
            item {
                CyberSectionHeader(
                    title = "ANIMATION & MOTION STUDIO",
                    subtitle = "10 Launcher/Widget Physics Presets • Speed Control & Reduce Motion",
                    badgeText = selectedAnimPreset
                )
                Spacer(modifier = Modifier.height(8.dp))
                CyberGlassCard(accentColor = ElectricCyan) {
                    Text(
                        text = "Animation Speed Multiplier: ${String.format("%.1fx", animSpeed)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Slider(
                        value = animSpeed,
                        onValueChange = { animSpeed = it },
                        onValueChangeFinished = {
                            viewModel.updateAnimationPreferences(selectedAnimPreset, animSpeed, reduceMotion)
                        },
                        valueRange = 0.5f..2.0f
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Reduce Motion Mode", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Minimizes continuous shaders and page transitions to save battery", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                        }
                        Switch(
                            checked = reduceMotion,
                            onCheckedChange = {
                                reduceMotion = it
                                viewModel.updateAnimationPreferences(selectedAnimPreset, animSpeed, it)
                            }
                        )
                    }
                }
            }

            items(CyberCatalogData.animationPresets) { preset ->
                val isSelected = selectedAnimPreset == preset
                CyberGlassCard(
                    accentColor = if (isSelected) NeonGreen else ElectricCyan,
                    onClick = {
                        selectedAnimPreset = preset
                        viewModel.updateAnimationPreferences(preset, animSpeed, reduceMotion)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(preset, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(if (isSelected) "ACTIVE" else "SELECT", color = NeonGreen, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

// ============================================================================
// 20. THEME CREATOR STUDIO
// ============================================================================
@Composable
fun ThemeCreatorStudioSection(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel
) {
    var name by remember { mutableStateOf("Vivo Cyber Custom X") }
    var category by remember { mutableStateOf("Cyber Hacker") }
    var primaryHex by remember { mutableStateOf("#00FF66") }
    var secondaryHex by remember { mutableStateOf("#00E5FF") }
    var wallpaperId by remember { mutableStateOf("wp_hacker_matrix") }
    var iconPack by remember { mutableStateOf("Cyber Hacker") }
    var clockStyle by remember { mutableStateOf("Cyber Clock") }
    var batteryStyle by remember { mutableStateOf("Neon Battery") }
    var controlCenter by remember { mutableStateOf("Cyber Hacker") }
    var fontStyle by remember { mutableStateOf("Cyber") }
    var dockStyle by remember { mutableStateOf("Neon Border Floating Dock") }
    var folderStyle by remember { mutableStateOf("3x3 Matrix Hex Folder") }
    var importJsonText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("theme_creator_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            CyberSectionHeader(
                title = "COMPLETE THEME CREATOR",
                subtitle = "Combine Wallpaper, Icons, Widgets, Control Center, Fonts & Colors",
                badgeText = "Builder + JSON"
            )
        }

        item {
            CyberGlassCard(accentColor = parseHexColor(primaryHex, NeonGreen)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Custom Theme Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("1. Primary & Secondary Accent Colors", color = Color.White, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("#00FF66", "#00E5FF", "#FF007F", "#FFD700", "#B388FF", "#D71921", "#FFFFFF").forEach { hex ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(parseHexColor(hex))
                                .border(if (primaryHex == hex) 3.dp else 0.dp, Color.White, CircleShape)
                                .clickable { primaryHex = hex }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("2. Select Icon Pack", color = Color.White, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(CyberCatalogData.iconPacks) { p ->
                        FilterChip(selected = iconPack == p, onClick = { iconPack = p }, label = { Text(p) })
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("3. Select Clock & Battery Widgets", color = Color.White, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(CyberCatalogData.widgetStudioCatalog.filter { it.category == "Clock" }) { w ->
                        FilterChip(selected = clockStyle == w.title, onClick = { clockStyle = w.title }, label = { Text(w.title) })
                    }
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(CyberCatalogData.widgetStudioCatalog.filter { it.category == "Battery" }) { w ->
                        FilterChip(selected = batteryStyle == w.title, onClick = { batteryStyle = w.title }, label = { Text(w.title) })
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("4. Select Control Center & Font", color = Color.White, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(CyberCatalogData.controlCenterThemes) { cc ->
                        FilterChip(selected = controlCenter == cc, onClick = { controlCenter = cc }, label = { Text(cc) })
                    }
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(CyberCatalogData.fontStyles) { f ->
                        FilterChip(selected = fontStyle == f, onClick = { fontStyle = f }, label = { Text(f) })
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.saveUserCreatedTheme(
                                name = name,
                                category = category,
                                primaryHex = primaryHex,
                                secondaryHex = secondaryHex,
                                wallpaperId = wallpaperId,
                                iconPack = iconPack,
                                clockStyle = clockStyle,
                                batteryStyle = batteryStyle,
                                controlCenterStyle = controlCenter,
                                fontStyle = fontStyle,
                                dockStyle = dockStyle,
                                folderStyle = folderStyle
                            )
                        },
                        modifier = Modifier.weight(1.3f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = parseHexColor(primaryHex, NeonGreen),
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Save Custom Theme", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { viewModel.shareTheme(uiState.activeTheme) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Export JSON", color = Color.White)
                    }
                }
            }
        }

        // Structured Theme JSON Validator & Importer
        item {
            CyberGlassCard(accentColor = ElectricCyan) {
                Text(
                    text = "Import & Validate Theme File (.cybertheme.json)",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = importJsonText,
                    onValueChange = { importJsonText = it },
                    placeholder = { Text("Paste CYBER_THEME_X_V1 JSON payload here...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { importJsonText = viewModel.exportThemeToJson(uiState.activeTheme) }
                    ) {
                        Text("Load Active Theme JSON", color = ElectricCyan)
                    }
                    Button(
                        onClick = { viewModel.importAndValidateThemeJson(importJsonText) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                    ) {
                        Text("Validate & Import", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ============================================================================
// 22. DEVICE COMPATIBILITY CENTER (VIVO T2X 5G DASHBOARD)
// ============================================================================
@Composable
fun DeviceCompatibilityCenterSection(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel
) {
    val audit = uiState.compatibilityAudit

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("compatibility_center_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "VIVO T2X 5G COMPATIBILITY CENTER",
                        style = MaterialTheme.typography.headlineMedium,
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Transparent Android 15 & Funtouch OS Capability Verification",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                OutlinedButton(onClick = { viewModel.refreshDeviceAudit() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh Audit", tint = NeonGreen)
                }
            }
        }

        // Device Hardware & OS Telemetry Card
        item {
            CyberGlassCard(accentColor = NeonGreen) {
                Text("Hardware & Firmware Profile", style = MaterialTheme.typography.titleMedium, color = NeonGreen, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                val rows = listOf(
                    "Target Device Profile" to (audit?.modelName ?: "Vivo T2x 5G (Dimensity 6020)"),
                    "Android SDK Version" to (audit?.androidVersionLabel ?: "Android 15 (API 35)"),
                    "OS Layer" to (audit?.funtouchOsLabel ?: "Funtouch OS 15"),
                    "WallpaperManager API" to if (audit?.isWallpaperSupported != false) "Supported & Ready" else "Restricted",
                    "AppWidgetManager Pinning" to if (audit?.isAppWidgetPinSupported != false) "Supported (2x2, 4x2, 4x4)" else "Use OS Picker",
                    "Default Home Launcher Role" to if (audit?.isDefaultLauncher == true) "CYBER LAUNCHER Active" else "Stock Launcher Active (Optional)",
                    "Notification Listener" to if (audit?.isNotificationAccessEnabled == true) "Granted (Privacy-Safe)" else "Optional Opt-In"
                )
                rows.forEach { (k, v) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(k, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.75f))
                        Text(v, style = MaterialTheme.typography.labelLarge, color = NeonGreen)
                    }
                }
            }
        }

        // Fully Supported Native Features
        item {
            CyberGlassCard(accentColor = StatusCompatible) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusCompatible)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Supported Native Android & Vivo Features", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                (audit?.supportedFeatures ?: emptyList()).forEach { item ->
                    Text("• $item", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.88f), modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }

        // Features Requiring User Action
        item {
            CyberGlassCard(accentColor = StatusUserAction) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = StatusUserAction)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Optional Setup & User-Action Features", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                (audit?.userActionFeatures ?: emptyList()).forEach { item ->
                    Text("• $item", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.88f), modifier = Modifier.padding(vertical = 2.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { viewModel.openSystemPermissionScreen("DEFAULT_HOME_SETTINGS") },
                        colors = ButtonDefaults.buttonColors(containerColor = StatusUserAction, contentColor = Color.Black)
                    ) {
                        Text("Default Home Setup", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(onClick = { viewModel.openSystemPermissionScreen("NOTIFICATION_LISTENER") }) {
                        Text("Notification Access", color = Color.White)
                    }
                }
            }
        }

        // Honest Vivo Funtouch OS / Android 15 Boundaries
        item {
            CyberGlassCard(accentColor = StatusRestricted) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = StatusRestricted)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("OS-Protected System Boundaries (Honest Reporting)", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                (audit?.vivoRestrictedFeatures ?: emptyList()).forEach { item ->
                    Text("• $item", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.85f), modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }
    }
}
