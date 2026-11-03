package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PushPin
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CustomWidgetEntity
import com.example.data.CyberCatalogData
import com.example.data.WidgetPresetCatalogItem
import com.example.engine.WallpaperAndWidgetRenderer
import com.example.ui.CyberThemeViewModel
import com.example.ui.CyberUiState
import com.example.ui.components.CyberGlassCard
import com.example.ui.components.CyberSectionHeader
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.parseHexColor

@Composable
fun WidgetStudioScreen(
    uiState: CyberUiState,
    viewModel: CyberThemeViewModel
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("Clock") }
    var showWidgetCreator by remember { mutableStateOf(false) }
    var editingPreset by remember { mutableStateOf<WidgetPresetCatalogItem?>(null) }

    val categoryItems = remember(selectedCategory) {
        CyberCatalogData.widgetStudioCatalog.filter { it.category == selectedCategory }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("widget_studio_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header + No-Code Widget Creator Trigger
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ADVANCED WIDGET STUDIO",
                        style = MaterialTheme.typography.headlineMedium,
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "70 Designs Across 7 Categories + Real Android AppWidgets",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = {
                        editingPreset = null
                        showWidgetCreator = true
                    },
                    modifier = Modifier.testTag("open_widget_creator_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Widget Creator", fontWeight = FontWeight.Bold)
                }
            }
        }

        // User's Custom Saved Widgets (Section 9)
        if (uiState.customWidgets.isNotEmpty()) {
            item {
                CyberSectionHeader(
                    title = "Your Custom & Active Home Widgets",
                    subtitle = "Rendered via real Android AppWidgetProvider & RemoteViews",
                    badgeText = "${uiState.customWidgets.size} Saved"
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(uiState.customWidgets, key = { it.id }) { customWidget ->
                        val accent = parseHexColor(customWidget.accentColorHex, NeonGreen)
                        val renderedBmp = remember(customWidget) {
                            WallpaperAndWidgetRenderer.renderWidgetBitmap(context, customWidget, widthPx = 420, heightPx = 220)
                        }
                        CyberGlassCard(
                            modifier = Modifier.width(265.dp),
                            accentColor = accent
                        ) {
                            Image(
                                bitmap = renderedBmp.asImageBitmap(),
                                contentDescription = customWidget.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(105.dp)
                                    .clip(RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.FillBounds
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = customWidget.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${customWidget.category} • ${customWidget.sizeType} • ${customWidget.fontFamily}",
                                style = MaterialTheme.typography.labelSmall,
                                color = accent
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { viewModel.pinWidgetToHomeScreen(customWidget.category, customWidget.title) },
                                    colors = ButtonDefaults.buttonColors(containerColor = accent, contentColor = Color.Black),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.PushPin, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add to Home", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                                Row {
                                    IconButton(onClick = { viewModel.duplicateCustomWidget(customWidget) }) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Duplicate", tint = ElectricCyan, modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(onClick = { viewModel.deleteCustomWidget(customWidget.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFF5252), modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 7 Major Widget Category Tabs
        item {
            CyberSectionHeader(
                title = "Browse 70 Studio Widget Designs",
                subtitle = "10 distinct designs in every category • Select to customize or pin",
                badgeText = "$selectedCategory (10)"
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CyberCatalogData.widgetCategories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        modifier = Modifier.testTag("widget_cat_$category")
                    )
                }
            }
        }

        // 10 Designs in the Selected Category
        items(categoryItems, key = { it.id }) { item ->
            val accent = parseHexColor(item.accentHex, NeonGreen)
            val bg = parseHexColor(item.secondaryHex, Color(0xFF0B121C))

            CyberGlassCard(accentColor = accent) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1.2f)) {
                        Text(
                            text = "${item.category.uppercase()} • ${item.defaultSize} • ${item.designStyle.uppercase()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = accent
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = item.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }

                    // Live Styled Mini Widget Preview Card
                    Box(
                        modifier = Modifier
                            .weight(0.95f)
                            .height(78.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(bg)
                            .border(1.5.dp, accent, RoundedCornerShape(18.dp))
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = item.previewMetric,
                                style = MaterialTheme.typography.titleMedium,
                                color = accent,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = item.designStyle,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val entity = CustomWidgetEntity(
                                title = item.title,
                                category = item.category,
                                designPreset = item.designStyle,
                                sizeType = item.defaultSize,
                                bgColorHex = item.secondaryHex,
                                accentColorHex = item.accentHex,
                                transparency = 0.9f,
                                blurRadius = 12f,
                                cornerRadiusDp = 22,
                                borderWidthDp = 2,
                                hasShadow = true,
                                fontFamily = "Cyber",
                                fontSizeSp = 24,
                                textAlign = "CENTER",
                                iconName = "bolt",
                                useGradient = true,
                                dataSource = item.dataSource,
                                customNoteText = item.previewMetric
                            )
                            viewModel.saveCustomWidget(entity)
                            viewModel.pinWidgetToHomeScreen(item.category, item.title)
                        },
                        modifier = Modifier.weight(1.3f),
                        colors = ButtonDefaults.buttonColors(containerColor = accent, contentColor = Color.Black)
                    ) {
                        Icon(Icons.Default.PushPin, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add to Home Screen", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            editingPreset = item
                            showWidgetCreator = true
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Customize", color = Color.White)
                    }
                }
            }
        }
    }

    // Complete Visual No-Code Widget Creator Modal (Section 9)
    if (showWidgetCreator) {
        WidgetCreatorDialog(
            initialPreset = editingPreset,
            onDismiss = { showWidgetCreator = false },
            onSaveAndPin = { entity, pinNow ->
                viewModel.saveCustomWidget(entity)
                if (pinNow) {
                    viewModel.pinWidgetToHomeScreen(entity.category, entity.title)
                }
                showWidgetCreator = false
            }
        )
    }
}

@Composable
fun WidgetCreatorDialog(
    initialPreset: WidgetPresetCatalogItem?,
    onDismiss: () -> Unit,
    onSaveAndPin: (CustomWidgetEntity, Boolean) -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf(initialPreset?.title ?: "Custom Cyber Widget") }
    var category by remember { mutableStateOf(initialPreset?.category ?: "Clock") }
    var sizeType by remember { mutableStateOf(initialPreset?.defaultSize ?: "4x2") }
    var bgColorHex by remember { mutableStateOf(initialPreset?.secondaryHex ?: "#081018") }
    var accentColorHex by remember { mutableStateOf(initialPreset?.accentHex ?: "#00FF66") }
    var transparency by remember { mutableFloatStateOf(0.9f) }
    var blurRadius by remember { mutableFloatStateOf(12f) }
    var cornerRadiusDp by remember { mutableIntStateOf(24) }
    var borderWidthDp by remember { mutableIntStateOf(2) }
    var hasShadow by remember { mutableStateOf(true) }
    var fontFamily by remember { mutableStateOf("Cyber") }
    var fontSizeSp by remember { mutableIntStateOf(26) }
    var textAlign by remember { mutableStateOf("CENTER") }
    var useGradient by remember { mutableStateOf(true) }
    var dataSource by remember { mutableStateOf(initialPreset?.dataSource ?: "TIME") }
    var customNoteText by remember { mutableStateOf(initialPreset?.previewMetric ?: "CYBER OS ACTIVE") }

    val draftEntity = CustomWidgetEntity(
        title = title,
        category = category,
        designPreset = initialPreset?.designStyle ?: "Custom",
        sizeType = sizeType,
        bgColorHex = bgColorHex,
        accentColorHex = accentColorHex,
        transparency = transparency,
        blurRadius = blurRadius,
        cornerRadiusDp = cornerRadiusDp,
        borderWidthDp = borderWidthDp,
        hasShadow = hasShadow,
        fontFamily = fontFamily,
        fontSizeSp = fontSizeSp,
        textAlign = textAlign,
        iconName = "bolt",
        useGradient = useGradient,
        dataSource = dataSource,
        customNoteText = customNoteText
    )

    val livePreviewBitmap = remember(draftEntity) {
        WallpaperAndWidgetRenderer.renderWidgetBitmap(context, draftEntity, widthPx = 500, heightPx = 260)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF080E16),
        title = {
            Text(
                text = "VISUAL WIDGET CREATOR",
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
                // Live Rendered Canvas Bitmap Preview
                Image(
                    bitmap = livePreviewBitmap.asImageBitmap(),
                    contentDescription = "Live Widget Canvas Preview",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(18.dp)),
                    contentScale = ContentScale.FillBounds
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Widget Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (dataSource == "NOTES") {
                    OutlinedTextField(
                        value = customNoteText,
                        onValueChange = { customNoteText = it },
                        label = { Text("Custom Banner / Note Text") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Text("Widget Size & Data Source", style = MaterialTheme.typography.labelLarge, color = Color.White)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("2x2", "4x2", "4x4").forEach { sz ->
                        FilterChip(
                            selected = sizeType == sz,
                            onClick = { sizeType = sz },
                            label = { Text(sz) }
                        )
                    }
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(listOf("TIME", "BATTERY", "CALENDAR", "WEATHER", "MUSIC", "STATS", "NOTES")) { ds ->
                        FilterChip(
                            selected = dataSource == ds,
                            onClick = { dataSource = ds },
                            label = { Text(ds) }
                        )
                    }
                }

                Text("Accent Color & Background Swatches", style = MaterialTheme.typography.labelLarge, color = Color.White)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("#00FF66", "#00E5FF", "#FF007F", "#FFD700", "#B388FF", "#D71921", "#FFFFFF").forEach { hex ->
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(parseHexColor(hex))
                                .border(if (accentColorHex == hex) 2.5.dp else 0.dp, Color.White, CircleShape)
                                .clickable { accentColorHex = hex }
                        )
                    }
                }

                Text("Transparency: ${(transparency * 100).toInt()}% • Blur: ${blurRadius.toInt()}px", color = Color.White)
                Slider(value = transparency, onValueChange = { transparency = it }, valueRange = 0.25f..1.0f)

                Text("Corner Radius: ${cornerRadiusDp}dp • Border: ${borderWidthDp}dp", color = Color.White)
                Slider(
                    value = cornerRadiusDp.toFloat(),
                    onValueChange = { cornerRadiusDp = it.toInt() },
                    valueRange = 6f..40f
                )
                Slider(
                    value = borderWidthDp.toFloat(),
                    onValueChange = { borderWidthDp = it.toInt() },
                    valueRange = 0f..6f
                )

                Text("Typography (${fontFamily}) • Size: ${fontSizeSp}sp • Align: $textAlign", color = Color.White)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(CyberCatalogData.fontStyles) { f ->
                        FilterChip(
                            selected = fontFamily == f,
                            onClick = { fontFamily = f },
                            label = { Text(f) }
                        )
                    }
                }
                Slider(
                    value = fontSizeSp.toFloat(),
                    onValueChange = { fontSizeSp = it.toInt() },
                    valueRange = 16f..40f
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("LEFT", "CENTER", "RIGHT").forEach { align ->
                        FilterChip(
                            selected = textAlign == align,
                            onClick = { textAlign = align },
                            label = { Text(align) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Gradient Background & Neon Shadow", color = Color.White)
                    Switch(checked = useGradient, onCheckedChange = { useGradient = it })
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onSaveAndPin(draftEntity, false) }) {
                    Text("Save Only", color = ElectricCyan)
                }
                Button(
                    onClick = { onSaveAndPin(draftEntity, true) },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = Color.Black)
                ) {
                    Text("Save & Pin to Home", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}
