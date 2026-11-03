package com.example.widgets

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CustomWidgetEntity
import com.example.data.CyberCatalogData
import com.example.data.CyberDatabase
import com.example.data.WidgetPresetCatalogItem
import com.example.ui.theme.CyberThemeXTheme
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.parseHexColor
import kotlinx.coroutines.launch

class CyberWidgetConfigActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        // Set default canceled result in case user backs out
        val cancelResult = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(Activity.RESULT_CANCELED, cancelResult)

        setContent {
            CyberThemeXTheme {
                var selectedCategory by remember { mutableStateOf("All") }
                val categories = remember { listOf("All") + CyberCatalogData.widgetCategories }
                val filteredPresets = remember(selectedCategory) {
                    if (selectedCategory == "All") CyberCatalogData.widgetStudioCatalog
                    else CyberCatalogData.widgetStudioCatalog.filter { it.category == selectedCategory }
                }
                val scope = rememberCoroutineScope()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "CONFIGURE HOME SCREEN WIDGET",
                            style = MaterialTheme.typography.headlineMedium,
                            color = NeonGreen
                        )
                        Text(
                            text = "Select any of the 70 Cyber Theme X widget designs to bind to your Android Home Screen.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(categories) { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(filteredPresets, key = { it.id }) { preset ->
                                WidgetConfigCard(
                                    preset = preset,
                                    onSelect = {
                                        scope.launch {
                                            bindPresetAndFinish(appWidgetId, preset)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun bindPresetAndFinish(appWidgetId: Int, preset: WidgetPresetCatalogItem) {
        val dao = CyberDatabase.getInstance(applicationContext).cyberDao()
        val entity = CustomWidgetEntity(
            title = preset.title,
            category = preset.category,
            designPreset = preset.designStyle,
            sizeType = preset.defaultSize,
            bgColorHex = preset.secondaryHex,
            accentColorHex = preset.accentHex,
            transparency = 0.92f,
            blurRadius = 14f,
            cornerRadiusDp = 22,
            borderWidthDp = 2,
            hasShadow = true,
            fontFamily = "Cyber",
            fontSizeSp = 24,
            textAlign = "CENTER",
            iconName = "bolt",
            useGradient = true,
            dataSource = preset.dataSource,
            customNoteText = preset.previewMetric,
            boundAppWidgetId = appWidgetId
        )
        dao.insertCustomWidget(entity)

        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            val awm = AppWidgetManager.getInstance(applicationContext)
            WidgetUpdateCoordinator.updateSingleWidget(
                context = applicationContext,
                appWidgetManager = awm,
                appWidgetId = appWidgetId,
                defaultCategory = preset.category,
                defaultDataSource = preset.dataSource,
                defaultTitle = preset.title
            )
            val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(Activity.RESULT_OK, resultValue)
        }
        finish()
    }
}

@androidx.compose.runtime.Composable
private fun WidgetConfigCard(
    preset: WidgetPresetCatalogItem,
    onSelect: () -> Unit
) {
    val accent = parseHexColor(preset.accentHex, NeonGreen)
    val bg = parseHexColor(preset.secondaryHex, Color(0xFF0A1018))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(bg.copy(alpha = 0.9f))
            .border(1.5.dp, accent.copy(alpha = 0.65f), RoundedCornerShape(18.dp))
            .clickable { onSelect() }
            .padding(16.dp)
            .testTag("widget_config_${preset.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${preset.category.uppercase()} • ${preset.defaultSize}",
                style = MaterialTheme.typography.labelSmall,
                color = accent
            )
            Text(
                text = preset.title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = preset.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.72f)
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(accent.copy(alpha = 0.16f))
                .border(1.dp, accent, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = preset.previewMetric,
                style = MaterialTheme.typography.labelLarge,
                color = accent,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
