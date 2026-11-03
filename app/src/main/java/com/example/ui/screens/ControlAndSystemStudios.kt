package com.example.ui.screens

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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.CyberCatalogData
import com.example.ui.CyberThemeViewModel
import com.example.ui.CyberUiState
import com.example.ui.components.CyberGlassCard
import com.example.ui.components.CyberSectionHeader
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.StatusCompatible
import com.example.ui.theme.parseHexColor
import com.example.widgets.CyberNotificationListenerService

// ============================================================================
// 10. CONTROL CENTER STUDIO
// ============================================================================
@Composable
fun ControlCenterStudioSection(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel
) {
    val prefs = uiState.prefs
    var selectedTheme by remember(prefs.activeControlCenterTheme) { mutableStateOf(prefs.activeControlCenterTheme) }
    var blurRadius by remember(prefs.controlCenterBlur) { mutableFloatStateOf(prefs.controlCenterBlur) }
    var panelAlpha by remember(prefs.controlCenterTransparency) { mutableFloatStateOf(prefs.controlCenterTransparency) }
    var brightnessSlider by remember { mutableFloatStateOf(0.82f) }
    var volumeSlider by remember { mutableFloatStateOf(0.70f) }

    val themeAccent = when {
        selectedTheme.contains("iOS", ignoreCase = true) -> Color(0xFF0A84FF)
        selectedTheme.contains("RGB", ignoreCase = true) -> Color(0xFFFF007F)
        selectedTheme.contains("Nothing", ignoreCase = true) -> Color(0xFFD71921)
        selectedTheme.contains("Purple", ignoreCase = true) -> NeonPurple
        selectedTheme.contains("Blue", ignoreCase = true) -> ElectricCyan
        else -> NeonGreen
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("control_center_studio_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CyberSectionHeader(
                title = "CONTROL CENTER STUDIO",
                subtitle = "10 Themes • Interactive Hardware Torch, Connectivity Panels & Tile Editor",
                badgeText = selectedTheme,
                accentColor = themeAccent
            )
            Text(
                text = "Vivo Funtouch OS Compatibility: Hardware Flashlight toggles directly; Wi-Fi, Mobile Data, Bluetooth, and System tiles open real Android Settings Panels. Native OEM notification shade replacement is protected by Android OS security.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 10 Control Center Theme Presets
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CyberCatalogData.controlCenterThemes) { t ->
                    FilterChip(
                        selected = selectedTheme == t,
                        onClick = {
                            selectedTheme = t
                            viewModel.updateControlCenterSettings(t, blurRadius, panelAlpha)
                        },
                        label = { Text(t) }
                    )
                }
            }
        }

        // Interactive Functional Control Center Deck
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF080E17).copy(alpha = panelAlpha.coerceIn(0.4f, 0.98f)))
                    .border(2.dp, themeAccent.copy(alpha = 0.75f), RoundedCornerShape(28.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Top Date, Time & Battery / 5G Status Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "09:41 • MON NOV 2",
                                style = MaterialTheme.typography.titleMedium,
                                color = themeAccent,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "CONTROL DECK // $selectedTheme",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Wifi, contentDescription = null, tint = themeAccent, modifier = Modifier.size(18.dp))
                            Icon(Icons.Default.BatteryChargingFull, contentDescription = null, tint = themeAccent, modifier = Modifier.size(18.dp))
                            Text("88%", style = MaterialTheme.typography.labelLarge, color = Color.White)
                        }
                    }

                    // Connectivity + Media Player Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Network Card
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.07f))
                                .border(1.dp, themeAccent.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.controlCenterTiles.take(2).forEach { tile ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { viewModel.toggleControlCenterTile(tile.id) },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(tile.label, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                        Text(tile.subtitle, color = themeAccent, style = MaterialTheme.typography.labelSmall)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(if (tile.isActive) themeAccent else Color.Gray)
                                    )
                                }
                            }
                        }

                        // Music Player Card
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.07f))
                                .border(1.dp, themeAccent.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.MusicNote, contentDescription = null, tint = themeAccent, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Cyber Synth FM", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("⏮   ▶ PLAY   ⏭", color = themeAccent, style = MaterialTheme.typography.labelLarge)
                        }
                    }

                    // Brightness & Volume Sliders
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.06f))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text("Display Brightness: ${(brightnessSlider * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = Color.White)
                        Slider(value = brightnessSlider, onValueChange = { brightnessSlider = it })
                        Text("Media Volume: ${(volumeSlider * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = Color.White)
                        Slider(value = volumeSlider, onValueChange = { volumeSlider = it })
                    }

                    // Interactive Quick Settings Tiles Grid + Reorder Editor
                    Text(
                        text = "Interactive Quick Tiles (Tap to toggle • Arrows to reorder layout)",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    uiState.controlCenterTiles.forEachIndexed { idx, tile ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (tile.isActive) themeAccent.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f))
                                .border(
                                    width = 1.dp,
                                    color = if (tile.isActive) themeAccent else Color.White.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clickable { viewModel.toggleControlCenterTile(tile.id) }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FlashlightOn,
                                    contentDescription = null,
                                    tint = if (tile.isActive) themeAccent else Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = tile.label,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${tile.subtitle} • ${if (tile.isActive) "ACTIVE" else "STANDBY"}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (tile.isActive) themeAccent else Color.White.copy(alpha = 0.6f)
                                    )
                                }
                            }
                            Row {
                                IconButton(
                                    onClick = { viewModel.moveControlCenterTile(idx, -1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.ArrowUpward, contentDescription = "Move Up", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                                IconButton(
                                    onClick = { viewModel.moveControlCenterTile(idx, 1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.ArrowDownward, contentDescription = "Move Down", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    // Panel Blur & Transparency Customization
                    Text("Panel Blur: ${blurRadius.toInt()}px • Opacity: ${(panelAlpha * 100).toInt()}%", color = Color.White)
                    Slider(
                        value = panelAlpha,
                        onValueChange = {
                            panelAlpha = it
                            viewModel.updateControlCenterSettings(selectedTheme, blurRadius, panelAlpha)
                        },
                        valueRange = 0.45f..0.98f
                    )
                }
            }
        }
    }
}

// ============================================================================
// 11, 12, 13, 14: NOTIFICATION, STATUS BAR, LOCK SCREEN & DIALER STUDIOS
// ============================================================================
@Composable
fun NotificationAndStatusBarStudioSection(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel,
    initialMode: String = "NOTIFICATIONS" // "NOTIFICATIONS" or "STATUS_BAR"
) {
    val activeNotifCount by CyberNotificationListenerService.activeCount.collectAsStateWithLifecycle()
    var studioTab by remember(initialMode) { mutableStateOf(initialMode) }
    var selectedNotifStyle by remember(uiState.prefs.activeNotificationStyle) {
        mutableStateOf(uiState.prefs.activeNotificationStyle)
    }
    var selectedStatusBarStyle by remember(uiState.prefs.activeStatusBarStyle) {
        mutableStateOf(uiState.prefs.activeStatusBarStyle)
    }
    var cornerRadiusDp by remember { mutableFloatStateOf(20f) }
    var showBatteryPct by remember { mutableStateOf(true) }
    var showNetworkSpeed by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = studioTab == "NOTIFICATIONS",
                    onClick = { studioTab = "NOTIFICATIONS" },
                    label = { Text("Notification Studio (10)") }
                )
                FilterChip(
                    selected = studioTab == "STATUS_BAR",
                    onClick = { studioTab = "STATUS_BAR" },
                    label = { Text("Status Bar Studio (10)") }
                )
            }
        }

        if (studioTab == "NOTIFICATIONS") {
            item {
                CyberSectionHeader(
                    title = "NOTIFICATION STUDIO",
                    subtitle = "10 Card Designs • Privacy-Safe NotificationListener Integration",
                    badgeText = selectedNotifStyle
                )
                Spacer(modifier = Modifier.height(8.dp))
                CyberGlassCard(accentColor = NeonGreen) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Live Notification Card Preview ($selectedNotifStyle)",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Active System Notifications Count: $activeNotifCount (Zero private text collected)",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonGreen
                            )
                        }
                        OutlinedButton(onClick = { viewModel.openSystemPermissionScreen("NOTIFICATION_LISTENER") }) {
                            Text("Listener Access", color = ElectricCyan)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sample Styled Notification Cards
                    listOf(
                        Triple("CYBER SECURITY CORE", "Vivo T2x 5G Threat Shield: All packages verified.", "#00FF66"),
                        Triple("SYNTH MUSIC DECK", "Now Playing: Night City Overdrive (FLAC 24-bit)", "#00E5FF"),
                        Triple("FLASHCHARGE TELEMETRY", "Battery reached 88% • Thermal 31.2°C Optimal", "#B388FF")
                    ).forEach { (appTitle, body, hex) ->
                        val c = parseHexColor(hex)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(cornerRadiusDp.dp))
                                .background(Color.Black.copy(alpha = 0.72f))
                                .border(1.5.dp, c, RoundedCornerShape(cornerRadiusDp.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = c)
                            Column {
                                Text(text = "$appTitle • $selectedNotifStyle", style = MaterialTheme.typography.labelSmall, color = c)
                                Text(text = body, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Card Corner Radius: ${cornerRadiusDp.toInt()}dp", color = Color.White)
                    Slider(value = cornerRadiusDp, onValueChange = { cornerRadiusDp = it }, valueRange = 6f..32f)
                }
            }

            items(CyberCatalogData.notificationStyles) { style ->
                CyberGlassCard(
                    accentColor = if (selectedNotifStyle == style) NeonGreen else ElectricCyan,
                    onClick = {
                        selectedNotifStyle = style
                        viewModel.updateSystemStudioSelection(notificationStyle = style)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(style, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (selectedNotifStyle == style) "ACTIVE" else "SELECT",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonGreen
                        )
                    }
                }
            }
        } else {
            // STATUS BAR STUDIO (Section 12)
            item {
                CyberSectionHeader(
                    title = "STATUS BAR STUDIO",
                    subtitle = "10 Visual Styles • Clock, Battery Icon, 5G Speed & Vivo Notes",
                    badgeText = selectedStatusBarStyle
                )
                Spacer(modifier = Modifier.height(8.dp))
                CyberGlassCard(accentColor = ElectricCyan) {
                    // Live Simulated Status Bar Strip
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black)
                            .border(1.dp, NeonGreen, RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("09:41 AM • MON", style = MaterialTheme.typography.labelLarge, color = NeonGreen)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (showNetworkSpeed) {
                                Text("148 KB/s 5G", style = MaterialTheme.typography.labelSmall, color = ElectricCyan)
                            }
                            Icon(Icons.Default.SignalCellularAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Icon(Icons.Default.Wifi, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                            Icon(Icons.Default.BatteryChargingFull, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                            if (showBatteryPct) {
                                Text("88%", style = MaterialTheme.typography.labelLarge, color = NeonGreen)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Show Battery Percentage", color = Color.White)
                        Switch(checked = showBatteryPct, onCheckedChange = { showBatteryPct = it })
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Show Real-Time 5G Network Speed", color = Color.White)
                        Switch(checked = showNetworkSpeed, onCheckedChange = { showNetworkSpeed = it })
                    }
                    Text(
                        text = "Vivo Funtouch OS Note: Custom status bar headers render inside Cyber Launcher and full-screen studio previews. System-wide status bar icons are controlled by Android SystemUI.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(CyberCatalogData.statusBarStyles) { style ->
                CyberGlassCard(
                    accentColor = if (selectedStatusBarStyle == style) NeonGreen else ElectricCyan,
                    onClick = {
                        selectedStatusBarStyle = style
                        viewModel.updateSystemStudioSelection(statusBarStyle = style)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(style, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(if (selectedStatusBarStyle == style) "ACTIVE" else "APPLY", color = NeonGreen, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

// ============================================================================
// 13. LOCK SCREEN STUDIO & 14. DIALER STUDIO
// ============================================================================
@Composable
fun LockScreenAndDialerStudioSection(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel,
    initialMode: String = "LOCK_SCREEN" // "LOCK_SCREEN" or "DIALER"
) {
    var studioTab by remember(initialMode) { mutableStateOf(initialMode) }
    var selectedLockStyle by remember(uiState.prefs.activeLockScreenStyle) {
        mutableStateOf(uiState.prefs.activeLockScreenStyle)
    }
    var aodMode by remember { mutableStateOf(false) }

    // Dialer state
    var selectedDialerTheme by remember(uiState.prefs.activeDialerTheme) {
        mutableStateOf(uiState.prefs.activeDialerTheme)
    }
    var dialedNumber by remember { mutableStateOf("") }
    var simulatedCallState by remember { mutableStateOf("IDLE") } // "IDLE", "INCOMING", "OUTGOING"

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = studioTab == "LOCK_SCREEN",
                    onClick = { studioTab = "LOCK_SCREEN" },
                    label = { Text("Lock Screen Studio (10)") }
                )
                FilterChip(
                    selected = studioTab == "DIALER",
                    onClick = { studioTab = "DIALER" },
                    label = { Text("Dialer Studio (10)") }
                )
            }
        }

        if (studioTab == "LOCK_SCREEN") {
            item {
                CyberSectionHeader(
                    title = "LOCK SCREEN & AOD STUDIO",
                    subtitle = "10 Lock Designs • Clocks, Weather, Music & Biometric-Safe Preview",
                    badgeText = selectedLockStyle
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Interactive Lock Screen / AOD Simulator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            if (aodMode) Brush.verticalGradient(listOf(Color.Black, Color.Black))
                            else Brush.verticalGradient(listOf(Color(0xFF06121C), Color(0xFF140824), Color.Black))
                        )
                        .border(2.dp, NeonGreen, RoundedCornerShape(28.dp))
                        .padding(20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = NeonGreen)
                            Text(
                                text = "09:41",
                                style = MaterialTheme.typography.displayLarge,
                                color = NeonGreen,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "MONDAY, NOV 2 • 24°C CLEAR • BAT 88%",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = selectedLockStyle.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = ElectricCyan
                            )
                        }

                        // Lock Screen Notification & Music Pill
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.08f))
                                .border(1.dp, ElectricCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("♪ Cyberia Mix — Playing", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                            Text("⏮ ▶ ⏭", color = NeonGreen, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FilterChip(
                                selected = aodMode,
                                onClick = { aodMode = !aodMode },
                                label = { Text(if (aodMode) "AOD Mode ON" else "AOD Preview") }
                            )
                            Text(
                                text = "Android Keyguard Security Respected",
                                style = MaterialTheme.typography.labelSmall,
                                color = StatusCompatible
                            )
                        }
                    }
                }
            }

            items(CyberCatalogData.lockScreenDesigns) { design ->
                CyberGlassCard(
                    accentColor = if (selectedLockStyle == design) NeonGreen else ElectricCyan,
                    onClick = {
                        selectedLockStyle = design
                        viewModel.updateSystemStudioSelection(lockScreenStyle = design)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(design, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(if (selectedLockStyle == design) "SELECTED" else "PREVIEW", color = NeonGreen, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        } else {
            // 14. DIALER STUDIO
            val dialAccent = when {
                selectedDialerTheme.contains("Gold", ignoreCase = true) -> RoyalGold
                selectedDialerTheme.contains("RGB", ignoreCase = true) -> Color(0xFFFF007F)
                selectedDialerTheme.contains("iOS", ignoreCase = true) -> Color(0xFF0A84FF)
                selectedDialerTheme.contains("Nothing", ignoreCase = true) -> Color(0xFFD71921)
                else -> NeonGreen
            }

            item {
                CyberSectionHeader(
                    title = "CYBER DIALER STUDIO",
                    subtitle = "10 Dialer Themes • T9 Keypad, Call Screen Simulator & Telecom ACTION_DIAL",
                    badgeText = selectedDialerTheme,
                    accentColor = dialAccent
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(CyberCatalogData.dialerThemes) { dt ->
                        FilterChip(
                            selected = selectedDialerTheme == dt,
                            onClick = {
                                selectedDialerTheme = dt
                                viewModel.updateSystemStudioSelection(dialerTheme = dt)
                            },
                            label = { Text(dt) }
                        )
                    }
                }
            }

            // Interactive Dialpad + Incoming/Outgoing Call Simulator
            item {
                CyberGlassCard(accentColor = dialAccent) {
                    if (simulatedCallState != "IDLE") {
                        // Incoming / Outgoing Call Interface Preview
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = if (simulatedCallState == "INCOMING") "INCOMING ENCRYPTED CALL..." else "DIALING OUTBOUND...",
                                style = MaterialTheme.typography.labelLarge,
                                color = dialAccent
                            )
                            Text(
                                text = dialedNumber.ifBlank { "+1 (800) 555-CYBR" },
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Theme: $selectedDialerTheme • HD Voice 5G VoNR",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(24.dp),
                                modifier = Modifier.padding(top = 12.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.placeDialerCall(dialedNumber.ifBlank { "*#06#" }) },
                                    colors = ButtonDefaults.buttonColors(containerColor = StatusCompatible, contentColor = Color.Black)
                                ) {
                                    Icon(Icons.Default.Call, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Launch System Call", fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = { simulatedCallState = "IDLE" },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252), contentColor = Color.White)
                                ) {
                                    Icon(Icons.Default.CallEnd, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("End Preview")
                                }
                            }
                        }
                    } else {
                        // Number Readout
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Black.copy(alpha = 0.6f))
                                .border(1.dp, dialAccent.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = dialedNumber.ifBlank { "Enter number..." },
                                style = MaterialTheme.typography.headlineMedium,
                                color = if (dialedNumber.isBlank()) Color.White.copy(alpha = 0.4f) else dialAccent,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = {
                                if (dialedNumber.isNotEmpty()) dialedNumber = dialedNumber.dropLast(1)
                            }) {
                                Icon(Icons.Default.Backspace, contentDescription = "Backspace", tint = dialAccent)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 3x4 T9 Keypad
                        val rows = listOf(
                            listOf("1" to "OO", "2" to "ABC", "3" to "DEF"),
                            listOf("4" to "GHI", "5" to "JKL", "6" to "MNO"),
                            listOf("7" to "PQRS", "8" to "TUV", "9" to "WXYZ"),
                            listOf("*" to "", "0" to "+", "#" to "")
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            rows.forEach { rowKeys ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    rowKeys.forEach { (digit, sub) ->
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(dialAccent.copy(alpha = 0.12f))
                                                .border(1.dp, dialAccent.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
                                                .clickable { dialedNumber += digit }
                                                .padding(vertical = 10.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = digit,
                                                style = MaterialTheme.typography.titleLarge,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = sub,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = dialAccent,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Call Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { viewModel.placeDialerCall(dialedNumber) },
                                modifier = Modifier
                                    .weight(1.3f)
                                    .testTag("dialer_call_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = dialAccent, contentColor = Color.Black)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Call via Telecom", fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = { simulatedCallState = "INCOMING" },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Incoming UI", color = Color.White)
                            }
                            OutlinedButton(
                                onClick = { simulatedCallState = "OUTGOING" },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Outgoing UI", color = ElectricCyan)
                            }
                        }
                    }
                }
            }
        }
    }
}
