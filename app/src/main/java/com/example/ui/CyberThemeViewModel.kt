package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ApplyComponentStatus
import com.example.data.ApplyStepReportItem
import com.example.data.BackupSnapshotEntity
import com.example.data.ControlCenterTileItem
import com.example.data.CustomIconOverrideEntity
import com.example.data.CustomWidgetEntity
import com.example.data.CyberCatalogData
import com.example.data.CyberDatabase
import com.example.data.CyberPreferencesRepository
import com.example.data.LauncherFolderEntity
import com.example.data.MainNavTab
import com.example.data.StudioDestination
import com.example.data.ThemeEntity
import com.example.data.UserCustomizationPrefs
import com.example.data.WallpaperHistoryEntity
import com.example.engine.DeviceCompatibilityAudit
import com.example.engine.InstalledAppInfo
import com.example.engine.SystemIntegrationHelper
import com.example.engine.WallpaperAndWidgetRenderer
import com.example.engine.WallpaperTarget
import com.example.ui.theme.AppAppearanceMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject

data class OneClickApplyReportState(
    val isVisible: Boolean = false,
    val isApplying: Boolean = false,
    val themeName: String = "",
    val steps: List<ApplyStepReportItem> = emptyList(),
    val summaryBanner: String = ""
)

data class CyberUiState(
    val selectedTab: MainNavTab = MainNavTab.HOME,
    val selectedStudio: StudioDestination = StudioDestination.HUB,
    val themes: List<ThemeEntity> = CyberCatalogData.builtInThemes,
    val prefs: UserCustomizationPrefs = UserCustomizationPrefs(),
    val customWidgets: List<CustomWidgetEntity> = emptyList(),
    val customIcons: List<CustomIconOverrideEntity> = emptyList(),
    val wallpapers: List<WallpaperHistoryEntity> = CyberCatalogData.builtInWallpapers,
    val launcherFolders: List<LauncherFolderEntity> = emptyList(),
    val backups: List<BackupSnapshotEntity> = emptyList(),
    val installedApps: List<InstalledAppInfo> = emptyList(),
    val controlCenterTiles: List<ControlCenterTileItem> = CyberCatalogData.defaultControlCenterTiles,
    val compatibilityAudit: DeviceCompatibilityAudit? = null,
    val applyReport: OneClickApplyReportState = OneClickApplyReportState(),
    val statusToastMessage: String? = null
) {
    val activeTheme: ThemeEntity
        get() = themes.firstOrNull { it.id == prefs.activeThemeId }
            ?: themes.firstOrNull()
            ?: CyberCatalogData.builtInThemes.first()

    val favoriteThemes: List<ThemeEntity>
        get() = themes.filter { it.isFavorite }

    val recentThemes: List<ThemeEntity>
        get() = themes.filter { it.lastUsedTimestamp > 0L }.sortedByDescending { it.lastUsedTimestamp }.take(6)

    val recommendedThemes: List<ThemeEntity>
        get() = themes.sortedByDescending { it.downloadsCount }.take(6)

    val downloadedThemes: List<ThemeEntity>
        get() = themes.filter { it.isDownloaded }
}

private data class LocalTransientState(
    val selectedTab: MainNavTab = MainNavTab.HOME,
    val selectedStudio: StudioDestination = StudioDestination.HUB,
    val controlCenterTiles: List<ControlCenterTileItem> = CyberCatalogData.defaultControlCenterTiles,
    val compatibilityAudit: DeviceCompatibilityAudit? = null,
    val installedApps: List<InstalledAppInfo> = emptyList(),
    val applyReport: OneClickApplyReportState = OneClickApplyReportState(),
    val statusToastMessage: String? = null
)

class CyberThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val context get() = getApplication<Application>().applicationContext
    private val dao = CyberDatabase.getInstance(context).cyberDao()
    private val prefsRepo = CyberPreferencesRepository(context)
    private val transientState = MutableStateFlow(LocalTransientState())

    val uiState: StateFlow<CyberUiState> = combine(
        dao.observeAllThemes(),
        prefsRepo.prefsFlow,
        dao.observeCustomWidgets(),
        dao.observeCustomIcons(),
        dao.observeWallpapers(),
        dao.observeLauncherFolders(),
        dao.observeBackups(),
        transientState
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        val themes = (values[0] as List<ThemeEntity>).ifEmpty { CyberCatalogData.builtInThemes }
        val prefs = values[1] as UserCustomizationPrefs
        @Suppress("UNCHECKED_CAST")
        val widgets = values[2] as List<CustomWidgetEntity>
        @Suppress("UNCHECKED_CAST")
        val icons = values[3] as List<CustomIconOverrideEntity>
        @Suppress("UNCHECKED_CAST")
        val wallpapers = (values[4] as List<WallpaperHistoryEntity>).ifEmpty { CyberCatalogData.builtInWallpapers }
        @Suppress("UNCHECKED_CAST")
        val folders = values[5] as List<LauncherFolderEntity>
        @Suppress("UNCHECKED_CAST")
        val backups = values[6] as List<BackupSnapshotEntity>
        val local = values[7] as LocalTransientState

        CyberUiState(
            selectedTab = local.selectedTab,
            selectedStudio = local.selectedStudio,
            themes = themes,
            prefs = prefs,
            customWidgets = widgets,
            customIcons = icons,
            wallpapers = wallpapers,
            launcherFolders = folders,
            backups = backups,
            installedApps = local.installedApps,
            controlCenterTiles = local.controlCenterTiles,
            compatibilityAudit = local.compatibilityAudit,
            applyReport = local.applyReport,
            statusToastMessage = local.statusToastMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CyberUiState(
            compatibilityAudit = SystemIntegrationHelper.inspectDeviceCompatibility(context),
            installedApps = SystemIntegrationHelper.queryInstalledLauncherApps(context)
        )
    )

    init {
        seedInitialDatabaseAndAudit()
    }

    private fun seedInitialDatabaseAndAudit() {
        viewModelScope.launch {
            val audit = SystemIntegrationHelper.inspectDeviceCompatibility(context)
            val apps = SystemIntegrationHelper.queryInstalledLauncherApps(context)
            transientState.update {
                it.copy(compatibilityAudit = audit, installedApps = apps)
            }

            val existingThemes = dao.observeAllThemes().first()
            if (existingThemes.isEmpty()) {
                dao.insertThemes(CyberCatalogData.builtInThemes)
            }
            val existingWallpapers = dao.observeWallpapers().first()
            if (existingWallpapers.isEmpty()) {
                dao.insertWallpapers(CyberCatalogData.builtInWallpapers)
            }
            val existingFolders = dao.observeLauncherFolders().first()
            if (existingFolders.isEmpty()) {
                dao.insertLauncherFolder(
                    LauncherFolderEntity(
                        folderName = "Cyber Core",
                        colorHex = "#00FF66",
                        layoutStyle = "2x2 Grid",
                        appPackagesCsv = "com.android.settings,com.cyber.terminal,com.vivo.imanager,com.cyber.files"
                    )
                )
                dao.insertLauncherFolder(
                    LauncherFolderEntity(
                        folderName = "Media & Gaming",
                        colorHex = "#00E5FF",
                        layoutStyle = "Neon Ring",
                        appPackagesCsv = "com.vivo.game,com.cyber.music,com.android.camera,com.android.gallery3d"
                    )
                )
            }
            val existingWidgets = dao.getCustomWidgetsOnce()
            if (existingWidgets.isEmpty()) {
                dao.insertCustomWidget(
                    CustomWidgetEntity(
                        title = "Cyber Matrix Clock",
                        category = "Clock",
                        designPreset = "Cyber Clock",
                        sizeType = "4x2",
                        bgColorHex = "#060D09",
                        accentColorHex = "#00FF66",
                        transparency = 0.9f,
                        blurRadius = 14f,
                        cornerRadiusDp = 24,
                        borderWidthDp = 2,
                        hasShadow = true,
                        fontFamily = "Monospace",
                        fontSizeSp = 28,
                        textAlign = "CENTER",
                        iconName = "schedule",
                        useGradient = true,
                        dataSource = "TIME"
                    )
                )
                dao.insertCustomWidget(
                    CustomWidgetEntity(
                        title = "Vivo FlashCharge Ring",
                        category = "Battery",
                        designPreset = "Circular Battery",
                        sizeType = "2x2",
                        bgColorHex = "#071018",
                        accentColorHex = "#00E5FF",
                        transparency = 0.88f,
                        blurRadius = 12f,
                        cornerRadiusDp = 24,
                        borderWidthDp = 2,
                        hasShadow = true,
                        fontFamily = "Cyber",
                        fontSizeSp = 24,
                        textAlign = "CENTER",
                        iconName = "battery_charging_full",
                        useGradient = true,
                        dataSource = "BATTERY"
                    )
                )
            }
        }
    }

    fun selectTab(tab: MainNavTab) {
        transientState.update { it.copy(selectedTab = tab) }
    }

    fun openStudio(studio: StudioDestination) {
        transientState.update {
            it.copy(
                selectedTab = MainNavTab.CUSTOMIZE,
                selectedStudio = studio
            )
        }
    }

    fun clearToast() {
        transientState.update { it.copy(statusToastMessage = null) }
    }

    private fun postToast(msg: String) {
        transientState.update { it.copy(statusToastMessage = msg) }
    }

    fun refreshDeviceAudit() {
        val audit = SystemIntegrationHelper.inspectDeviceCompatibility(context)
        transientState.update { it.copy(compatibilityAudit = audit) }
        postToast("Vivo T2x 5G & Android 15 compatibility audit refreshed")
    }

    // =========================================================================
    // ONE-CLICK APPLY ENGINE (Section 21)
    // =========================================================================
    fun executeOneClickApply(
        theme: ThemeEntity,
        wallpaperTarget: WallpaperTarget = WallpaperTarget.BOTH
    ) {
        viewModelScope.launch {
            transientState.update {
                it.copy(
                    applyReport = OneClickApplyReportState(
                        isVisible = true,
                        isApplying = true,
                        themeName = theme.name,
                        steps = emptyList(),
                        summaryBanner = "Checking Vivo T2x 5G compatibility & applying components..."
                    )
                )
            }

            val audit = SystemIntegrationHelper.inspectDeviceCompatibility(context)
            val steps = mutableListOf<ApplyStepReportItem>()

            // 1. Apply Wallpaper via WallpaperManager
            val matchingWallpaper = uiState.value.wallpapers.firstOrNull { it.id == theme.wallpaperPresetId }
                ?: WallpaperHistoryEntity(
                    id = theme.wallpaperPresetId,
                    title = "${theme.name} Wallpaper",
                    category = theme.category,
                    resolutionTag = "4K",
                    primaryHex = theme.primaryColorHex,
                    secondaryHex = theme.secondaryColorHex,
                    patternType = "GRID"
                )
            val wpResult = WallpaperAndWidgetRenderer.applyWallpaperToSystem(context, matchingWallpaper, wallpaperTarget)
            if (wpResult.isSuccess) {
                steps.add(
                    ApplyStepReportItem(
                        componentName = "4K System Wallpaper (${wallpaperTarget.name})",
                        status = ApplyComponentStatus.APPLIED_SYSTEM,
                        detailMessage = wpResult.getOrDefault("Applied directly to Android WallpaperManager.")
                    )
                )
            } else {
                steps.add(
                    ApplyStepReportItem(
                        componentName = "4K Wallpaper Engine",
                        status = ApplyComponentStatus.APPLIED_LAUNCHER_AND_APP,
                        detailMessage = "Applied to Cyber Launcher & in-app preview canvas."
                    )
                )
            }

            // 2. Update Active Theme in DataStore & Room
            val updatedTheme = theme.copy(
                isDownloaded = true,
                lastUsedTimestamp = System.currentTimeMillis()
            )
            dao.insertTheme(updatedTheme)
            prefsRepo.applyThemeToPreferences(updatedTheme)

            steps.add(
                ApplyStepReportItem(
                    componentName = "Color Palette, Typography (${theme.fontFamilyName}) & Dock (${theme.dockStyle})",
                    status = ApplyComponentStatus.APPLIED_LAUNCHER_AND_APP,
                    detailMessage = "Synchronized primary (${theme.primaryColorHex}) & secondary (${theme.secondaryColorHex}) tokens across Cyber Theme X & Cyber Launcher."
                )
            )

            // 3. Icon Pack & Folder Layout
            steps.add(
                ApplyStepReportItem(
                    componentName = "Icon Pack (${theme.iconPackId}) & Folders (${theme.folderStyle})",
                    status = ApplyComponentStatus.APPLIED_LAUNCHER_AND_APP,
                    detailMessage = "Applied to Cyber Launcher home grid, app drawer, and Icon Studio."
                )
            )

            // 4. Refresh Real Android Home Screen Widgets
            SystemIntegrationHelper.refreshAllCyberHomeWidgets(context)
            steps.add(
                ApplyStepReportItem(
                    componentName = "Home Screen Widgets (${theme.clockWidgetStyle}, ${theme.batteryWidgetStyle})",
                    status = ApplyComponentStatus.APPLIED_SYSTEM,
                    detailMessage = "Updated all pinned Cyber Theme X AppWidgets & synced Widget Studio presets."
                )
            )

            // 5. Check Default Launcher Role (Requires User Action if not default)
            if (audit.isDefaultLauncher) {
                steps.add(
                    ApplyStepReportItem(
                        componentName = "Default Home Launcher (CYBER LAUNCHER)",
                        status = ApplyComponentStatus.APPLIED_SYSTEM,
                        detailMessage = "CYBER LAUNCHER is active as your default Android Home screen."
                    )
                )
            } else {
                steps.add(
                    ApplyStepReportItem(
                        componentName = "Default Home Screen Replacement",
                        status = ApplyComponentStatus.REQUIRES_USER_ACTION,
                        detailMessage = "Optional: Tap 'Open Cyber Launcher' or set CYBER LAUNCHER as Default Home in Android Settings."
                    )
                )
            }

            // 6. Explicitly report Vivo / Android 15 system-restricted components honestly
            steps.add(
                ApplyStepReportItem(
                    componentName = "Control Center (${theme.controlCenterStyle}) & Notifications (${theme.notificationStyle})",
                    status = ApplyComponentStatus.OS_RESTRICTED_PREVIEW,
                    detailMessage = "Active inside Cyber Launcher & Control Center Studio. Native Vivo Funtouch OS Quick Settings shade is OS-protected."
                )
            )

            transientState.update {
                it.copy(
                    compatibilityAudit = audit,
                    applyReport = OneClickApplyReportState(
                        isVisible = true,
                        isApplying = false,
                        themeName = theme.name,
                        steps = steps,
                        summaryBanner = "Theme '${theme.name}' applied! ${steps.count { s -> s.status == ApplyComponentStatus.APPLIED_SYSTEM || s.status == ApplyComponentStatus.APPLIED_LAUNCHER_AND_APP }} modules updated."
                    )
                )
            }
        }
    }

    fun dismissApplyReport() {
        transientState.update { it.copy(applyReport = it.applyReport.copy(isVisible = false)) }
    }

    // =========================================================================
    // THEME STORE & THEME CREATOR ACTIONS
    // =========================================================================
    fun toggleThemeFavorite(theme: ThemeEntity) {
        viewModelScope.launch {
            dao.updateTheme(theme.copy(isFavorite = !theme.isFavorite))
            postToast(if (!theme.isFavorite) "Added '${theme.name}' to Favorites" else "Removed from Favorites")
        }
    }

    fun downloadOnlineTheme(theme: ThemeEntity) {
        viewModelScope.launch {
            dao.updateTheme(
                theme.copy(
                    isDownloaded = true,
                    downloadsCount = theme.downloadsCount + 1,
                    lastUsedTimestamp = System.currentTimeMillis()
                )
            )
            postToast("Downloaded '${theme.name}' to your offline library")
        }
    }

    fun duplicateTheme(theme: ThemeEntity) {
        viewModelScope.launch {
            val copyId = "custom_${System.currentTimeMillis()}"
            val duplicated = theme.copy(
                id = copyId,
                name = "${theme.name} (Remix)",
                isBuiltIn = false,
                isOnlineStoreItem = false,
                isDownloaded = true,
                lastUsedTimestamp = System.currentTimeMillis()
            )
            dao.insertTheme(duplicated)
            postToast("Duplicated '${theme.name}' as '${duplicated.name}'")
        }
    }

    fun deleteCustomTheme(theme: ThemeEntity) {
        if (theme.isBuiltIn) {
            postToast("Built-in themes cannot be deleted; use Reset instead.")
            return
        }
        viewModelScope.launch {
            dao.deleteCustomTheme(theme.id)
            postToast("Deleted custom theme '${theme.name}'")
        }
    }

    fun saveUserCreatedTheme(
        name: String,
        category: String,
        primaryHex: String,
        secondaryHex: String,
        wallpaperId: String,
        iconPack: String,
        clockStyle: String,
        batteryStyle: String,
        controlCenterStyle: String,
        fontStyle: String,
        dockStyle: String,
        folderStyle: String
    ) {
        viewModelScope.launch {
            val id = "user_theme_${System.currentTimeMillis()}"
            val newTheme = ThemeEntity(
                id = id,
                name = name.ifBlank { "Custom Cyber Theme" },
                category = category,
                description = "User-engineered theme created in Cyber Theme Creator.",
                primaryColorHex = primaryHex,
                secondaryColorHex = secondaryHex,
                backgroundColorHex = "#05080C",
                homeLayoutStyle = "Custom 4x5 Cyber Grid",
                wallpaperPresetId = wallpaperId,
                iconPackId = iconPack,
                clockWidgetStyle = clockStyle,
                batteryWidgetStyle = batteryStyle,
                calendarWidgetStyle = "Digital Calendar",
                musicWidgetStyle = "Cyber Player",
                dockStyle = dockStyle,
                folderStyle = folderStyle,
                controlCenterStyle = controlCenterStyle,
                notificationStyle = "Neon Edge Card",
                fontFamilyName = fontStyle,
                widgetConfigSummary = "$clockStyle + $batteryStyle",
                isBuiltIn = false,
                isOnlineStoreItem = false,
                isDownloaded = true,
                isFavorite = true,
                lastUsedTimestamp = System.currentTimeMillis()
            )
            dao.insertTheme(newTheme)
            postToast("Created & saved theme '${newTheme.name}'")
        }
    }

    fun exportThemeToJson(theme: ThemeEntity): String {
        val obj = JSONObject().apply {
            put("schema", "CYBER_THEME_X_V1")
            put("id", theme.id)
            put("name", theme.name)
            put("category", theme.category)
            put("primaryColorHex", theme.primaryColorHex)
            put("secondaryColorHex", theme.secondaryColorHex)
            put("wallpaperPresetId", theme.wallpaperPresetId)
            put("iconPackId", theme.iconPackId)
            put("clockWidgetStyle", theme.clockWidgetStyle)
            put("batteryWidgetStyle", theme.batteryWidgetStyle)
            put("controlCenterStyle", theme.controlCenterStyle)
            put("fontFamilyName", theme.fontFamilyName)
            put("dockStyle", theme.dockStyle)
            put("folderStyle", theme.folderStyle)
        }
        return obj.toString(2)
    }

    fun shareTheme(theme: ThemeEntity) {
        val json = exportThemeToJson(theme)
        SystemIntegrationHelper.shareThemeJson(context, theme.name, json)
    }

    fun importAndValidateThemeJson(rawJson: String): Boolean {
        return try {
            val obj = JSONObject(rawJson.trim())
            val schema = obj.optString("schema", "")
            if (schema != "CYBER_THEME_X_V1") {
                postToast("Invalid theme JSON: missing CYBER_THEME_X_V1 schema header")
                return false
            }
            val name = obj.optString("name", "Imported Cyber Theme")
            val primary = obj.optString("primaryColorHex", "#00FF66")
            val secondary = obj.optString("secondaryColorHex", "#00E5FF")
            if (!primary.startsWith("#") || !secondary.startsWith("#")) {
                postToast("Validation failed: invalid hex color format")
                return false
            }
            saveUserCreatedTheme(
                name = "$name (Imported)",
                category = obj.optString("category", "Cyber Hacker"),
                primaryHex = primary,
                secondaryHex = secondary,
                wallpaperId = obj.optString("wallpaperPresetId", "wp_hacker_matrix"),
                iconPack = obj.optString("iconPackId", "Cyber Hacker"),
                clockStyle = obj.optString("clockWidgetStyle", "Cyber Clock"),
                batteryStyle = obj.optString("batteryWidgetStyle", "Neon Battery"),
                controlCenterStyle = obj.optString("controlCenterStyle", "Cyber Hacker"),
                fontStyle = obj.optString("fontFamilyName", "Cyber"),
                dockStyle = obj.optString("dockStyle", "Neon Border Floating Dock"),
                folderStyle = obj.optString("folderStyle", "3x3 Matrix Hex Folder")
            )
            true
        } catch (e: Exception) {
            postToast("JSON Validation Error: ${e.localizedMessage ?: "Malformed JSON"}")
            false
        }
    }

    // =========================================================================
    // WALLPAPER STUDIO ACTIONS
    // =========================================================================
    fun applyWallpaper(wallpaper: WallpaperHistoryEntity, target: WallpaperTarget) {
        viewModelScope.launch {
            val updated = wallpaper.copy(
                appliedTarget = target.name,
                lastAppliedAt = System.currentTimeMillis(),
                isDownloaded = true
            )
            dao.insertWallpaper(updated)
            val result = WallpaperAndWidgetRenderer.applyWallpaperToSystem(context, updated, target)
            postToast(result.getOrElse { "Wallpaper applied to Cyber Launcher & Studio Preview" })
        }
    }

    fun updateWallpaperAdjustments(wallpaper: WallpaperHistoryEntity) {
        viewModelScope.launch {
            dao.insertWallpaper(wallpaper)
            postToast("Saved filter adjustments for '${wallpaper.title}'")
        }
    }

    fun toggleWallpaperFavorite(wallpaper: WallpaperHistoryEntity) {
        viewModelScope.launch {
            dao.insertWallpaper(wallpaper.copy(isFavorite = !wallpaper.isFavorite))
        }
    }

    fun addGalleryWallpaper(uriString: String) {
        viewModelScope.launch {
            val id = "wp_gallery_${System.currentTimeMillis()}"
            val entity = WallpaperHistoryEntity(
                id = id,
                title = "Custom Gallery Wallpaper",
                category = "Minimal",
                resolutionTag = "4K",
                primaryHex = "#00FF66",
                secondaryHex = "#00E5FF",
                patternType = "MINIMAL",
                isFavorite = true,
                isDownloaded = true,
                lastAppliedAt = System.currentTimeMillis(),
                customGalleryUri = uriString
            )
            dao.insertWallpaper(entity)
            postToast("Imported image from gallery into Wallpaper Studio")
        }
    }

    // =========================================================================
    // WIDGET STUDIO & WIDGET CREATOR ACTIONS
    // =========================================================================
    fun saveCustomWidget(widget: CustomWidgetEntity) {
        viewModelScope.launch {
            dao.insertCustomWidget(widget)
            SystemIntegrationHelper.refreshAllCyberHomeWidgets(context)
            postToast("Saved custom widget '${widget.title}' & refreshed home widgets")
        }
    }

    fun duplicateCustomWidget(widget: CustomWidgetEntity) {
        viewModelScope.launch {
            dao.insertCustomWidget(
                widget.copy(
                    id = 0,
                    title = "${widget.title} Copy",
                    createdAt = System.currentTimeMillis()
                )
            )
            postToast("Duplicated widget '${widget.title}'")
        }
    }

    fun deleteCustomWidget(widgetId: Int) {
        viewModelScope.launch {
            dao.deleteCustomWidget(widgetId)
            postToast("Deleted custom widget")
        }
    }

    fun pinWidgetToHomeScreen(category: String, title: String) {
        val requested = SystemIntegrationHelper.requestPinCyberWidget(context, category)
        if (requested) {
            postToast("Requested Android OS to pin '$title' to Home Screen")
        } else {
            postToast("Saved '$title'! Long-press your Home Screen -> Widgets -> Cyber Theme X to place it.")
        }
    }

    // =========================================================================
    // ICON STUDIO ACTIONS
    // =========================================================================
    fun updateIconPackPreferences(pack: String, shape: String, neon: Boolean, scale: Float, showLabels: Boolean) {
        viewModelScope.launch {
            prefsRepo.updateIconSettings(pack, shape, neon, scale, showLabels)
            postToast("Applied '$pack' ($shape) to Cyber Launcher & Icon Studio")
        }
    }

    fun saveCustomAppIconOverride(overrideEntity: CustomIconOverrideEntity) {
        viewModelScope.launch {
            dao.insertCustomIcon(overrideEntity)
            postToast("Updated custom icon for ${overrideEntity.appLabel}")
        }
    }

    // =========================================================================
    // CONTROL CENTER STUDIO ACTIONS
    // =========================================================================
    fun toggleControlCenterTile(tileId: String) {
        val currentTiles = transientState.value.controlCenterTiles
        val target = currentTiles.firstOrNull { it.id == tileId } ?: return
        val newActive = !target.isActive

        when (target.systemActionType) {
            "TORCH_HARDWARE" -> {
                val ok = SystemIntegrationHelper.toggleFlashlightTorch(context, newActive)
                postToast(if (ok) "Flashlight Torch ${if (newActive) "ON" else "OFF"}" else "Torch toggled in studio state")
            }
            "TOGGLE_AMOLED" -> {
                viewModelScope.launch {
                    prefsRepo.updateAppearanceMode(
                        if (newActive) AppAppearanceMode.AMOLED_BLACK else AppAppearanceMode.CYBER_DARK
                    )
                }
                postToast("AMOLED Black Mode ${if (newActive) "Enabled" else "Switched to Cyber Dark"}")
            }
            "WIFI_PANEL", "INTERNET_PANEL", "BLUETOOTH_SETTINGS", "AIRPLANE_SETTINGS",
            "LOCATION_SETTINGS", "BATTERY_SAVER", "DISPLAY_SETTINGS", "TETHER_SETTINGS", "SOUND_SETTINGS" -> {
                val msg = SystemIntegrationHelper.launchSystemAction(context, target.systemActionType)
                postToast(msg)
            }
            else -> {
                postToast("${target.label}: ${if (newActive) "Activated" else "Standby"}")
            }
        }

        transientState.update { state ->
            state.copy(
                controlCenterTiles = state.controlCenterTiles.map {
                    if (it.id == tileId) it.copy(isActive = newActive) else it
                }
            )
        }
    }

    fun moveControlCenterTile(index: Int, direction: Int) {
        val list = transientState.value.controlCenterTiles.toMutableList()
        val targetIdx = index + direction
        if (index in list.indices && targetIdx in list.indices) {
            val item = list.removeAt(index)
            list.add(targetIdx, item)
            transientState.update { it.copy(controlCenterTiles = list) }
        }
    }

    fun updateControlCenterSettings(theme: String, blur: Float, alpha: Float) {
        viewModelScope.launch {
            prefsRepo.updateControlCenterSettings(theme, blur, alpha)
            postToast("Saved Control Center theme '$theme'")
        }
    }

    // =========================================================================
    // NOTIFICATION, STATUS BAR, LOCK SCREEN, DIALER, FONT, RGB, CHARGING, SOUND, ANIMATION
    // =========================================================================
    fun updateSystemStudioSelection(
        notificationStyle: String? = null,
        statusBarStyle: String? = null,
        lockScreenStyle: String? = null,
        dialerTheme: String? = null
    ) {
        viewModelScope.launch {
            prefsRepo.updateSystemStudioStyles(notificationStyle, statusBarStyle, lockScreenStyle, dialerTheme)
            postToast("Updated studio configuration")
        }
    }

    fun placeDialerCall(phoneNumber: String) {
        val msg = SystemIntegrationHelper.launchSystemAction(context, "DIAL_NUMBER", phoneNumber)
        postToast(msg)
    }

    fun updateFontPreferences(fontStyle: String, scale: Float) {
        viewModelScope.launch {
            prefsRepo.updateFontSettings(fontStyle, scale)
            SystemIntegrationHelper.refreshAllCyberHomeWidgets(context)
            postToast("Applied '$fontStyle' font to App, Cyber Launcher & Widgets")
        }
    }

    fun updateRgbEdgePreferences(enabled: Boolean, preset: String, speed: Float, thickness: Float) {
        viewModelScope.launch {
            prefsRepo.updateRgbSettings(enabled, preset, speed, thickness)
            postToast("RGB Edge Lighting updated: $preset")
        }
    }

    fun selectChargingAnimation(animName: String) {
        viewModelScope.launch {
            prefsRepo.updateChargingAndSound(chargingAnim = animName)
            postToast("Selected '$animName' charging animation")
        }
    }

    fun selectAndPlaySoundPack(packName: String, role: String = "Ringtone") {
        viewModelScope.launch {
            prefsRepo.updateChargingAndSound(soundPack = packName)
            SystemIntegrationHelper.playCyberSoundPreview(packName, role)
            postToast("Playing synthesized '$packName' ($role)")
        }
    }

    fun updateAnimationPreferences(preset: String, speed: Float, reduceMotion: Boolean) {
        viewModelScope.launch {
            prefsRepo.updateAnimationSettings(preset, speed, reduceMotion)
            postToast("Updated animation physics: $preset")
        }
    }

    // =========================================================================
    // CYBER LAUNCHER & BACKUP / RESTORE / SETTINGS ACTIONS
    // =========================================================================
    fun createLauncherFolder(name: String, colorHex: String, layoutStyle: String, packagesCsv: String) {
        viewModelScope.launch {
            dao.insertLauncherFolder(
                LauncherFolderEntity(
                    folderName = name.ifBlank { "Cyber Folder" },
                    colorHex = colorHex,
                    layoutStyle = layoutStyle,
                    appPackagesCsv = packagesCsv
                )
            )
            postToast("Created folder '$name' on Cyber Launcher")
        }
    }

    fun deleteLauncherFolder(folderId: Int) {
        viewModelScope.launch {
            dao.deleteLauncherFolder(folderId)
            postToast("Removed launcher folder")
        }
    }

    fun updateLauncherSettings(cols: Int, rows: Int, hiddenCsv: String, doubleTap: String) {
        viewModelScope.launch {
            prefsRepo.updateLauncherSettings(cols, rows, hiddenCsv, doubleTap)
            postToast("Updated Cyber Launcher ($cols×$rows grid)")
        }
    }

    fun updateAppearanceMode(mode: AppAppearanceMode) {
        viewModelScope.launch {
            prefsRepo.updateAppearanceMode(mode)
            postToast("Switched appearance mode to ${mode.name.replace('_', ' ')}")
        }
    }

    fun createBackupSnapshot(snapshotName: String) {
        viewModelScope.launch {
            val state = uiState.value
            val payload = exportThemeToJson(state.activeTheme)
            dao.insertBackup(
                BackupSnapshotEntity(
                    snapshotName = snapshotName.ifBlank { "Backup ${state.activeTheme.name}" },
                    activeThemeId = state.prefs.activeThemeId,
                    activeIconPack = state.prefs.activeIconPack,
                    activeControlCenter = state.prefs.activeControlCenterTheme,
                    activeFont = state.prefs.activeFontStyle,
                    widgetsCount = state.customWidgets.size,
                    jsonPayload = payload
                )
            )
            postToast("Created local backup snapshot '$snapshotName'")
        }
    }

    fun restoreBackupSnapshot(backup: BackupSnapshotEntity) {
        viewModelScope.launch {
            val theme = dao.getThemeById(backup.activeThemeId) ?: uiState.value.activeTheme
            prefsRepo.applyThemeToPreferences(theme)
            prefsRepo.updateFontSettings(backup.activeFont, 1.0f)
            postToast("Restored backup '${backup.snapshotName}'")
        }
    }

    fun restorePreviousTheme() {
        viewModelScope.launch {
            val prevId = uiState.value.prefs.previousThemeId
            val prevTheme = dao.getThemeById(prevId) ?: CyberCatalogData.builtInThemes.first()
            executeOneClickApply(prevTheme, WallpaperTarget.HOME)
        }
    }

    fun resetAllCustomizations() {
        viewModelScope.launch {
            prefsRepo.resetAllCustomizations()
            val defaultTheme = CyberCatalogData.builtInThemes.first()
            prefsRepo.applyThemeToPreferences(defaultTheme)
            postToast("Reset all customizations to factory Cyber Hacker defaults")
        }
    }

    fun openSystemPermissionScreen(actionType: String) {
        val msg = SystemIntegrationHelper.launchSystemAction(context, actionType)
        postToast(msg)
    }
}
