package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.ui.theme.AppAppearanceMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.cyberDataStore: DataStore<Preferences> by preferencesDataStore(name = "cyber_theme_x_prefs")

data class UserCustomizationPrefs(
    val activeThemeId: String = "theme_cyber_hacker",
    val previousThemeId: String = "theme_amoled_black",
    val appearanceMode: AppAppearanceMode = AppAppearanceMode.AMOLED_BLACK,
    val activeIconPack: String = "Cyber Hacker",
    val activeIconShape: String = "Rounded",
    val activeIconNeonBorder: Boolean = true,
    val activeIconSizeScale: Float = 1.0f,
    val showIconLabels: Boolean = true,
    val activeControlCenterTheme: String = "Cyber Hacker",
    val controlCenterBlur: Float = 18f,
    val controlCenterTransparency: Float = 0.85f,
    val activeNotificationStyle: String = "Terminal Code Card",
    val activeStatusBarStyle: String = "Cyber Telemetry HUD",
    val activeLockScreenStyle: String = "Cyber Matrix Terminal Lock",
    val activeDialerTheme: String = "Cyber Dark",
    val activeFontStyle: String = "Cyber",
    val fontScale: Float = 1.0f,
    val rgbEdgeEnabled: Boolean = true,
    val rgbEdgePreset: String = "Matrix Acid Green",
    val rgbEdgeSpeed: Float = 1.0f,
    val rgbEdgeThicknessDp: Float = 3.5f,
    val activeChargingAnimation: String = "Cyber Charging",
    val activeSoundPack: String = "Cyber Pulse 2099",
    val activeAnimationPreset: String = "Cyber Snap (Ultra Fast)",
    val animationSpeedMultiplier: Float = 1.0f,
    val reduceMotion: Boolean = false,
    val launcherGridColumns: Int = 4,
    val launcherGridRows: Int = 5,
    val hiddenAppsCsv: String = "",
    val doubleTapAction: String = "Open Control Center"
)

class CyberPreferencesRepository(private val context: Context) {

    private object Keys {
        val ACTIVE_THEME_ID = stringPreferencesKey("active_theme_id")
        val PREVIOUS_THEME_ID = stringPreferencesKey("previous_theme_id")
        val APPEARANCE_MODE = stringPreferencesKey("appearance_mode")
        val ACTIVE_ICON_PACK = stringPreferencesKey("active_icon_pack")
        val ACTIVE_ICON_SHAPE = stringPreferencesKey("active_icon_shape")
        val ACTIVE_ICON_NEON = booleanPreferencesKey("active_icon_neon")
        val ACTIVE_ICON_SIZE = floatPreferencesKey("active_icon_size")
        val SHOW_ICON_LABELS = booleanPreferencesKey("show_icon_labels")
        val CONTROL_CENTER_THEME = stringPreferencesKey("control_center_theme")
        val CONTROL_CENTER_BLUR = floatPreferencesKey("control_center_blur")
        val CONTROL_CENTER_ALPHA = floatPreferencesKey("control_center_alpha")
        val NOTIFICATION_STYLE = stringPreferencesKey("notification_style")
        val STATUS_BAR_STYLE = stringPreferencesKey("status_bar_style")
        val LOCK_SCREEN_STYLE = stringPreferencesKey("lock_screen_style")
        val DIALER_THEME = stringPreferencesKey("dialer_theme")
        val FONT_STYLE = stringPreferencesKey("font_style")
        val FONT_SCALE = floatPreferencesKey("font_scale")
        val RGB_ENABLED = booleanPreferencesKey("rgb_enabled")
        val RGB_PRESET = stringPreferencesKey("rgb_preset")
        val RGB_SPEED = floatPreferencesKey("rgb_speed")
        val RGB_THICKNESS = floatPreferencesKey("rgb_thickness")
        val CHARGING_ANIM = stringPreferencesKey("charging_anim")
        val SOUND_PACK = stringPreferencesKey("sound_pack")
        val ANIM_PRESET = stringPreferencesKey("anim_preset")
        val ANIM_SPEED = floatPreferencesKey("anim_speed")
        val REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        val GRID_COLS = intPreferencesKey("grid_cols")
        val GRID_ROWS = intPreferencesKey("grid_rows")
        val HIDDEN_APPS_CSV = stringPreferencesKey("hidden_apps_csv")
        val DOUBLE_TAP_ACTION = stringPreferencesKey("double_tap_action")
    }

    val prefsFlow: Flow<UserCustomizationPrefs> = context.cyberDataStore.data.map { prefs ->
        val modeName = prefs[Keys.APPEARANCE_MODE] ?: AppAppearanceMode.AMOLED_BLACK.name
        val mode = runCatching { AppAppearanceMode.valueOf(modeName) }.getOrDefault(AppAppearanceMode.AMOLED_BLACK)
        UserCustomizationPrefs(
            activeThemeId = prefs[Keys.ACTIVE_THEME_ID] ?: "theme_cyber_hacker",
            previousThemeId = prefs[Keys.PREVIOUS_THEME_ID] ?: "theme_amoled_black",
            appearanceMode = mode,
            activeIconPack = prefs[Keys.ACTIVE_ICON_PACK] ?: "Cyber Hacker",
            activeIconShape = prefs[Keys.ACTIVE_ICON_SHAPE] ?: "Rounded",
            activeIconNeonBorder = prefs[Keys.ACTIVE_ICON_NEON] ?: true,
            activeIconSizeScale = prefs[Keys.ACTIVE_ICON_SIZE] ?: 1.0f,
            showIconLabels = prefs[Keys.SHOW_ICON_LABELS] ?: true,
            activeControlCenterTheme = prefs[Keys.CONTROL_CENTER_THEME] ?: "Cyber Hacker",
            controlCenterBlur = prefs[Keys.CONTROL_CENTER_BLUR] ?: 18f,
            controlCenterTransparency = prefs[Keys.CONTROL_CENTER_ALPHA] ?: 0.85f,
            activeNotificationStyle = prefs[Keys.NOTIFICATION_STYLE] ?: "Terminal Code Card",
            activeStatusBarStyle = prefs[Keys.STATUS_BAR_STYLE] ?: "Cyber Telemetry HUD",
            activeLockScreenStyle = prefs[Keys.LOCK_SCREEN_STYLE] ?: "Cyber Matrix Terminal Lock",
            activeDialerTheme = prefs[Keys.DIALER_THEME] ?: "Cyber Dark",
            activeFontStyle = prefs[Keys.FONT_STYLE] ?: "Cyber",
            fontScale = prefs[Keys.FONT_SCALE] ?: 1.0f,
            rgbEdgeEnabled = prefs[Keys.RGB_ENABLED] ?: true,
            rgbEdgePreset = prefs[Keys.RGB_PRESET] ?: "Matrix Acid Green",
            rgbEdgeSpeed = prefs[Keys.RGB_SPEED] ?: 1.0f,
            rgbEdgeThicknessDp = prefs[Keys.RGB_THICKNESS] ?: 3.5f,
            activeChargingAnimation = prefs[Keys.CHARGING_ANIM] ?: "Cyber Charging",
            activeSoundPack = prefs[Keys.SOUND_PACK] ?: "Cyber Pulse 2099",
            activeAnimationPreset = prefs[Keys.ANIM_PRESET] ?: "Cyber Snap (Ultra Fast)",
            animationSpeedMultiplier = prefs[Keys.ANIM_SPEED] ?: 1.0f,
            reduceMotion = prefs[Keys.REDUCE_MOTION] ?: false,
            launcherGridColumns = prefs[Keys.GRID_COLS] ?: 4,
            launcherGridRows = prefs[Keys.GRID_ROWS] ?: 5,
            hiddenAppsCsv = prefs[Keys.HIDDEN_APPS_CSV] ?: "",
            doubleTapAction = prefs[Keys.DOUBLE_TAP_ACTION] ?: "Open Control Center"
        )
    }

    suspend fun applyThemeToPreferences(theme: ThemeEntity) {
        context.cyberDataStore.edit { prefs ->
            val current = prefs[Keys.ACTIVE_THEME_ID] ?: "theme_cyber_hacker"
            if (current != theme.id) {
                prefs[Keys.PREVIOUS_THEME_ID] = current
            }
            prefs[Keys.ACTIVE_THEME_ID] = theme.id
            prefs[Keys.ACTIVE_ICON_PACK] = theme.iconPackId
            prefs[Keys.CONTROL_CENTER_THEME] = theme.controlCenterStyle
            prefs[Keys.NOTIFICATION_STYLE] = theme.notificationStyle
            prefs[Keys.FONT_STYLE] = theme.fontFamilyName
            if (theme.category == "AMOLED Black") {
                prefs[Keys.APPEARANCE_MODE] = AppAppearanceMode.AMOLED_BLACK.name
            }
        }
    }

    suspend fun updateAppearanceMode(mode: AppAppearanceMode) {
        context.cyberDataStore.edit { it[Keys.APPEARANCE_MODE] = mode.name }
    }

    suspend fun updateIconSettings(pack: String, shape: String, neon: Boolean, scale: Float, showLabels: Boolean) {
        context.cyberDataStore.edit {
            it[Keys.ACTIVE_ICON_PACK] = pack
            it[Keys.ACTIVE_ICON_SHAPE] = shape
            it[Keys.ACTIVE_ICON_NEON] = neon
            it[Keys.ACTIVE_ICON_SIZE] = scale
            it[Keys.SHOW_ICON_LABELS] = showLabels
        }
    }

    suspend fun updateControlCenterSettings(theme: String, blur: Float, alpha: Float) {
        context.cyberDataStore.edit {
            it[Keys.CONTROL_CENTER_THEME] = theme
            it[Keys.CONTROL_CENTER_BLUR] = blur
            it[Keys.CONTROL_CENTER_ALPHA] = alpha
        }
    }

    suspend fun updateSystemStudioStyles(
        notificationStyle: String? = null,
        statusBarStyle: String? = null,
        lockScreenStyle: String? = null,
        dialerTheme: String? = null
    ) {
        context.cyberDataStore.edit { prefs ->
            notificationStyle?.let { prefs[Keys.NOTIFICATION_STYLE] = it }
            statusBarStyle?.let { prefs[Keys.STATUS_BAR_STYLE] = it }
            lockScreenStyle?.let { prefs[Keys.LOCK_SCREEN_STYLE] = it }
            dialerTheme?.let { prefs[Keys.DIALER_THEME] = it }
        }
    }

    suspend fun updateFontSettings(fontStyle: String, scale: Float) {
        context.cyberDataStore.edit {
            it[Keys.FONT_STYLE] = fontStyle
            it[Keys.FONT_SCALE] = scale
        }
    }

    suspend fun updateRgbSettings(enabled: Boolean, preset: String, speed: Float, thickness: Float) {
        context.cyberDataStore.edit {
            it[Keys.RGB_ENABLED] = enabled
            it[Keys.RGB_PRESET] = preset
            it[Keys.RGB_SPEED] = speed
            it[Keys.RGB_THICKNESS] = thickness
        }
    }

    suspend fun updateChargingAndSound(chargingAnim: String? = null, soundPack: String? = null) {
        context.cyberDataStore.edit { prefs ->
            chargingAnim?.let { prefs[Keys.CHARGING_ANIM] = it }
            soundPack?.let { prefs[Keys.SOUND_PACK] = it }
        }
    }

    suspend fun updateAnimationSettings(preset: String, speed: Float, reduceMotion: Boolean) {
        context.cyberDataStore.edit {
            it[Keys.ANIM_PRESET] = preset
            it[Keys.ANIM_SPEED] = speed
            it[Keys.REDUCE_MOTION] = reduceMotion
        }
    }

    suspend fun updateLauncherSettings(cols: Int, rows: Int, hiddenCsv: String, doubleTap: String) {
        context.cyberDataStore.edit {
            it[Keys.GRID_COLS] = cols
            it[Keys.GRID_ROWS] = rows
            it[Keys.HIDDEN_APPS_CSV] = hiddenCsv
            it[Keys.DOUBLE_TAP_ACTION] = doubleTap
        }
    }

    suspend fun resetAllCustomizations() {
        context.cyberDataStore.edit { it.clear() }
    }
}
