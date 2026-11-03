package com.example.launcher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.MainActivity
import com.example.ui.CyberThemeViewModel
import com.example.ui.components.RgbEdgeLightingOverlay
import com.example.ui.theme.CyberThemeXTheme

class CyberLauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: CyberThemeViewModel = viewModel()
            val state by vm.uiState.collectAsStateWithLifecycle()

            CyberThemeXTheme(
                appearanceMode = state.prefs.appearanceMode,
                primaryAccentHex = state.activeTheme.primaryColorHex,
                secondaryAccentHex = state.activeTheme.secondaryColorHex,
                fontPreset = state.prefs.activeFontStyle,
                fontScale = state.prefs.fontScale
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing)
                    ) {
                        CyberLauncherScreen(
                            uiState = state,
                            viewModel = vm,
                            isStandaloneHomeActivity = true,
                            onExitToSuite = {
                                startActivity(Intent(this@CyberLauncherActivity, MainActivity::class.java))
                            }
                        )
                        RgbEdgeLightingOverlay(
                            enabled = state.prefs.rgbEdgeEnabled,
                            presetName = state.prefs.rgbEdgePreset,
                            speedMultiplier = state.prefs.rgbEdgeSpeed,
                            thicknessDp = state.prefs.rgbEdgeThicknessDp,
                            reduceMotion = state.prefs.reduceMotion
                        )
                    }
                }
            }
        }
    }
}
