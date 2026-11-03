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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CyberCatalogData
import com.example.data.StudioDestination
import com.example.data.ThemeEntity
import com.example.engine.WallpaperTarget
import com.example.ui.CyberThemeViewModel
import com.example.ui.CyberUiState
import com.example.ui.components.CyberGlassCard
import com.example.ui.components.CyberSectionHeader
import com.example.ui.components.InteractivePhoneThemePreview
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.parseHexColor

@Composable
fun ThemeStoreScreen(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var filterMode by remember { mutableStateOf("All Themes") } // "All Themes", "Online Catalog", "Downloaded", "Favorites"
    var inspectedTheme by remember { mutableStateOf<ThemeEntity?>(null) }

    val filteredThemes = remember(uiState.themes, selectedCategory, searchQuery, filterMode) {
        uiState.themes.filter { theme ->
            val catMatch = selectedCategory == "All" || theme.category.equals(selectedCategory, ignoreCase = true)
            val queryMatch = searchQuery.isBlank() ||
                theme.name.contains(searchQuery, ignoreCase = true) ||
                theme.description.contains(searchQuery, ignoreCase = true) ||
                theme.category.contains(searchQuery, ignoreCase = true)
            val modeMatch = when (filterMode) {
                "Online Catalog" -> theme.isOnlineStoreItem
                "Downloaded" -> theme.isDownloaded
                "Favorites" -> theme.isFavorite
                else -> true
            }
            catMatch && queryMatch && modeMatch
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("theme_store_list"),
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
                        text = "COMPLETE THEME STORE",
                        style = MaterialTheme.typography.headlineMedium,
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "10+ Complete Ecosystems • One-Click Apply • Online & Local Library",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = { viewModel.openStudio(StudioDestination.THEME_CREATOR) },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Create", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Search & Store Mode Filters
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("theme_store_search"),
                placeholder = { Text("Search themes, icon packs, clocks or colors...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonGreen) },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listOf("All Themes", "Online Catalog", "Downloaded", "Favorites")) { mode ->
                    FilterChip(
                        selected = filterMode == mode,
                        onClick = { filterMode = mode },
                        label = { Text(mode) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CyberCatalogData.themeCategories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) }
                    )
                }
            }
        }

        // Theme Store Cards
        items(filteredThemes, key = { it.id }) { theme ->
            val primary = parseHexColor(theme.primaryColorHex, NeonGreen)
            val secondary = parseHexColor(theme.secondaryColorHex, ElectricCyan)
            val isActive = theme.id == uiState.activeTheme.id

            CyberGlassCard(
                accentColor = primary,
                borderAlpha = if (isActive) 0.95f else 0.45f
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(primary)
                        )
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(secondary)
                        )
                        Text(
                            text = theme.category.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            color = primary
                        )
                        if (theme.isOnlineStoreItem && !theme.isDownloaded) {
                            Text(
                                text = "• ONLINE STORE",
                                style = MaterialTheme.typography.labelSmall,
                                color = ElectricCyan
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${theme.downloadsCount} DLs",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        IconButton(onClick = { viewModel.toggleThemeFavorite(theme) }) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite Theme",
                                tint = if (theme.isFavorite) Color(0xFFFF4081) else Color.White.copy(alpha = 0.4f)
                            )
                        }
                    }
                }

                Text(
                    text = theme.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = theme.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 14-Component Summary Grid
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black.copy(alpha = 0.45f))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "• Layout: ${theme.homeLayoutStyle} | Dock: ${theme.dockStyle}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Text(
                        text = "• Icons: ${theme.iconPackId} | Folders: ${theme.folderStyle} | Font: ${theme.fontFamilyName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Text(
                        text = "• Widgets: ${theme.clockWidgetStyle} • ${theme.batteryWidgetStyle} • ${theme.calendarWidgetStyle} • ${theme.musicWidgetStyle}",
                        style = MaterialTheme.typography.labelSmall,
                        color = primary
                    )
                    Text(
                        text = "• Control Center: ${theme.controlCenterStyle} | Notifications: ${theme.notificationStyle}",
                        style = MaterialTheme.typography.labelSmall,
                        color = secondary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Primary Actions Row: Preview, Apply, Download, Customize
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.executeOneClickApply(theme) },
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("apply_theme_${theme.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primary,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = if (isActive) "Applied ✓" else "Apply Theme",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = { inspectedTheme = theme },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = null, tint = primary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Inspect", color = Color.White)
                    }

                    if (!theme.isDownloaded) {
                        OutlinedButton(
                            onClick = { viewModel.downloadOnlineTheme(theme) }
                        ) {
                            Icon(Icons.Default.CloudDownload, contentDescription = "Download", tint = ElectricCyan)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Secondary Actions Row: Customize, Duplicate, Share, Reset/Delete
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextActionChip(
                        icon = Icons.Default.Edit,
                        label = "Customize",
                        tint = ElectricCyan,
                        onClick = { viewModel.openStudio(StudioDestination.THEME_CREATOR) }
                    )
                    TextActionChip(
                        icon = Icons.Default.ContentCopy,
                        label = "Duplicate",
                        tint = NeonGreen,
                        onClick = { viewModel.duplicateTheme(theme) }
                    )
                    TextActionChip(
                        icon = Icons.Default.Share,
                        label = "Share JSON",
                        tint = primary,
                        onClick = { viewModel.shareTheme(theme) }
                    )
                    if (!theme.isBuiltIn) {
                        TextActionChip(
                            icon = Icons.Default.Delete,
                            label = "Delete",
                            tint = Color(0xFFFF5252),
                            onClick = { viewModel.deleteCustomTheme(theme) }
                        )
                    } else {
                        TextActionChip(
                            icon = Icons.Default.Refresh,
                            label = "Reset",
                            tint = Color.White.copy(alpha = 0.7f),
                            onClick = { viewModel.resetAllCustomizations() }
                        )
                    }
                }
            }
        }
    }

    // Full 14-Component Theme Inspector & Preview Modal
    inspectedTheme?.let { theme ->
        val primary = parseHexColor(theme.primaryColorHex, NeonGreen)
        AlertDialog(
            onDismissRequest = { inspectedTheme = null },
            containerColor = Color(0xFF080E16),
            title = {
                Text(
                    text = "${theme.name} — Full Spec",
                    style = MaterialTheme.typography.titleLarge,
                    color = primary
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    InteractivePhoneThemePreview(
                        theme = theme,
                        iconShapeName = uiState.prefs.activeIconShape
                    )

                    CyberSectionHeader(
                        title = "All 14 Included Modules",
                        badgeText = "Verified"
                    )

                    val specPairs = listOf(
                        "1. Home Screen Layout" to theme.homeLayoutStyle,
                        "2. 4K Wallpaper Preset" to theme.wallpaperPresetId,
                        "3. Icon Pack" to theme.iconPackId,
                        "4. Clock Widget" to theme.clockWidgetStyle,
                        "5. Battery Widget" to theme.batteryWidgetStyle,
                        "6. Calendar Widget" to theme.calendarWidgetStyle,
                        "7. Music Widget" to theme.musicWidgetStyle,
                        "8. Color Palette" to "${theme.primaryColorHex} / ${theme.secondaryColorHex} / ${theme.backgroundColorHex}",
                        "9. Dock Design" to theme.dockStyle,
                        "10. Folder Design" to theme.folderStyle,
                        "11. Control Center" to theme.controlCenterStyle,
                        "12. Notification Card" to theme.notificationStyle,
                        "13. Custom Typography" to theme.fontFamilyName,
                        "14. Widget Configuration" to theme.widgetConfigSummary
                    )

                    specPairs.forEach { (k, v) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.05f))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = k, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                            Text(text = v, style = MaterialTheme.typography.labelSmall, color = primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.executeOneClickApply(theme, WallpaperTarget.BOTH)
                        inspectedTheme = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primary, contentColor = Color.Black)
                ) {
                    Text("One-Click Apply All", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { inspectedTheme = null }) {
                    Text("Close", color = Color.White)
                }
            }
        )
    }
}

@Composable
private fun TextActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = tint, modifier = Modifier.size(15.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = tint)
    }
}
