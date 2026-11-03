package com.example.launcher

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.LauncherFolderEntity
import com.example.engine.InstalledAppInfo
import com.example.engine.WallpaperAndWidgetRenderer
import com.example.ui.CyberThemeViewModel
import com.example.ui.CyberUiState
import com.example.ui.components.CyberGlassCard
import com.example.ui.components.resolveIconShape
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.parseHexColor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CyberLauncherScreen(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel,
    isStandaloneHomeActivity: Boolean = false,
    onExitToSuite: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val activeTheme = uiState.activeTheme
    val prefs = uiState.prefs
    val primary = parseHexColor(activeTheme.primaryColorHex, NeonGreen)
    val secondary = parseHexColor(activeTheme.secondaryColorHex, ElectricCyan)
    val bg = parseHexColor(activeTheme.backgroundColorHex, Color(0xFF04080C))
    val iconShape = resolveIconShape(prefs.activeIconShape)

    var currentPage by remember { mutableIntStateOf(0) }
    var showAppDrawer by remember { mutableStateOf(false) }
    var showUniversalSearch by remember { mutableStateOf(false) }
    var showLayoutEditorModal by remember { mutableStateOf(false) }
    var showFolderCreatorModal by remember { mutableStateOf(false) }
    var activeFolderSheet by remember { mutableStateOf<LauncherFolderEntity?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showHiddenAppsInDrawer by remember { mutableStateOf(false) }

    val hiddenSet = remember(prefs.hiddenAppsCsv) {
        prefs.hiddenAppsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
    }

    val visibleApps = remember(uiState.installedApps, hiddenSet, showHiddenAppsInDrawer, searchQuery) {
        uiState.installedApps.filter { app ->
            val hiddenMatch = if (showHiddenAppsInDrawer) true else app.packageName !in hiddenSet
            val queryMatch = searchQuery.isBlank() ||
                app.label.contains(searchQuery, ignoreCase = true) ||
                app.categoryTag.contains(searchQuery, ignoreCase = true)
            hiddenMatch && queryMatch
        }
    }

    val (batteryPct, isCharging) = remember { WallpaperAndWidgetRenderer.getRealBatteryTelemetry(context) }
    val timeStr = remember { SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) }
    val dateStr = remember { SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date()).uppercase() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        bg,
                        primary.copy(alpha = 0.14f),
                        secondary.copy(alpha = 0.10f),
                        Color.Black
                    )
                )
            )
            .combinedClickable(
                onClick = {},
                onDoubleClick = {
                    showUniversalSearch = !showUniversalSearch
                }
            )
            .padding(14.dp)
            .testTag("cyber_launcher_root")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Launcher HUD Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "CYBER LAUNCHER • ${activeTheme.name.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = primary
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = { showUniversalSearch = !showUniversalSearch },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Universal Search", tint = primary)
                    }
                    IconButton(
                        onClick = { showFolderCreatorModal = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.CreateNewFolder, contentDescription = "Create Folder", tint = secondary)
                    }
                    IconButton(
                        onClick = { showLayoutEditorModal = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = "Launcher Layout Settings", tint = Color.White)
                    }
                }
            }

            // Universal Search Bar (Swipe-down / Double-tap / Search icon)
            AnimatedVisibility(visible = showUniversalSearch) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        if (it.isNotEmpty()) showAppDrawer = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text("Universal Search apps, folders & studios...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = primary) },
                    singleLine = true
                )
            }

            // Multi-Page Selector Tabs (Page 1: Main HUD, Page 2: Folders & Widgets, Page 3: Pro Telemetry)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("HOME PAGE 1", "FOLDERS & HUB 2", "TELEMETRY 3").forEachIndexed { idx, label ->
                    val selected = currentPage == idx
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(if (selected) primary.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.06f))
                            .border(1.dp, if (selected) primary else Color.Transparent, RoundedCornerShape(50))
                            .clickable { currentPage = idx }
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (selected) primary else Color.White.copy(alpha = 0.65f)
                        )
                    }
                }
            }

            if (!showAppDrawer) {
                // HOME SCREEN PAGES
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Live Theme Clock + Battery Home Widgets
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CyberGlassCard(
                            modifier = Modifier.weight(1.35f),
                            accentColor = primary
                        ) {
                            Text(
                                text = timeStr,
                                style = MaterialTheme.typography.displayLarge,
                                color = primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = dateStr,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = activeTheme.clockWidgetStyle,
                                style = MaterialTheme.typography.labelSmall,
                                color = secondary
                            )
                        }

                        CyberGlassCard(
                            modifier = Modifier.weight(1f),
                            accentColor = secondary
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.BatteryChargingFull,
                                    contentDescription = null,
                                    tint = secondary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$batteryPct%",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = secondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = if (isCharging) "FLASH CHARGE ⚡" else "OPTIMAL POWER",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = activeTheme.batteryWidgetStyle,
                                style = MaterialTheme.typography.labelSmall,
                                color = primary
                            )
                        }
                    }

                    // Custom Folders Row
                    if (uiState.launcherFolders.isNotEmpty()) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(uiState.launcherFolders, key = { it.id }) { folder ->
                                val folderColor = parseHexColor(folder.colorHex, primary)
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(folderColor.copy(alpha = 0.14f))
                                        .border(1.dp, folderColor.copy(alpha = 0.7f), RoundedCornerShape(16.dp))
                                        .clickable { activeFolderSheet = folder }
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Folder, contentDescription = null, tint = folderColor)
                                    Column {
                                        Text(
                                            text = folder.folderName,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${folder.layoutStyle} • ${folder.appPackagesCsv.split(",").filter { it.isNotBlank() }.size} Apps",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = folderColor
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Home App Icon Grid (Dynamic Columns & Icon Size Scale + Custom Icon Pack rendering)
                    val pageApps = remember(visibleApps, currentPage, prefs.launcherGridColumns, prefs.launcherGridRows) {
                        val pageSize = (prefs.launcherGridColumns * 2).coerceAtLeast(6)
                        val start = (currentPage * 4).coerceAtMost((visibleApps.size - 1).coerceAtLeast(0))
                        visibleApps.drop(start).take(pageSize)
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(prefs.launcherGridColumns.coerceIn(3, 6)),
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 6.dp)
                    ) {
                        items(pageApps, key = { it.packageName }) { app ->
                            val overrideIcon = uiState.customIcons.firstOrNull { it.targetPackageOrId == app.packageName }
                            val iconAccent = overrideIcon?.let { parseHexColor(it.fgColorHex, primary) } ?: primary
                            val displayLabel = overrideIcon?.customLabel?.ifBlank { app.label } ?: app.label
                            val sizeDp = (52f * prefs.activeIconSizeScale.coerceIn(0.8f, 1.3f)).dp

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable {
                                        launchAppSafely(context, app.packageName)
                                    }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(sizeDp)
                                        .clip(iconShape)
                                        .background(
                                            Brush.linearGradient(
                                                listOf(
                                                    iconAccent.copy(alpha = 0.22f),
                                                    secondary.copy(alpha = 0.14f)
                                                )
                                            )
                                        )
                                        .border(
                                            width = if (prefs.activeIconNeonBorder) 1.8.dp else 0.8.dp,
                                            color = if (prefs.activeIconNeonBorder) iconAccent else Color.White.copy(alpha = 0.25f),
                                            shape = iconShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = displayLabel.take(2).uppercase(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = iconAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                if (prefs.showIconLabels) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = displayLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // SWIPE-UP APP DRAWER WITH SEARCH & APP HIDING
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xEB070C12))
                        .border(1.dp, primary.copy(alpha = 0.45f), RoundedCornerShape(22.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "APP DRAWER (${visibleApps.size} APPS)",
                            style = MaterialTheme.typography.titleMedium,
                            color = primary,
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilterChip(
                                selected = showHiddenAppsInDrawer,
                                onClick = { showHiddenAppsInDrawer = !showHiddenAppsInDrawer },
                                label = { Text(if (showHiddenAppsInDrawer) "Showing Hidden" else "Hidden (${hiddenSet.size})") },
                                leadingIcon = {
                                    Icon(
                                        if (showHiddenAppsInDrawer) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                            OutlinedButton(onClick = { showAppDrawer = false }) {
                                Text("Close", color = Color.White)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        placeholder = { Text("Filter installed apps (tap eye icon to hide/unhide)...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = primary) },
                        singleLine = true
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(prefs.launcherGridColumns.coerceIn(3, 6)),
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(visibleApps, key = { it.packageName }) { app ->
                            val isHidden = app.packageName in hiddenSet
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isHidden) Color.Red.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.04f))
                                    .clickable { launchAppSafely(context, app.packageName) }
                                    .padding(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(iconShape)
                                        .background(primary.copy(alpha = 0.18f))
                                        .border(1.dp, primary, iconShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = app.label.take(2).uppercase(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = app.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                IconButton(
                                    onClick = {
                                        val updatedSet = hiddenSet.toMutableSet()
                                        if (isHidden) updatedSet.remove(app.packageName) else updatedSet.add(app.packageName)
                                        viewModel.updateLauncherSettings(
                                            cols = prefs.launcherGridColumns,
                                            rows = prefs.launcherGridRows,
                                            hiddenCsv = updatedSet.joinToString(","),
                                            doubleTap = prefs.doubleTapAction
                                        )
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isHidden) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle Hide App",
                                        tint = if (isHidden) Color.Red else Color.White.copy(alpha = 0.5f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CUSTOM DOCK + SWIPE-UP APP DRAWER TRIGGER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black.copy(alpha = 0.78f))
                    .border(1.5.dp, Brush.linearGradient(listOf(primary, secondary)), RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DockActionIcon(
                    icon = Icons.Default.Phone,
                    label = "Dialer",
                    tint = primary,
                    onClick = { viewModel.placeDialerCall("*#06#") }
                )
                DockActionIcon(
                    icon = if (showAppDrawer) Icons.Default.Home else Icons.Default.Apps,
                    label = if (showAppDrawer) "Home" else "App Drawer",
                    tint = secondary,
                    onClick = { showAppDrawer = !showAppDrawer }
                )
                DockActionIcon(
                    icon = Icons.Default.Terminal,
                    label = "Default Home",
                    tint = primary,
                    onClick = { viewModel.openSystemPermissionScreen("DEFAULT_HOME_SETTINGS") }
                )
                if (isStandaloneHomeActivity && onExitToSuite != null) {
                    DockActionIcon(
                        icon = Icons.Default.Settings,
                        label = "Theme Suite",
                        tint = secondary,
                        onClick = onExitToSuite
                    )
                } else {
                    DockActionIcon(
                        icon = Icons.Default.KeyboardArrowUp,
                        label = "Layout",
                        tint = secondary,
                        onClick = { showLayoutEditorModal = true }
                    )
                }
            }
        }
    }

    // Folder Content Viewer Modal
    activeFolderSheet?.let { folder ->
        AlertDialog(
            onDismissRequest = { activeFolderSheet = null },
            containerColor = Color(0xFF0B121B),
            title = {
                Text(
                    text = "${folder.folderName} (${folder.layoutStyle})",
                    color = parseHexColor(folder.colorHex, primary)
                )
            },
            text = {
                val pkgs = folder.appPackagesCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    pkgs.forEach { pkg ->
                        val label = uiState.installedApps.firstOrNull { it.packageName == pkg }?.label ?: pkg.substringAfterLast('.')
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.06f))
                                .clickable { launchAppSafely(context, pkg) }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = label, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(text = "LAUNCH", color = primary, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { activeFolderSheet = null }) {
                    Text("Done")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    viewModel.deleteLauncherFolder(folder.id)
                    activeFolderSheet = null
                }) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete Folder", color = Color.Red)
                }
            }
        )
    }

    // Create Custom Folder Modal
    if (showFolderCreatorModal) {
        var folderName by remember { mutableStateOf("Cyber Ops") }
        var selectedLayout by remember { mutableStateOf("2x2 Grid") }
        var selectedColor by remember { mutableStateOf("#00FF66") }

        AlertDialog(
            onDismissRequest = { showFolderCreatorModal = false },
            containerColor = Color(0xFF0A1018),
            title = { Text("Create Custom Launcher Folder", color = primary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = folderName,
                        onValueChange = { folderName = it },
                        label = { Text("Folder Name") },
                        singleLine = true
                    )
                    Text("Folder Layout Style", style = MaterialTheme.typography.labelLarge, color = Color.White)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(listOf("2x2 Grid", "3x3 Matrix", "Stack Card", "Neon Ring")) { style ->
                            FilterChip(
                                selected = selectedLayout == style,
                                onClick = { selectedLayout = style },
                                label = { Text(style) }
                            )
                        }
                    }
                    Text("Folder Accent Color", style = MaterialTheme.typography.labelLarge, color = Color.White)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("#00FF66", "#00E5FF", "#FF007F", "#FFD700", "#B388FF").forEach { hex ->
                            val c = parseHexColor(hex)
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(c)
                                    .border(if (selectedColor == hex) 3.dp else 0.dp, Color.White, CircleShape)
                                    .clickable { selectedColor = hex }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val samplePkgs = uiState.installedApps.take(4).joinToString(",") { it.packageName }
                        viewModel.createLauncherFolder(folderName, selectedColor, selectedLayout, samplePkgs)
                        showFolderCreatorModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primary, contentColor = Color.Black)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text("Create Folder", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showFolderCreatorModal = false }) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }

    // Launcher Grid & Gesture Layout Editor Modal
    if (showLayoutEditorModal) {
        var cols by remember { mutableIntStateOf(prefs.launcherGridColumns) }
        var rows by remember { mutableIntStateOf(prefs.launcherGridRows) }
        var iconScale by remember { mutableStateOf(prefs.activeIconSizeScale) }
        var showLabels by remember { mutableStateOf(prefs.showIconLabels) }

        AlertDialog(
            onDismissRequest = { showLayoutEditorModal = false },
            containerColor = Color(0xFF0A1018),
            title = { Text("Home Screen Layout & Grid Editor", color = primary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Grid Columns: $cols × Rows: $rows", color = Color.White)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(4 to 5, 5 to 5, 5 to 6, 3 to 4).forEach { (c, r) ->
                            FilterChip(
                                selected = cols == c && rows == r,
                                onClick = {
                                    cols = c
                                    rows = r
                                },
                                label = { Text("${c}×${r}") }
                            )
                        }
                    }
                    Text("Icon Size Scale: ${(iconScale * 100).toInt()}%", color = Color.White)
                    Slider(
                        value = iconScale,
                        onValueChange = { iconScale = it },
                        valueRange = 0.8f..1.3f
                    )
                    FilterChip(
                        selected = showLabels,
                        onClick = { showLabels = !showLabels },
                        label = { Text(if (showLabels) "App Labels Visible" else "App Labels Hidden") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateLauncherSettings(cols, rows, prefs.hiddenAppsCsv, prefs.doubleTapAction)
                        viewModel.updateIconPackPreferences(
                            pack = prefs.activeIconPack,
                            shape = prefs.activeIconShape,
                            neon = prefs.activeIconNeonBorder,
                            scale = iconScale,
                            showLabels = showLabels
                        )
                        showLayoutEditorModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = primary, contentColor = Color.Black)
                ) {
                    Text("Save Layout", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLayoutEditorModal = false }) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }
}

@Composable
private fun DockActionIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(tint.copy(alpha = 0.16f))
                .border(1.dp, tint, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = tint, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.White)
    }
}

private fun launchAppSafely(context: android.content.Context, packageName: String) {
    try {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
        }
    } catch (_: Exception) {
    }
}
