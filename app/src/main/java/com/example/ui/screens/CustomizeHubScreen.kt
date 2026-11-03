package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.MainNavTab
import com.example.data.StudioDestination
import com.example.launcher.CyberLauncherScreen
import com.example.ui.CyberThemeViewModel
import com.example.ui.CyberUiState
import com.example.ui.components.CyberGlassCard
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.RoyalGold

@Composable
fun CustomizeHubScreen(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel
) {
    val currentStudio = uiState.selectedStudio

    if (currentStudio != StudioDestination.HUB) {
        BackHandler {
            viewModel.openStudio(StudioDestination.HUB)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Sub-Studio Top Navigation Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF070C12))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.openStudio(StudioDestination.HUB) },
                    modifier = Modifier.testTag("back_to_customize_hub_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Customize Hub",
                        tint = NeonGreen
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = currentStudio.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentStudio.subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonGreen
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                when (currentStudio) {
                    StudioDestination.WALLPAPER -> WallpaperStudioSection(uiState, viewModel)
                    StudioDestination.ICONS -> IconStudioSection(uiState, viewModel)
                    StudioDestination.LAUNCHER -> CyberLauncherScreen(uiState = uiState, viewModel = viewModel)
                    StudioDestination.WIDGET_CREATOR -> WidgetStudioScreen(uiState = uiState, viewModel = viewModel)
                    StudioDestination.CONTROL_CENTER -> ControlCenterStudioSection(uiState, viewModel)
                    StudioDestination.NOTIFICATIONS -> NotificationAndStatusBarStudioSection(uiState, viewModel, "NOTIFICATIONS")
                    StudioDestination.STATUS_BAR -> NotificationAndStatusBarStudioSection(uiState, viewModel, "STATUS_BAR")
                    StudioDestination.LOCK_SCREEN -> LockScreenAndDialerStudioSection(uiState, viewModel, "LOCK_SCREEN")
                    StudioDestination.DIALER -> LockScreenAndDialerStudioSection(uiState, viewModel, "DIALER")
                    StudioDestination.FONTS -> FontStudioSection(uiState, viewModel)
                    StudioDestination.RGB_EDGE -> RgbAndChargingStudioSection(uiState, viewModel, "RGB")
                    StudioDestination.CHARGING -> RgbAndChargingStudioSection(uiState, viewModel, "CHARGING")
                    StudioDestination.SOUNDS -> SoundAndAnimationStudioSection(uiState, viewModel, "SOUNDS")
                    StudioDestination.ANIMATIONS -> SoundAndAnimationStudioSection(uiState, viewModel, "ANIMATIONS")
                    StudioDestination.THEME_CREATOR -> ThemeCreatorStudioSection(uiState, viewModel)
                    StudioDestination.COMPATIBILITY -> DeviceCompatibilityCenterSection(uiState, viewModel)
                    StudioDestination.HUB -> Unit
                }
            }
        }
    } else {
        // Unified Customize Command Hub listing all 15 Specialized Studios
        var searchQuery by remember { mutableStateOf("") }
        val allStudios = remember {
            StudioDestination.entries.filter { it != StudioDestination.HUB }
        }
        val filteredStudios = remember(searchQuery) {
            if (searchQuery.isBlank()) allStudios
            else allStudios.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                    it.subtitle.contains(searchQuery, ignoreCase = true)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("customize_hub_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "CUSTOMIZE COMMAND HUB",
                    style = MaterialTheme.typography.headlineMedium,
                    color = NeonGreen,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "All 15 Personalization Studios • Real System & Launcher Engines",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search studios (Wallpaper, Icons, Control Center, RGB, Dialer...)") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonGreen) },
                    singleLine = true
                )
            }

            items(filteredStudios, key = { it.id }) { studio ->
                val (iconVec, accent) = studioVisualMeta(studio)
                CyberGlassCard(
                    modifier = Modifier.testTag("studio_card_${studio.id}"),
                    accentColor = accent,
                    onClick = {
                        if (studio == StudioDestination.WIDGET_CREATOR) {
                            viewModel.selectTab(MainNavTab.WIDGETS)
                        } else {
                            viewModel.openStudio(studio)
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(accent.copy(alpha = 0.16f))
                                    .border(1.dp, accent, RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = iconVec, contentDescription = studio.title, tint = accent)
                            }
                            Column {
                                Text(
                                    text = studio.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = studio.subtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.75f)
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(accent.copy(alpha = 0.18f))
                                .border(1.dp, accent, RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = studio.badge,
                                style = MaterialTheme.typography.labelSmall,
                                color = accent,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun studioVisualMeta(studio: StudioDestination): Pair<ImageVector, Color> {
    return when (studio) {
        StudioDestination.WALLPAPER -> Icons.Default.Wallpaper to NeonGreen
        StudioDestination.ICONS -> Icons.Default.Palette to ElectricCyan
        StudioDestination.LAUNCHER -> Icons.Default.Home to NeonGreen
        StudioDestination.WIDGET_CREATOR -> Icons.Default.Widgets to NeonPurple
        StudioDestination.CONTROL_CENTER -> Icons.Default.Tune to RoyalGold
        StudioDestination.NOTIFICATIONS -> Icons.Default.Notifications to ElectricCyan
        StudioDestination.STATUS_BAR -> Icons.Default.SignalCellularAlt to NeonGreen
        StudioDestination.LOCK_SCREEN -> Icons.Default.Lock to NeonPurple
        StudioDestination.DIALER -> Icons.Default.Phone to NeonGreen
        StudioDestination.FONTS -> Icons.Default.FontDownload to ElectricCyan
        StudioDestination.RGB_EDGE -> Icons.Default.LightMode to Color(0xFFFF007F)
        StudioDestination.CHARGING -> Icons.Default.BatteryChargingFull to NeonGreen
        StudioDestination.SOUNDS -> Icons.Default.VolumeUp to RoyalGold
        StudioDestination.ANIMATIONS -> Icons.Default.Animation to ElectricCyan
        StudioDestination.THEME_CREATOR -> Icons.Default.Build to NeonGreen
        StudioDestination.COMPATIBILITY -> Icons.Default.PhoneAndroid to NeonGreen
        StudioDestination.HUB -> Icons.Default.Tune to NeonGreen
    }
}
