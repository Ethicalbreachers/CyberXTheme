package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.StudioDestination
import com.example.ui.CyberThemeViewModel
import com.example.ui.CyberUiState
import com.example.ui.components.CyberGlassCard
import com.example.ui.components.CyberSectionHeader
import com.example.ui.theme.AppAppearanceMode
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.StatusCompatible

@Composable
fun SettingsAndLibraryScreen(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel
) {
    val prefs = uiState.prefs
    var backupName by remember { mutableStateOf("Vivo T2x Setup Snapshot") }
    var selectedLanguage by remember { mutableStateOf("English (Global)") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("settings_screen_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "LIBRARY, BACKUP & SETTINGS",
                style = MaterialTheme.typography.headlineMedium,
                color = NeonGreen,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Personal Customization Vault • Local Backup & Restore • Privacy & Appearance",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 1. Personal Customization Library (Section 24)
        item {
            CyberSectionHeader(
                title = "Personal Customization Library",
                subtitle = "Persisted locally in Room SQLite Database & Jetpack DataStore",
                badgeText = "Offline Ready"
            )
            Spacer(modifier = Modifier.height(8.dp))
            CyberGlassCard(accentColor = NeonGreen) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LibraryMetricColumn("Saved Themes", "${uiState.themes.size}", NeonGreen)
                    LibraryMetricColumn("4K Wallpapers", "${uiState.wallpapers.size}", ElectricCyan)
                    LibraryMetricColumn("Custom Widgets", "${uiState.customWidgets.size}", NeonPurple)
                    LibraryMetricColumn("Icon Overrides", "${uiState.customIcons.size}", StatusCompatible)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.restorePreviousTheme() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Restore Previous Theme", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = { viewModel.openStudio(StudioDestination.THEME_CREATOR) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.LibraryBooks, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Import / Export JSON", color = Color.White)
                    }
                }
            }
        }

        // 2. Local Backup & Restore Engine (Section 25)
        item {
            CyberSectionHeader(
                title = "Backup & Restore Center",
                subtitle = "Full snapshots of Themes, Widgets, Icons, Wallpapers & Launcher Layout",
                badgeText = "${uiState.backups.size} Snapshots"
            )
            Spacer(modifier = Modifier.height(8.dp))
            CyberGlassCard(accentColor = ElectricCyan) {
                OutlinedTextField(
                    value = backupName,
                    onValueChange = { backupName = it },
                    label = { Text("Backup Snapshot Label") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.createBackupSnapshot(backupName) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("create_backup_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                ) {
                    Icon(Icons.Default.Backup, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Create Complete Local Backup", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (uiState.backups.isNotEmpty()) {
            items(uiState.backups, key = { it.id }) { snap ->
                CyberGlassCard(accentColor = NeonGreen) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(snap.snapshotName, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(
                                text = "Icons: ${snap.activeIconPack} • CC: ${snap.activeControlCenter} • Font: ${snap.activeFont}",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonGreen
                            )
                        }
                        Button(
                            onClick = { viewModel.restoreBackupSnapshot(snap) },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = Color.Black)
                        ) {
                            Icon(Icons.Default.Restore, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Restore", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. Appearance Mode, Language & Motion Settings (Section 26)
        item {
            CyberSectionHeader(
                title = "Display, Appearance & Motion",
                subtitle = "AMOLED Black, Cyber Dark, Light Mode & Reduce Motion"
            )
            Spacer(modifier = Modifier.height(8.dp))
            CyberGlassCard(accentColor = NeonGreen) {
                Text("Theme Surface Mode", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        AppAppearanceMode.AMOLED_BLACK to "AMOLED Black",
                        AppAppearanceMode.CYBER_DARK to "Cyber Dark",
                        AppAppearanceMode.LIGHT_MODE to "Light Mode"
                    ).forEach { (mode, label) ->
                        FilterChip(
                            selected = prefs.appearanceMode == mode,
                            onClick = { viewModel.updateAppearanceMode(mode) },
                            label = { Text(label) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("Interface Language", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("English (Global)", "हिन्दी (India)", "日本語 (Cyber)").forEach { lang ->
                        FilterChip(
                            selected = selectedLanguage == lang,
                            onClick = { selectedLanguage = lang },
                            label = { Text(lang) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Reduce Motion & Battery Saver Animations", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Pauses continuous RGB shaders to conserve Vivo T2x 5G battery", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                    }
                    Switch(
                        checked = prefs.reduceMotion,
                        onCheckedChange = {
                            viewModel.updateAnimationPreferences(
                                preset = prefs.activeAnimationPreset,
                                speed = prefs.animationSpeedMultiplier,
                                reduceMotion = it
                            )
                        }
                    )
                }
            }
        }

        // 4. Security, Privacy & Permission Manager (Section 27)
        item {
            CyberSectionHeader(
                title = "Security, Privacy & Permission Manager",
                subtitle = "Zero tracking • Minimum permissions • Transparent explanations"
            )
            Spacer(modifier = Modifier.height(8.dp))
            CyberGlassCard(accentColor = StatusCompatible) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = StatusCompatible)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Strict Privacy Guarantee", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "• Wallpaper Permission (SET_WALLPAPER): Used only when you press Apply Wallpaper.\n" +
                        "• Gallery Access: Uses Android's zero-permission Photo Picker (no broad storage read access).\n" +
                        "• Notification Listener: Optional opt-in; never reads or transmits personal message text.\n" +
                        "• Dialer & Contacts: Uses safe Intent.ACTION_DIAL without reading private contact databases.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { viewModel.openStudio(StudioDestination.COMPATIBILITY) }) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Permission Audit", color = Color.White)
                    }
                    OutlinedButton(onClick = { viewModel.resetAllCustomizations() }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = Color(0xFFFF5252), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reset All Customizations", color = Color(0xFFFF5252))
                    }
                }
            }
        }

        // 5. About & Vivo T2x 5G Build Information
        item {
            CyberGlassCard(accentColor = NeonPurple) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = NeonPurple)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("CYBER THEME X — v1.0.0 PRO", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Engineered for Vivo T2x 5G • Android 15 • Funtouch OS 15", style = MaterialTheme.typography.labelSmall, color = NeonGreen)
                        Text("Support: support@cyberthemex.studio • All 15 Studios Active", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryMetricColumn(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.headlineMedium, color = color, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.75f))
    }
}
