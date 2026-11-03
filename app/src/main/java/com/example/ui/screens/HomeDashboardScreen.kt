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
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.MainNavTab
import com.example.data.StudioDestination
import com.example.data.ThemeEntity
import com.example.ui.CyberThemeViewModel
import com.example.ui.CyberUiState
import com.example.ui.components.CyberGlassCard
import com.example.ui.components.CyberSectionHeader
import com.example.ui.components.InteractivePhoneThemePreview
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.RainbowGradientBrush
import com.example.ui.theme.RoyalGold
import com.example.ui.theme.StatusCompatible
import com.example.ui.theme.parseHexColor

@Composable
fun HomeDashboardScreen(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel
) {
    val activeTheme = uiState.activeTheme
    val primary = parseHexColor(activeTheme.primaryColorHex, NeonGreen)
    val secondary = parseHexColor(activeTheme.secondaryColorHex, ElectricCyan)
    val audit = uiState.compatibilityAudit

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_dashboard_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Hero Header Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF06120B),
                                Color(0xFF0A1926),
                                Color(0xFF150924)
                            )
                        )
                    )
                    .border(1.5.dp, RainbowGradientBrush, RoundedCornerShape(24.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CYBER THEME X // OS 15",
                            style = MaterialTheme.typography.labelLarge,
                            color = NeonGreen
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(NeonGreen.copy(alpha = 0.16f))
                                .border(1.dp, NeonGreen, RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "VIVO T2X 5G READY",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonGreen
                            )
                        }
                    }
                    Text(
                        text = "Active Theme: ${activeTheme.name}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = activeTheme.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // 2. Live Interactive Theme Preview + Apply Theme Button
        item {
            CyberSectionHeader(
                title = "Current Theme & Live Preview",
                subtitle = "14 synchronized modules • Tap One-Click Apply to deploy",
                badgeText = activeTheme.category
            )
            Spacer(modifier = Modifier.height(8.dp))
            InteractivePhoneThemePreview(
                theme = activeTheme,
                iconShapeName = uiState.prefs.activeIconShape,
                onApplyClick = { viewModel.executeOneClickApply(activeTheme) }
            )
        }

        // 3. Five Mandatory Studio Shortcuts (Wallpaper, Icon Studio, Widget Studio, Control Center, Launcher)
        item {
            CyberSectionHeader(
                title = "Quick Personalization Studios",
                subtitle = "Direct access to core customization engines",
                badgeText = "5 Shortcuts"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickStudioShortcutCard(
                        title = "Wallpaper Studio",
                        subtitle = "10 Categories • 4K & Live",
                        icon = Icons.Default.Wallpaper,
                        accent = NeonGreen,
                        tag = "shortcut_wallpaper",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.openStudio(StudioDestination.WALLPAPER) }
                    )
                    QuickStudioShortcutCard(
                        title = "Icon Studio",
                        subtitle = "10 Packs • Shapes & Neon",
                        icon = Icons.Default.Palette,
                        accent = ElectricCyan,
                        tag = "shortcut_icons",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.openStudio(StudioDestination.ICONS) }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickStudioShortcutCard(
                        title = "Widget Studio",
                        subtitle = "70 Designs + Creator",
                        icon = Icons.Default.Widgets,
                        accent = NeonPurple,
                        tag = "shortcut_widgets",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.selectTab(MainNavTab.WIDGETS) }
                    )
                    QuickStudioShortcutCard(
                        title = "Control Center",
                        subtitle = "10 Themes • System Tiles",
                        icon = Icons.Default.Tune,
                        accent = RoyalGold,
                        tag = "shortcut_control_center",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.openStudio(StudioDestination.CONTROL_CENTER) }
                    )
                }
                QuickStudioShortcutCard(
                    title = "Cyber Launcher (Optional Default Home)",
                    subtitle = "Multi-page Home Screen, App Drawer, Custom Folders, App Hiding & Grid Editor",
                    icon = Icons.Default.Home,
                    accent = NeonGreen,
                    tag = "shortcut_launcher",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.openStudio(StudioDestination.LAUNCHER) }
                )
            }
        }

        // 4. Recently Used Themes
        item {
            ThemeHorizontalSection(
                title = "Recently Used Themes",
                subtitle = "Instant rollback or re-application",
                icon = Icons.Default.History,
                themes = uiState.recentThemes.ifEmpty { uiState.themes.take(4) },
                activeThemeId = activeTheme.id,
                onApply = { viewModel.executeOneClickApply(it) },
                onFavorite = { viewModel.toggleThemeFavorite(it) }
            )
        }

        // 5. Favorite Themes
        item {
            ThemeHorizontalSection(
                title = "Favorite Themes",
                subtitle = "Your starred personalization setups",
                icon = Icons.Default.Favorite,
                themes = uiState.favoriteThemes.ifEmpty { uiState.themes.take(3) },
                activeThemeId = activeTheme.id,
                onApply = { viewModel.executeOneClickApply(it) },
                onFavorite = { viewModel.toggleThemeFavorite(it) }
            )
        }

        // 6. Recommended Themes
        item {
            ThemeHorizontalSection(
                title = "Recommended for Vivo T2x 5G",
                subtitle = "Optimized for AMOLED & Funtouch OS 15",
                icon = Icons.Default.Star,
                themes = uiState.recommendedThemes,
                activeThemeId = activeTheme.id,
                onApply = { viewModel.executeOneClickApply(it) },
                onFavorite = { viewModel.toggleThemeFavorite(it) }
            )
        }

        // 7. Downloaded Themes
        item {
            ThemeHorizontalSection(
                title = "Downloaded & Offline Ready",
                subtitle = "${uiState.downloadedThemes.size} themes stored locally in Room DB",
                icon = Icons.Default.DownloadDone,
                themes = uiState.downloadedThemes,
                activeThemeId = activeTheme.id,
                onApply = { viewModel.executeOneClickApply(it) },
                onFavorite = { viewModel.toggleThemeFavorite(it) }
            )
        }

        // 8. Device Compatibility Status Card (Vivo T2x 5G)
        item {
            CyberGlassCard(
                accentColor = StatusCompatible,
                onClick = { viewModel.openStudio(StudioDestination.COMPATIBILITY) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = StatusCompatible,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Device Compatibility Status",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${audit?.modelName ?: "Vivo T2x 5G"} • ${audit?.androidVersionLabel ?: "Android 15"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = StatusCompatible
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = StatusCompatible
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${audit?.funtouchOsLabel ?: "Funtouch OS 15"} • Wallpaper API: Ready • AppWidgets: Ready • Torch Hardware: ${if (audit?.hasFlashlightHardware != false) "Ready" else "Simulated"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { viewModel.openStudio(StudioDestination.COMPATIBILITY) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StatusCompatible,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Open Vivo T2x 5G Center", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(onClick = { viewModel.openStudio(StudioDestination.HUB) }) {
                        Icon(Icons.Default.Apps, contentDescription = null, tint = ElectricCyan)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("All 15 Studios", color = ElectricCyan)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickStudioShortcutCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accent: Color,
    tag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CyberGlassCard(
        modifier = modifier.testTag(tag),
        accentColor = accent,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accent.copy(alpha = 0.16f))
                    .border(1.dp, accent, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = accent)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.72f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ThemeHorizontalSection(
    title: String,
    subtitle: String,
    icon: ImageVector,
    themes: List<ThemeEntity>,
    activeThemeId: String,
    onApply: (ThemeEntity) -> Unit,
    onFavorite: (ThemeEntity) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(20.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(themes, key = { "${title}_${it.id}" }) { theme ->
                val accent = parseHexColor(theme.primaryColorHex, NeonGreen)
                val secondary = parseHexColor(theme.secondaryColorHex, ElectricCyan)
                val isCurrent = theme.id == activeThemeId

                CyberGlassCard(
                    modifier = Modifier.width(235.dp),
                    accentColor = accent,
                    borderAlpha = if (isCurrent) 0.95f else 0.4f,
                    onClick = { onApply(theme) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(accent)
                            )
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(secondary)
                            )
                        }
                        Text(
                            text = if (isCurrent) "ACTIVE" else theme.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = accent
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = theme.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${theme.iconPackId} • ${theme.clockWidgetStyle}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { onApply(theme) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accent,
                                contentColor = Color.Black
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(if (isCurrent) "Re-Apply" else "Apply", fontWeight = FontWeight.Bold)
                        }
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = if (theme.isFavorite) Color(0xFFFF4081) else Color.White.copy(alpha = 0.35f),
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { onFavorite(theme) }
                        )
                    }
                }
            }
        }
    }
}
