package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhonelinkSetup
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CustomIconOverrideEntity
import com.example.data.CyberCatalogData
import com.example.data.WallpaperHistoryEntity
import com.example.engine.InstalledAppInfo
import com.example.engine.WallpaperAndWidgetRenderer
import com.example.engine.WallpaperTarget
import com.example.ui.CyberThemeViewModel
import com.example.ui.CyberUiState
import com.example.ui.components.CyberGlassCard
import com.example.ui.components.CyberSectionHeader
import com.example.ui.components.resolveIconShape
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.parseHexColor

@Composable
fun WallpaperStudioSection(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedWallpaper by remember {
        mutableStateOf(uiState.wallpapers.firstOrNull() ?: CyberCatalogData.builtInWallpapers.first())
    }
    var blurVal by remember(selectedWallpaper.id) { mutableFloatStateOf(selectedWallpaper.blurRadius) }
    var brightnessVal by remember(selectedWallpaper.id) { mutableFloatStateOf(selectedWallpaper.brightness) }
    var filterHex by remember(selectedWallpaper.id) { mutableStateOf(selectedWallpaper.colorFilterHex) }
    var previewOverlayMode by remember { mutableStateOf("HOME") } // "HOME" or "LOCK"

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.addGalleryWallpaper(uri.toString())
        }
    }

    val filteredWallpapers = remember(uiState.wallpapers, selectedCategory) {
        if (selectedCategory == "All") uiState.wallpapers
        else uiState.wallpapers.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    val editedWallpaper = selectedWallpaper.copy(
        blurRadius = blurVal,
        brightness = brightnessVal,
        colorFilterHex = filterHex
    )

    val previewBitmap = remember(editedWallpaper) {
        WallpaperAndWidgetRenderer.renderWallpaperBitmap(context, editedWallpaper, width = 360, height = 640)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("wallpaper_studio_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Studio Header + Gallery Picker
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "WALLPAPER STUDIO 4K",
                        style = MaterialTheme.typography.headlineMedium,
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "10 Categories • HD/4K/Live • Blur, Filter & System Apply",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = Color.Black)
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Gallery", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Live Wallpaper Editor & Home/Lock Preview Canvas
        item {
            CyberGlassCard(accentColor = parseHexColor(selectedWallpaper.primaryHex, NeonGreen)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = selectedWallpaper.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${selectedWallpaper.category} • ${selectedWallpaper.resolutionTag} RESOLUTION",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonGreen
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(
                            selected = previewOverlayMode == "HOME",
                            onClick = { previewOverlayMode = "HOME" },
                            label = { Text("Home Preview") }
                        )
                        FilterChip(
                            selected = previewOverlayMode == "LOCK",
                            onClick = { previewOverlayMode = "LOCK" },
                            label = { Text("Lock Preview") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Interactive Phone Frame Preview over Rendered Wallpaper Bitmap
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .border(2.dp, parseHexColor(selectedWallpaper.primaryHex, NeonGreen), RoundedCornerShape(22.dp))
                ) {
                    Image(
                        bitmap = previewBitmap.asImageBitmap(),
                        contentDescription = selectedWallpaper.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    if (previewOverlayMode == "LOCK") {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White)
                                Text(
                                    text = "09:41",
                                    style = MaterialTheme.typography.displayLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "MONDAY, NOVEMBER 2 • 24°C",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = NeonGreen
                                )
                            }
                            Text(
                                text = "Swipe up or use fingerprint to unlock",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.Black.copy(alpha = 0.6f))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text("09:41", color = NeonGreen, fontWeight = FontWeight.Bold)
                                Text("Vivo 5G • 88% ⚡", color = Color.White)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color.Black.copy(alpha = 0.65f))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                listOf("Dial", "Msg", "Cam", "Cyber").forEach { lbl ->
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(NeonGreen.copy(alpha = 0.2f))
                                            .border(1.dp, NeonGreen, RoundedCornerShape(10.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(lbl.take(2), color = NeonGreen, style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Real-time Blur, Brightness, and Color Filter controls
                Text(
                    text = "Gaussian Blur: ${blurVal.toInt()}px • Brightness: ${(brightnessVal * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
                Slider(value = blurVal, onValueChange = { blurVal = it }, valueRange = 0f..25f)
                Slider(value = brightnessVal, onValueChange = { brightnessVal = it }, valueRange = 0.4f..1.4f)

                Text("Color Filter & Gradient Tint", style = MaterialTheme.typography.labelLarge, color = Color.White)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        "#00000000" to "None",
                        "#4400FF66" to "Matrix",
                        "#4400E5FF" to "Cyber",
                        "#44FF007F" to "Neon",
                        "#44FFD700" to "Gold"
                    ).forEach { (hex, label) ->
                        FilterChip(
                            selected = filterHex == hex,
                            onClick = { filterHex = hex },
                            label = { Text(label) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Apply to Home, Lock, or Both via Android WallpaperManager
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.applyWallpaper(editedWallpaper, WallpaperTarget.HOME) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Home", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.applyWallpaper(editedWallpaper, WallpaperTarget.LOCK) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan, contentColor = Color.Black)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Lock", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { viewModel.applyWallpaper(editedWallpaper, WallpaperTarget.BOTH) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                    ) {
                        Icon(Icons.Default.PhonelinkSetup, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Both", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 10 Wallpaper Category Chips
        item {
            CyberSectionHeader(
                title = "10 Wallpaper Categories",
                subtitle = "Tap any wallpaper to load into the live 4K filter editor",
                badgeText = "${filteredWallpapers.size} Wallpapers"
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CyberCatalogData.wallpaperCategories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) }
                    )
                }
            }
        }

        // Wallpaper Catalog List
        items(filteredWallpapers, key = { it.id }) { wp ->
            val pColor = parseHexColor(wp.primaryHex, NeonGreen)
            val sColor = parseHexColor(wp.secondaryHex, ElectricCyan)
            val isSelected = wp.id == selectedWallpaper.id

            CyberGlassCard(
                accentColor = pColor,
                borderAlpha = if (isSelected) 0.95f else 0.4f,
                onClick = { selectedWallpaper = wp }
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
                                .size(54.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Brush.linearGradient(listOf(pColor, sColor, Color.Black)))
                                .border(1.dp, pColor, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = wp.resolutionTag,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White.copy(alpha = 0.85f))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                        Column {
                            Text(
                                text = wp.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Category: ${wp.category} • Pattern: ${wp.patternType}",
                                style = MaterialTheme.typography.labelSmall,
                                color = pColor
                            )
                            if (wp.appliedTarget != "NONE") {
                                Text(
                                    text = "Last Applied: ${wp.appliedTarget}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ElectricCyan
                                )
                            }
                        }
                    }
                    IconButton(onClick = { viewModel.toggleWallpaperFavorite(wp) }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite Wallpaper",
                            tint = if (wp.isFavorite) Color(0xFFFF4081) else Color.White.copy(alpha = 0.35f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IconStudioSection(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel
) {
    val prefs = uiState.prefs
    var selectedPack by remember(prefs.activeIconPack) { mutableStateOf(prefs.activeIconPack) }
    var selectedShape by remember(prefs.activeIconShape) { mutableStateOf(prefs.activeIconShape) }
    var neonBorder by remember(prefs.activeIconNeonBorder) { mutableStateOf(prefs.activeIconNeonBorder) }
    var iconScale by remember(prefs.activeIconSizeScale) { mutableFloatStateOf(prefs.activeIconSizeScale) }
    var showLabels by remember(prefs.showIconLabels) { mutableStateOf(prefs.showIconLabels) }
    var editingApp by remember { mutableStateOf<InstalledAppInfo?>(null) }

    val shapeObj = resolveIconShape(selectedShape)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("icon_studio_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CyberSectionHeader(
                title = "ICON STUDIO & PACK ENGINE",
                subtitle = "10 Icon Packs • Custom Shapes, Neon Borders & Per-App Launcher Icons",
                badgeText = selectedPack
            )
            Text(
                text = "Note: Custom icons apply across Cyber Launcher and shortcut widgets. Standard Android apps cannot overwrite icons inside unrelated third-party launchers without their explicit support.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Live Icon Pack Preview + Global Shape/Size Controls
        item {
            CyberGlassCard(accentColor = NeonGreen) {
                Text(
                    text = "Live Icon Pack Preview ($selectedPack • $selectedShape)",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(
                        "Phone" to "#00FF66",
                        "Camera" to "#00E5FF",
                        "Games" to "#FF007F",
                        "Music" to "#FFD700",
                        "Cyber" to "#B388FF"
                    ).forEach { (label, hex) ->
                        val c = parseHexColor(hex)
                        val boxSize = (50f * iconScale).dp
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(boxSize)
                                    .clip(shapeObj)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(c.copy(alpha = 0.25f), Color.Black)
                                        )
                                    )
                                    .border(
                                        width = if (neonBorder) 2.dp else 0.8.dp,
                                        color = if (neonBorder) c else Color.White.copy(alpha = 0.3f),
                                        shape = shapeObj
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label.take(2).uppercase(),
                                    color = c,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (showLabels) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("Select from 10 Icon Packs", style = MaterialTheme.typography.labelLarge, color = NeonGreen)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(CyberCatalogData.iconPacks) { pack ->
                        FilterChip(
                            selected = selectedPack == pack,
                            onClick = { selectedPack = pack },
                            label = { Text(pack) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Icon Mask Shape", style = MaterialTheme.typography.labelLarge, color = ElectricCyan)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(CyberCatalogData.iconShapes) { sh ->
                        FilterChip(
                            selected = selectedShape == sh,
                            onClick = { selectedShape = sh },
                            label = { Text(sh) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Icon Size Scale: ${(iconScale * 100).toInt()}%", color = Color.White)
                Slider(value = iconScale, onValueChange = { iconScale = it }, valueRange = 0.8f..1.3f)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Neon Glowing Borders & Shadows", color = Color.White)
                    Switch(checked = neonBorder, onCheckedChange = { neonBorder = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Display App Icon Labels", color = Color.White)
                    Switch(checked = showLabels, onCheckedChange = { showLabels = it })
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.updateIconPackPreferences(
                                pack = selectedPack,
                                shape = selectedShape,
                                neon = neonBorder,
                                scale = iconScale,
                                showLabels = showLabels
                            )
                        },
                        modifier = Modifier.weight(1.4f),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                    ) {
                        Text("Apply Icon Pack to Launcher", fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = { viewModel.shareTheme(uiState.activeTheme) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Export Pack", color = Color.White)
                    }
                }
            }
        }

        // Individual App Icon Selection & Override List
        item {
            CyberSectionHeader(
                title = "Per-App Custom Icon Editor",
                subtitle = "Tap any app to customize its label, neon color, or gallery icon",
                badgeText = "${uiState.installedApps.size} Apps"
            )
        }

        items(uiState.installedApps.take(16), key = { it.packageName }) { app ->
            val override = uiState.customIcons.firstOrNull { it.targetPackageOrId == app.packageName }
            val fgColor = override?.let { parseHexColor(it.fgColorHex, NeonGreen) } ?: NeonGreen
            val displayLabel = override?.customLabel?.ifBlank { app.label } ?: app.label

            CyberGlassCard(
                accentColor = fgColor,
                onClick = { editingApp = app }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(shapeObj)
                                .background(fgColor.copy(alpha = 0.2f))
                                .border(1.5.dp, fgColor, shapeObj),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(displayLabel.take(2).uppercase(), color = fgColor, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text(
                                text = displayLabel,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = app.packageName,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }
                    Text(
                        text = if (override != null) "CUSTOMIZED" else "EDIT ICON",
                        style = MaterialTheme.typography.labelSmall,
                        color = fgColor
                    )
                }
            }
        }
    }

    // Per-App Icon Customization Modal
    editingApp?.let { targetApp ->
        var customLabel by remember { mutableStateOf(targetApp.label) }
        var customFgHex by remember { mutableStateOf("#00FF66") }
        var customShape by remember { mutableStateOf(selectedShape) }

        AlertDialog(
            onDismissRequest = { editingApp = null },
            containerColor = Color(0xFF0A1018),
            title = { Text("Customize Icon: ${targetApp.label}", color = NeonGreen) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = customLabel,
                        onValueChange = { customLabel = it },
                        label = { Text("Custom Icon Label") },
                        singleLine = true
                    )
                    Text("Icon Neon Color", color = Color.White)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("#00FF66", "#00E5FF", "#FF007F", "#FFD700", "#B388FF", "#FFFFFF").forEach { hex ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(parseHexColor(hex))
                                    .border(if (customFgHex == hex) 2.5.dp else 0.dp, Color.White, CircleShape)
                                    .clickable { customFgHex = hex }
                            )
                        }
                    }
                    Text("Shape Override", color = Color.White)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(CyberCatalogData.iconShapes) { sh ->
                            FilterChip(
                                selected = customShape == sh,
                                onClick = { customShape = sh },
                                label = { Text(sh) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveCustomAppIconOverride(
                            CustomIconOverrideEntity(
                                targetPackageOrId = targetApp.packageName,
                                appLabel = targetApp.label,
                                customLabel = customLabel,
                                packName = selectedPack,
                                shape = customShape,
                                bgColorHex = "#081018",
                                fgColorHex = customFgHex,
                                neonBorder = true,
                                useGradient = true,
                                hasShadow = true,
                                sizeScale = iconScale,
                                showLabel = showLabels
                            )
                        )
                        editingApp = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                ) {
                    Text("Save App Icon", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { editingApp = null }) {
                    Text("Cancel", color = Color.White)
                }
            }
        )
    }
}
