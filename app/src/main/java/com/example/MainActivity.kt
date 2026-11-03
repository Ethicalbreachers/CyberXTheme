package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.MainNavTab
import com.example.data.StudioDestination
import com.example.ui.CyberThemeViewModel
import com.example.ui.components.OneClickApplyReportModal
import com.example.ui.components.RgbEdgeLightingOverlay
import com.example.ui.screens.CustomizeHubScreen
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.SettingsAndLibraryScreen
import com.example.ui.screens.ThemeStoreScreen
import com.example.ui.screens.WidgetStudioScreen
import com.example.ui.theme.CyberThemeXTheme
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.parseHexColor
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialTabExtra = intent?.getStringExtra("OPEN_TAB")
        setContent {
            val cyberViewModel: CyberThemeViewModel = viewModel()
            LaunchedEffect(initialTabExtra) {
                if (initialTabExtra == "WIDGETS") {
                    cyberViewModel.selectTab(MainNavTab.WIDGETS)
                }
            }
            CyberThemeXApp(viewModel = cyberViewModel)
        }
    }
}

@Composable
fun CyberThemeXApp(
    viewModel: CyberThemeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activeAccent = parseHexColor(uiState.activeTheme.primaryColorHex, NeonGreen)

    // Auto-dismiss status toast after 3.2 seconds
    LaunchedEffect(uiState.statusToastMessage) {
        if (uiState.statusToastMessage != null) {
            delay(3200)
            viewModel.clearToast()
        }
    }

    // BackHandler on secondary tabs to return to Home tab
    if (uiState.selectedTab != MainNavTab.HOME &&
        !(uiState.selectedTab == MainNavTab.CUSTOMIZE && uiState.selectedStudio != StudioDestination.HUB)
    ) {
        BackHandler {
            viewModel.selectTab(MainNavTab.HOME)
        }
    }

    CyberThemeXTheme(
        appearanceMode = uiState.prefs.appearanceMode,
        primaryAccentHex = uiState.activeTheme.primaryColorHex,
        secondaryAccentHex = uiState.activeTheme.secondaryColorHex,
        fontPreset = uiState.prefs.activeFontStyle,
        fontScale = uiState.prefs.fontScale
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets.safeDrawing,
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    NavigationBar(
                        containerColor = Color(0xFF060A10),
                        tonalElevation = 8.dp
                    ) {
                        val navItems = listOf(
                            Triple(MainNavTab.HOME, Icons.Default.Home, "nav_home"),
                            Triple(MainNavTab.THEMES, Icons.Default.Palette, "nav_themes"),
                            Triple(MainNavTab.WIDGETS, Icons.Default.Widgets, "nav_widgets"),
                            Triple(MainNavTab.CUSTOMIZE, Icons.Default.Tune, "nav_customize"),
                            Triple(MainNavTab.SETTINGS, Icons.Default.Settings, "nav_settings")
                        )
                        navItems.forEach { (tab, icon, tag) ->
                            val selected = uiState.selectedTab == tab
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    if (tab == MainNavTab.CUSTOMIZE && selected) {
                                        viewModel.openStudio(StudioDestination.HUB)
                                    } else {
                                        viewModel.selectTab(tab)
                                    }
                                },
                                icon = {
                                    Icon(imageVector = icon, contentDescription = tab.label)
                                },
                                label = {
                                    Text(
                                        text = tab.label,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.Black,
                                    selectedTextColor = activeAccent,
                                    indicatorColor = activeAccent,
                                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                    unselectedTextColor = Color.White.copy(alpha = 0.6f)
                                ),
                                modifier = Modifier.testTag(tag)
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (uiState.selectedTab) {
                        MainNavTab.HOME -> HomeDashboardScreen(uiState = uiState, viewModel = viewModel)
                        MainNavTab.THEMES -> ThemeStoreScreen(uiState = uiState, viewModel = viewModel)
                        MainNavTab.WIDGETS -> WidgetStudioScreen(uiState = uiState, viewModel = viewModel)
                        MainNavTab.CUSTOMIZE -> CustomizeHubScreen(uiState = uiState, viewModel = viewModel)
                        MainNavTab.SETTINGS -> SettingsAndLibraryScreen(uiState = uiState, viewModel = viewModel)
                    }

                    // Floating Cyber Status Banner Toast
                    AnimatedVisibility(
                        visible = uiState.statusToastMessage != null,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xEE08121C))
                                .border(1.5.dp, activeAccent, RoundedCornerShape(16.dp))
                                .clickable { viewModel.clearToast() }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = uiState.statusToastMessage ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = activeAccent,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Full-Screen RGB & Edge Lighting Overlay
            RgbEdgeLightingOverlay(
                enabled = uiState.prefs.rgbEdgeEnabled,
                presetName = uiState.prefs.rgbEdgePreset,
                speedMultiplier = uiState.prefs.rgbEdgeSpeed,
                thicknessDp = uiState.prefs.rgbEdgeThicknessDp,
                reduceMotion = uiState.prefs.reduceMotion
            )

            // One-Click Apply Verification Report Modal
            OneClickApplyReportModal(
                report = uiState.applyReport,
                onDismiss = { viewModel.dismissApplyReport() },
                onOpenLauncher = { viewModel.openStudio(StudioDestination.LAUNCHER) },
                onOpenCompatibilityCenter = { viewModel.openStudio(StudioDestination.COMPATIBILITY) }
            )
        }
    }
}
