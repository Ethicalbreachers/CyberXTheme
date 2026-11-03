package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MainNavTab(val label: String) {
    HOME("Home"),
    THEMES("Themes"),
    WIDGETS("Widgets"),
    CUSTOMIZE("Customize"),
    SETTINGS("Settings")
}

enum class StudioDestination(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String
) {
    HUB("hub", "Customize Hub", "All 15 Personalization Studios", "15 Studios"),
    WALLPAPER("wallpaper", "Wallpaper Studio", "10 Categories • 4K, Live & Filter Editor", "4K + Live"),
    ICONS("icons", "Icon Studio", "10 Icon Packs • Shape, Neon & Per-App Editor", "10 Packs"),
    LAUNCHER("launcher", "Cyber Launcher", "Multi-Page Home, App Drawer, Folders & Grid", "Home Role"),
    WIDGET_CREATOR("widget_creator", "Widget Creator", "No-Code Visual Designer & Home Pinning", "70+ Styles"),
    CONTROL_CENTER("control_center", "Control Center Studio", "10 Themes • Drag Editor & System Tiles", "10 Themes"),
    NOTIFICATIONS("notifications", "Notification Studio", "10 Card Designs • Privacy-Safe Listener", "10 Cards"),
    STATUS_BAR("status_bar", "Status Bar Studio", "10 Visual Styles • Clock, Battery & Signal", "10 Styles"),
    LOCK_SCREEN("lock_screen", "Lock Screen Studio", "10 AOD & Lock Previews • Clocks & Shortcuts", "10 Designs"),
    DIALER("dialer", "Dialer Studio", "10 Dialer Themes • T9 Keypad & Call Simulator", "Telecom"),
    FONTS("fonts", "Font Studio", "10 Bundled Google Fonts • Size & Weight", "10 Fonts"),
    RGB_EDGE("rgb_edge", "RGB & Edge Lighting", "12 Presets • Neon Borders & Speed Control", "12 Presets"),
    CHARGING("charging", "Charging Animation", "10 Live Battery & Power Animations", "10 FX"),
    SOUNDS("sounds", "Sound Studio", "10 Synth Audio Packs • Ringtone & UI Player", "10 Packs"),
    ANIMATIONS("animations", "Animation Studio", "10 Physics Presets & Reduce Motion", "10 Presets"),
    THEME_CREATOR("theme_creator", "Theme Creator", "Build, Validate, Export & Share Full Themes", "Builder"),
    COMPATIBILITY("compatibility", "Vivo T2x 5G Center", "Android 15 & Funtouch OS Compatibility Audit", "Vivo T2x 5G")
}

@Entity(tableName = "themes")
data class ThemeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val description: String,
    val primaryColorHex: String,
    val secondaryColorHex: String,
    val backgroundColorHex: String,
    val homeLayoutStyle: String,
    val wallpaperPresetId: String,
    val iconPackId: String,
    val clockWidgetStyle: String,
    val batteryWidgetStyle: String,
    val calendarWidgetStyle: String,
    val musicWidgetStyle: String,
    val dockStyle: String,
    val folderStyle: String,
    val controlCenterStyle: String,
    val notificationStyle: String,
    val fontFamilyName: String,
    val widgetConfigSummary: String,
    val isBuiltIn: Boolean = true,
    val isOnlineStoreItem: Boolean = false,
    val isDownloaded: Boolean = true,
    val isFavorite: Boolean = false,
    val lastUsedTimestamp: Long = 0L,
    val downloadsCount: Int = 1200
)

@Entity(tableName = "custom_widgets")
data class CustomWidgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val designPreset: String,
    val sizeType: String, // "2x2", "4x2", "4x4"
    val bgColorHex: String,
    val accentColorHex: String,
    val transparency: Float,
    val blurRadius: Float,
    val cornerRadiusDp: Int,
    val borderWidthDp: Int,
    val hasShadow: Boolean,
    val fontFamily: String,
    val fontSizeSp: Int,
    val textAlign: String, // "LEFT", "CENTER", "RIGHT"
    val iconName: String,
    val useGradient: Boolean,
    val dataSource: String, // "TIME", "BATTERY", "CALENDAR", "WEATHER", "MUSIC", "STATS", "NOTES"
    val customNoteText: String = "CYBER SYSTEM ONLINE",
    val boundAppWidgetId: Int = -1,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_icons")
data class CustomIconOverrideEntity(
    @PrimaryKey val targetPackageOrId: String,
    val appLabel: String,
    val customLabel: String,
    val packName: String,
    val shape: String, // "Rounded", "Square", "Circular", "Squircle", "Hexagon"
    val bgColorHex: String,
    val fgColorHex: String,
    val neonBorder: Boolean,
    val useGradient: Boolean,
    val hasShadow: Boolean,
    val sizeScale: Float,
    val showLabel: Boolean,
    val customImageUri: String? = null
)

@Entity(tableName = "wallpaper_history")
data class WallpaperHistoryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val resolutionTag: String, // "HD", "4K", "LIVE", "ANIMATED"
    val primaryHex: String,
    val secondaryHex: String,
    val patternType: String, // "MATRIX", "GRID", "AMOLED_RING", "NEBULA", "CYBER_CITY", "HEX", "WAVES", "MINIMAL", "PRISM_3D", "ANIME_SLASH"
    val blurRadius: Float = 0f,
    val brightness: Float = 1f,
    val colorFilterHex: String = "#00000000",
    val isFavorite: Boolean = false,
    val isDownloaded: Boolean = true,
    val appliedTarget: String = "NONE", // "HOME", "LOCK", "BOTH", "NONE"
    val lastAppliedAt: Long = 0L,
    val customGalleryUri: String? = null
)

@Entity(tableName = "launcher_folders")
data class LauncherFolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val folderName: String,
    val colorHex: String,
    val layoutStyle: String, // "2x2 Grid", "3x3 Matrix", "Stack Card", "Neon Ring"
    val appPackagesCsv: String,
    val pageIndex: Int = 0
)

@Entity(tableName = "backup_snapshots")
data class BackupSnapshotEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val snapshotName: String,
    val activeThemeId: String,
    val activeIconPack: String,
    val activeControlCenter: String,
    val activeFont: String,
    val widgetsCount: Int,
    val jsonPayload: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class WidgetPresetCatalogItem(
    val id: String,
    val title: String,
    val category: String,
    val designStyle: String,
    val defaultSize: String,
    val accentHex: String,
    val secondaryHex: String,
    val dataSource: String,
    val previewMetric: String,
    val subtitle: String
)

data class ControlCenterTileItem(
    val id: String,
    val label: String,
    val subtitle: String,
    val isActive: Boolean,
    val systemActionType: String
)

data class ApplyStepReportItem(
    val componentName: String,
    val status: ApplyComponentStatus,
    val detailMessage: String
)

enum class ApplyComponentStatus {
    APPLIED_SYSTEM,
    APPLIED_LAUNCHER_AND_APP,
    REQUIRES_USER_ACTION,
    OS_RESTRICTED_PREVIEW
}

object CyberCatalogData {

    val themeCategories = listOf(
        "All",
        "Cyber Hacker",
        "Neon Green",
        "RGB Gaming",
        "AMOLED Black",
        "iOS Inspired",
        "Nothing OS",
        "Royal Gold",
        "Glassmorphism",
        "Purple Neon",
        "Futuristic Pro"
    )

    val builtInThemes: List<ThemeEntity> = listOf(
        ThemeEntity(
            id = "theme_cyber_hacker",
            name = "Cyber Hacker OS",
            category = "Cyber Hacker",
            description = "Terminal matrix typography, surgical #00FF66 neon borders, hacker telemetry widgets, and encrypted dark dock.",
            primaryColorHex = "#00FF66",
            secondaryColorHex = "#00E5FF",
            backgroundColorHex = "#040806",
            homeLayoutStyle = "4x5 Matrix Grid + Top Terminal HUD",
            wallpaperPresetId = "wp_hacker_matrix",
            iconPackId = "Cyber Hacker",
            clockWidgetStyle = "Cyber Clock",
            batteryWidgetStyle = "Neon Battery",
            calendarWidgetStyle = "Digital Calendar",
            musicWidgetStyle = "Cyber Player",
            dockStyle = "Neon Border Floating Dock",
            folderStyle = "3x3 Matrix Hex Folder",
            controlCenterStyle = "Cyber Hacker",
            notificationStyle = "Terminal Code Card",
            fontFamilyName = "Monospace",
            widgetConfigSummary = "Cyber Clock + Neon Battery + Network Speed HUD",
            isBuiltIn = true,
            isFavorite = true,
            lastUsedTimestamp = System.currentTimeMillis(),
            downloadsCount = 18420
        ),
        ThemeEntity(
            id = "theme_neon_green",
            name = "Neon Green Overdrive",
            category = "Neon Green",
            description = "High-voltage acid green highlights over deep obsidian glass with glowing circular rings.",
            primaryColorHex = "#00E676",
            secondaryColorHex = "#76FF03",
            backgroundColorHex = "#050B07",
            homeLayoutStyle = "4x5 Balanced + Dual Ring Widgets",
            wallpaperPresetId = "wp_cyber_grid",
            iconPackId = "Neon Green",
            clockWidgetStyle = "Neon Clock",
            batteryWidgetStyle = "Circular Battery",
            calendarWidgetStyle = "Minimal Calendar",
            musicWidgetStyle = "Neon Player",
            dockStyle = "Frosted Neon Pill Dock",
            folderStyle = "Neon Ring Folder",
            controlCenterStyle = "Cyber Hacker",
            notificationStyle = "Neon Edge Card",
            fontFamilyName = "Cyber",
            widgetConfigSummary = "Neon Clock + Circular Battery + Neon Music",
            isBuiltIn = true,
            isFavorite = true,
            lastUsedTimestamp = System.currentTimeMillis() - 3600_000L,
            downloadsCount = 14300
        ),
        ThemeEntity(
            id = "theme_rgb_gaming",
            name = "RGB Gaming Apex",
            category = "RGB Gaming",
            description = "Chroma rainbow edge lighting, aggressive hexagonal icons, FPS/RAM telemetry widgets, and gaming control deck.",
            primaryColorHex = "#FF007F",
            secondaryColorHex = "#00E5FF",
            backgroundColorHex = "#080510",
            homeLayoutStyle = "5x5 Pro Gamer Grid + Telemetry Deck",
            wallpaperPresetId = "wp_gaming_rgb",
            iconPackId = "RGB Gaming",
            clockWidgetStyle = "Digital Clock",
            batteryWidgetStyle = "Gradient Battery",
            calendarWidgetStyle = "Dark Calendar",
            musicWidgetStyle = "RGB Player",
            dockStyle = "Chroma Rainbow Bar",
            folderStyle = "Hex Armor Folder",
            controlCenterStyle = "RGB Neon",
            notificationStyle = "RGB Chroma Banner",
            fontFamilyName = "Gaming",
            widgetConfigSummary = "RGB Clock + Device Info + Charging Booster",
            isBuiltIn = true,
            isFavorite = false,
            lastUsedTimestamp = System.currentTimeMillis() - 7200_000L,
            downloadsCount = 22910
        ),
        ThemeEntity(
            id = "theme_amoled_black",
            name = "AMOLED Pitch Black Pro",
            category = "AMOLED Black",
            description = "Zero-power #000000 pixels with ultra-crisp monochrome outlines for maximum battery efficiency on Vivo T2x 5G.",
            primaryColorHex = "#FFFFFF",
            secondaryColorHex = "#00FF66",
            backgroundColorHex = "#000000",
            homeLayoutStyle = "Minimalist 4x4 Clean Stack",
            wallpaperPresetId = "wp_amoled_eclipse",
            iconPackId = "AMOLED",
            clockWidgetStyle = "AMOLED Clock",
            batteryWidgetStyle = "Minimal Battery",
            calendarWidgetStyle = "Minimal Calendar",
            musicWidgetStyle = "AMOLED Player",
            dockStyle = "Stealth Invisible Dock",
            folderStyle = "Pure Outline Folder",
            controlCenterStyle = "AMOLED Black",
            notificationStyle = "OLED Hairline Card",
            fontFamilyName = "Minimal",
            widgetConfigSummary = "AMOLED Clock + Percentage Battery",
            isBuiltIn = true,
            isFavorite = true,
            lastUsedTimestamp = System.currentTimeMillis() - 10800_000L,
            downloadsCount = 31500
        ),
        ThemeEntity(
            id = "theme_ios_inspired",
            name = "Cupertino Glass 18",
            category = "iOS Inspired",
            description = "Silky squircle icons, multi-layer gaussian blur cards, depth clock typography, and translucent control center.",
            primaryColorHex = "#0A84FF",
            secondaryColorHex = "#5E5CE6",
            backgroundColorHex = "#0B132B",
            homeLayoutStyle = "4x6 Squircle Grid + Smart Stack",
            wallpaperPresetId = "wp_abstract_aurora",
            iconPackId = "iOS Inspired",
            clockWidgetStyle = "iOS Inspired Clock",
            batteryWidgetStyle = "Glass Battery",
            calendarWidgetStyle = "Date Card",
            musicWidgetStyle = "Album Art Player",
            dockStyle = "Frosted Glass Cushion Dock",
            folderStyle = "Blurred 3x3 Squircle Folder",
            controlCenterStyle = "iOS Inspired",
            notificationStyle = "Frosted Stack Card",
            fontFamilyName = "Rounded",
            widgetConfigSummary = "iOS Clock + 4-Ring Battery + Weather Card",
            isBuiltIn = true,
            downloadsCount = 27840
        ),
        ThemeEntity(
            id = "theme_nothing_os",
            name = "Nothing Dot Matrix 3.0",
            category = "Nothing OS",
            description = "Industrial dot-matrix typography, monochrome grayscale widgets with crimson red accents and raw telemetry.",
            primaryColorHex = "#D71921",
            secondaryColorHex = "#FFFFFF",
            backgroundColorHex = "#0A0A0A",
            homeLayoutStyle = "Asymmetric Dot Grid",
            wallpaperPresetId = "wp_minimal_dots",
            iconPackId = "Nothing Dots",
            clockWidgetStyle = "Vertical Clock",
            batteryWidgetStyle = "Percentage Battery",
            calendarWidgetStyle = "Event Calendar",
            musicWidgetStyle = "Minimal Player",
            dockStyle = "Monochrome Pill Dock",
            folderStyle = "Dot Matrix Circle Folder",
            controlCenterStyle = "Nothing OS",
            notificationStyle = "Dot Header glyph Card",
            fontFamilyName = "Monospace",
            widgetConfigSummary = "Dot Matrix Clock + Red Accent Weather + Vinyl Player",
            isBuiltIn = true,
            downloadsCount = 19650
        ),
        ThemeEntity(
            id = "theme_royal_gold",
            name = "Imperial Royal Gold",
            category = "Royal Gold",
            description = "Brushed 24K champagne gold accents over velvet onyx with serif luxury horology clock widgets.",
            primaryColorHex = "#FFD700",
            secondaryColorHex = "#FFA000",
            backgroundColorHex = "#0D0B06",
            homeLayoutStyle = "Luxury Centered Crest Layout",
            wallpaperPresetId = "wp_3d_gold_poly",
            iconPackId = "Royal Gold",
            clockWidgetStyle = "Analog Clock",
            batteryWidgetStyle = "Premium Battery",
            calendarWidgetStyle = "Premium Calendar",
            musicWidgetStyle = "Premium Player",
            dockStyle = "Gilded Bevel Dock",
            folderStyle = "Crest Frame Folder",
            controlCenterStyle = "Minimal Dark",
            notificationStyle = "Gold Filigree Card",
            fontFamilyName = "Elegant",
            widgetConfigSummary = "Chronograph Analog Clock + Gold Reserve Battery",
            isBuiltIn = true,
            downloadsCount = 11200
        ),
        ThemeEntity(
            id = "theme_glassmorphism",
            name = "Crystal Prism Glass",
            category = "Glassmorphism",
            description = "Refractive frosted acrylic cards with cyan-violet specular borders and floating translucent widgets.",
            primaryColorHex = "#00E5FF",
            secondaryColorHex = "#E040FB",
            backgroundColorHex = "#09111E",
            homeLayoutStyle = "Layered Glass Cards 4x5",
            wallpaperPresetId = "wp_space_nebula",
            iconPackId = "Glass Icons",
            clockWidgetStyle = "Glass Clock",
            batteryWidgetStyle = "Glass Battery",
            calendarWidgetStyle = "Glass Calendar",
            musicWidgetStyle = "Glass Player",
            dockStyle = "Crystal Blur Dock",
            folderStyle = "Acrylic Pane Folder",
            controlCenterStyle = "Glassmorphism",
            notificationStyle = "Translucent Ice Card",
            fontFamilyName = "Modern",
            widgetConfigSummary = "Glass Clock + Glass Weather + Acrylic Music",
            isBuiltIn = true,
            downloadsCount = 24100
        ),
        ThemeEntity(
            id = "theme_purple_neon",
            name = "Synthwave Purple Neon",
            category = "Purple Neon",
            description = "Outrun ultraviolet and electric magenta laser grids with retro-futuristic synth instrumentation.",
            primaryColorHex = "#B388FF",
            secondaryColorHex = "#FF4081",
            backgroundColorHex = "#0D0518",
            homeLayoutStyle = "Synth Horizon 4x5 Grid",
            wallpaperPresetId = "wp_anime_cyber_city",
            iconPackId = "Purple Neon",
            clockWidgetStyle = "Large Clock",
            batteryWidgetStyle = "Vertical Battery",
            calendarWidgetStyle = "Monthly Calendar",
            musicWidgetStyle = "Large Music Card",
            dockStyle = "Ultraviolet Laser Dock",
            folderStyle = "Neon Violet Portal Folder",
            controlCenterStyle = "Purple Glass",
            notificationStyle = "Synthwave Glow Card",
            fontFamilyName = "Cyber",
            widgetConfigSummary = "Large Outrun Clock + Synth Music + UV Battery",
            isBuiltIn = true,
            downloadsCount = 16890
        ),
        ThemeEntity(
            id = "theme_futuristic_pro",
            name = "Futuristic Pro HUD X",
            category = "Futuristic Pro",
            description = "Orbital spacecraft cockpit telemetry designed specifically for Vivo T2x 5G with live CPU/RAM/Network readouts.",
            primaryColorHex = "#00E5FF",
            secondaryColorHex = "#00FF66",
            backgroundColorHex = "#040A12",
            homeLayoutStyle = "Cockpit Telemetry 5x6 Grid",
            wallpaperPresetId = "wp_3d_quantum_core",
            iconPackId = "Minimal White",
            clockWidgetStyle = "Cyber Clock",
            batteryWidgetStyle = "Charging Battery",
            calendarWidgetStyle = "Agenda Calendar",
            musicWidgetStyle = "Compact Player",
            dockStyle = "Orbital Command Bar",
            folderStyle = "HUD Radar Folder",
            controlCenterStyle = "Futuristic Gaming",
            notificationStyle = "HUD Alert Module",
            fontFamilyName = "Bold",
            widgetConfigSummary = "Orbital Clock + System Telemetry + Radar Battery",
            isBuiltIn = true,
            downloadsCount = 21450
        ),
        // Online Store Catalog Themes (Downloadable in one tap)
        ThemeEntity(
            id = "theme_online_samurai",
            name = "Night City Ronin 2099",
            category = "RGB Gaming",
            description = "Online Store Exclusive: Cybernetic yellow and crimson visor HUD with katana slash transitions.",
            primaryColorHex = "#FCEE09",
            secondaryColorHex = "#FF003C",
            backgroundColorHex = "#0B0A05",
            homeLayoutStyle = "Visor Tactical 4x5",
            wallpaperPresetId = "wp_cyber_city_night",
            iconPackId = "Cyber Hacker",
            clockWidgetStyle = "Cyber Clock",
            batteryWidgetStyle = "Horizontal Battery",
            calendarWidgetStyle = "Digital Calendar",
            musicWidgetStyle = "Cyber Player",
            dockStyle = "Tactical Yellow Bar",
            folderStyle = "Visor Hex Folder",
            controlCenterStyle = "Futuristic Gaming",
            notificationStyle = "Tactical Visor Alert",
            fontFamilyName = "Gaming",
            widgetConfigSummary = "Ronin Clock + Tactical Battery",
            isBuiltIn = false,
            isOnlineStoreItem = true,
            isDownloaded = false,
            downloadsCount = 34200
        ),
        ThemeEntity(
            id = "theme_online_emerald_zen",
            name = "Bioluminescent Forest",
            category = "Glassmorphism",
            description = "Online Store Exclusive: Organic emerald glassmorphism with serene botanical neon accents.",
            primaryColorHex = "#1DE9B6",
            secondaryColorHex = "#A7FFEB",
            backgroundColorHex = "#04120E",
            homeLayoutStyle = "Organic Flow 4x5",
            wallpaperPresetId = "wp_nature_biolum",
            iconPackId = "Glass Icons",
            clockWidgetStyle = "Minimal Clock",
            batteryWidgetStyle = "Circular Battery",
            calendarWidgetStyle = "Glass Calendar",
            musicWidgetStyle = "Glass Player",
            dockStyle = "Emerald Glass Dock",
            folderStyle = "Leaf Glass Folder",
            controlCenterStyle = "Blue Glass",
            notificationStyle = "Botanical Glass Card",
            fontFamilyName = "Rounded",
            widgetConfigSummary = "Zen Clock + Bio Battery + Weather",
            isBuiltIn = false,
            isOnlineStoreItem = true,
            isDownloaded = false,
            downloadsCount = 9840
        ),
        ThemeEntity(
            id = "theme_online_cobalt_ice",
            name = "Sub-Zero Cryo Glass",
            category = "Futuristic Pro",
            description = "Online Store Exclusive: Liquid nitrogen blue glass panels with thermal telemetry widgets.",
            primaryColorHex = "#40C4FF",
            secondaryColorHex = "#18FFFF",
            backgroundColorHex = "#030B14",
            homeLayoutStyle = "Cryo Chamber 4x5",
            wallpaperPresetId = "wp_3d_quantum_core",
            iconPackId = "Glass Icons",
            clockWidgetStyle = "Glass Clock",
            batteryWidgetStyle = "Vertical Battery",
            calendarWidgetStyle = "Dark Calendar",
            musicWidgetStyle = "Glass Player",
            dockStyle = "Ice Prism Dock",
            folderStyle = "Cryo Hex Folder",
            controlCenterStyle = "Blue Glass",
            notificationStyle = "Cryo Frost Banner",
            fontFamilyName = "Modern",
            widgetConfigSummary = "Cryo Clock + Thermal Battery",
            isBuiltIn = false,
            isOnlineStoreItem = true,
            isDownloaded = false,
            downloadsCount = 15600
        )
    )

    val wallpaperCategories = listOf(
        "All", "Hacker", "Cyberpunk", "AMOLED", "Gaming", "Anime", "Nature", "Space", "Abstract", "Minimal", "3D"
    )

    val builtInWallpapers: List<WallpaperHistoryEntity> = listOf(
        WallpaperHistoryEntity("wp_hacker_matrix", "Matrix Code Rain 4K", "Hacker", "LIVE", "#00FF66", "#003311", "MATRIX", isFavorite = true),
        WallpaperHistoryEntity("wp_hacker_terminal", "Zero-Day Root Map", "Hacker", "4K", "#00E676", "#00E5FF", "HEX"),
        WallpaperHistoryEntity("wp_cyber_grid", "Neon Horizon Grid", "Cyberpunk", "ANIMATED", "#00E5FF", "#FF007F", "GRID", isFavorite = true),
        WallpaperHistoryEntity("wp_cyber_city_night", "Neo-Tokyo Rain Sector", "Cyberpunk", "4K", "#FCEE09", "#00E5FF", "CYBER_CITY"),
        WallpaperHistoryEntity("wp_amoled_eclipse", "Pitch Black Solar Ring", "AMOLED", "4K", "#00FF66", "#000000", "AMOLED_RING", isFavorite = true),
        WallpaperHistoryEntity("wp_amoled_void", "Pure OLED Horizon", "AMOLED", "HD", "#00E5FF", "#000000", "MINIMAL"),
        WallpaperHistoryEntity("wp_gaming_rgb", "Chroma Hex Core", "Gaming", "LIVE", "#FF007F", "#7C4DFF", "HEX"),
        WallpaperHistoryEntity("wp_gaming_mecha", "Valiant Mecha HUD", "Gaming", "4K", "#FF3D00", "#00E5FF", "PRISM_3D"),
        WallpaperHistoryEntity("wp_anime_cyber_city", "Akira Synth Drift", "Anime", "ANIMATED", "#B388FF", "#FF4081", "ANIME_SLASH"),
        WallpaperHistoryEntity("wp_nature_biolum", "Bioluminescent Canopy", "Nature", "4K", "#1DE9B6", "#004D40", "WAVES"),
        WallpaperHistoryEntity("wp_space_nebula", "Andromeda Quantum Nebula", "Space", "4K", "#7C4DFF", "#00E5FF", "NEBULA"),
        WallpaperHistoryEntity("wp_abstract_aurora", "Holographic Silk Wave", "Abstract", "4K", "#0A84FF", "#E040FB", "WAVES"),
        WallpaperHistoryEntity("wp_minimal_dots", "Nothing Monolith Grid", "Minimal", "HD", "#D71921", "#212121", "MINIMAL"),
        WallpaperHistoryEntity("wp_3d_quantum_core", "Tesseract 3D Lattice", "3D", "LIVE", "#00E5FF", "#00FF66", "PRISM_3D"),
        WallpaperHistoryEntity("wp_3d_gold_poly", "Obsidian & 24K Facets", "3D", "4K", "#FFD700", "#1A1405", "PRISM_3D")
    )

    val iconPacks = listOf(
        "Cyber Hacker",
        "Neon Green",
        "RGB Gaming",
        "AMOLED",
        "iOS Inspired",
        "Nothing Dots",
        "Purple Neon",
        "Glass Icons",
        "Royal Gold",
        "Minimal White"
    )

    val iconShapes = listOf("Rounded", "Square", "Circular", "Squircle", "Hexagon")

    val widgetCategories = listOf(
        "Clock",
        "Calendar",
        "Battery",
        "Music Player",
        "Weather",
        "Statistics",
        "Additional"
    )

    // 70 Complete Widget Designs (10 in every single one of the 7 categories)
    val widgetStudioCatalog: List<WidgetPresetCatalogItem> = listOf(
        // A. CLOCK WIDGETS (10)
        WidgetPresetCatalogItem("clk_1", "Digital Clock", "Clock", "Digital", "2x2", "#00FF66", "#070A0E", "TIME", "09:41", "Crisp 24h LED readout"),
        WidgetPresetCatalogItem("clk_2", "Analog Clock", "Clock", "Analog", "2x2", "#FFD700", "#0D0B06", "TIME", "10:10", "Precision sweep hands"),
        WidgetPresetCatalogItem("clk_3", "Neon Clock", "Clock", "Neon", "4x2", "#00E676", "#05100A", "TIME", "21:45", "Dual-layer neon tube glow"),
        WidgetPresetCatalogItem("clk_4", "Cyber Clock", "Clock", "Cyber", "4x2", "#00FF66", "#00E5FF", "TIME", "CYB//09:41", "Hacker terminal milliseconds"),
        WidgetPresetCatalogItem("clk_5", "Glass Clock", "Clock", "Glass", "4x2", "#00E5FF", "#1A2639", "TIME", "09:41 AM", "Frosted acrylic blur card"),
        WidgetPresetCatalogItem("clk_6", "Minimal Clock", "Clock", "Minimal", "2x2", "#FFFFFF", "#121212", "TIME", "09 41", "Ultra-clean typographic stack"),
        WidgetPresetCatalogItem("clk_7", "Large Clock", "Clock", "Large", "4x4", "#B388FF", "#0E071B", "TIME", "09:41", "Full-width statement display"),
        WidgetPresetCatalogItem("clk_8", "Vertical Clock", "Clock", "Vertical", "2x2", "#D71921", "#0A0A0A", "TIME", "09\n41", "Stacked vertical dot-matrix"),
        WidgetPresetCatalogItem("clk_9", "iOS Inspired Clock", "Clock", "iOS", "2x2", "#0A84FF", "#101B35", "TIME", "09:41", "Cupertino depth font"),
        WidgetPresetCatalogItem("clk_10", "AMOLED Clock", "Clock", "AMOLED", "2x2", "#00FF66", "#000000", "TIME", "09:41", "True #000000 pixel saver"),

        // B. CALENDAR WIDGETS (10)
        WidgetPresetCatalogItem("cal_1", "Monthly Calendar", "Calendar", "Monthly", "4x4", "#00FF66", "#08100C", "CALENDAR", "NOV 2026", "Full 7-column month matrix"),
        WidgetPresetCatalogItem("cal_2", "Daily Calendar", "Calendar", "Daily", "2x2", "#00E5FF", "#08141E", "CALENDAR", "MON 02", "Big day number + weekday"),
        WidgetPresetCatalogItem("cal_3", "Date Card", "Calendar", "Card", "4x2", "#0A84FF", "#0E182B", "CALENDAR", "Nov 2 • 3 Events", "Compact date + next event"),
        WidgetPresetCatalogItem("cal_4", "Event Calendar", "Calendar", "Events", "4x2", "#D71921", "#101010", "CALENDAR", "14:00 Cyber Sync", "Upcoming schedule timeline"),
        WidgetPresetCatalogItem("cal_5", "Minimal Calendar", "Calendar", "Minimal", "2x2", "#FFFFFF", "#0A0A0A", "CALENDAR", "02 NOV", "Zero-clutter monochrome"),
        WidgetPresetCatalogItem("cal_6", "Glass Calendar", "Calendar", "Glass", "4x2", "#18FFFF", "#132232", "CALENDAR", "WEEK 45", "Frosted glass week strip"),
        WidgetPresetCatalogItem("cal_7", "Dark Calendar", "Calendar", "Dark", "4x2", "#7C4DFF", "#090612", "CALENDAR", "NOV • Q4", "Obsidian stealth agenda"),
        WidgetPresetCatalogItem("cal_8", "Agenda Calendar", "Calendar", "Agenda", "4x4", "#00E676", "#07120D", "CALENDAR", "3 Tasks Today", "Task + calendar hybrid"),
        WidgetPresetCatalogItem("cal_9", "Digital Calendar", "Calendar", "Digital", "4x2", "#00FF66", "#030805", "CALENDAR", "2026.11.02", "ISO hacker timestamp card"),
        WidgetPresetCatalogItem("cal_10", "Premium Calendar", "Calendar", "Premium", "4x2", "#FFD700", "#120F07", "CALENDAR", "NOVEMBER", "Gold-trimmed executive date"),

        // C. BATTERY WIDGETS (10)
        WidgetPresetCatalogItem("bat_1", "Circular Battery", "Battery", "Circular", "2x2", "#00FF66", "#06100A", "BATTERY", "88%", "360° neon power ring"),
        WidgetPresetCatalogItem("bat_2", "Vertical Battery", "Battery", "Vertical", "2x2", "#B388FF", "#0D0718", "BATTERY", "88% ⚡", "Vertical plasma cell bar"),
        WidgetPresetCatalogItem("bat_3", "Horizontal Battery", "Battery", "Horizontal", "4x2", "#00E5FF", "#06131C", "BATTERY", "88% • 44W", "Wide cell meter + voltage"),
        WidgetPresetCatalogItem("bat_4", "Neon Battery", "Battery", "Neon", "2x2", "#00FF66", "#030B06", "BATTERY", "88%", "Glowing cyber capacitor"),
        WidgetPresetCatalogItem("bat_5", "Glass Battery", "Battery", "Glass", "4x2", "#0A84FF", "#122038", "BATTERY", "Phone 88% • Buds 95%", "Multi-device glass rings"),
        WidgetPresetCatalogItem("bat_6", "Minimal Battery", "Battery", "Minimal", "2x2", "#FFFFFF", "#000000", "BATTERY", "88", "Pure numeric OLED readout"),
        WidgetPresetCatalogItem("bat_7", "Gradient Battery", "Battery", "Gradient", "4x2", "#FF007F", "#00E5FF", "BATTERY", "88% RGB", "Rainbow spectrum bar"),
        WidgetPresetCatalogItem("bat_8", "Percentage Battery", "Battery", "Percentage", "2x2", "#D71921", "#0C0C0C", "BATTERY", "88%", "Dot-matrix giant percent"),
        WidgetPresetCatalogItem("bat_9", "Charging Battery", "Battery", "Charging", "4x2", "#00E676", "#04120A", "BATTERY", "FLASH CHARGE", "Vivo FlashCharge telemetry"),
        WidgetPresetCatalogItem("bat_10", "Premium Battery", "Battery", "Premium", "2x2", "#FFD700", "#120E05", "BATTERY", "88% PRO", "Chronograph power reserve"),

        // D. MUSIC PLAYER WIDGETS (10)
        WidgetPresetCatalogItem("mus_1", "Album Art Player", "Music Player", "Album", "4x2", "#0A84FF", "#101B2E", "MUSIC", "Cyberia Mix", "Vinyl sleeve + transport controls"),
        WidgetPresetCatalogItem("mus_2", "Glass Player", "Music Player", "Glass", "4x2", "#00E5FF", "#142436", "MUSIC", "Synth Horizon", "Frosted glass waveform deck"),
        WidgetPresetCatalogItem("mus_3", "Neon Player", "Music Player", "Neon", "4x2", "#00FF66", "#051009", "MUSIC", "Matrix Bass", "Equalizer bars + neon buttons"),
        WidgetPresetCatalogItem("mus_4", "Cyber Player", "Music Player", "Cyber", "4x2", "#FCEE09", "#0D0B04", "MUSIC", "Night City FM", "Tactical cassette HUD"),
        WidgetPresetCatalogItem("mus_5", "Minimal Player", "Music Player", "Minimal", "4x2", "#FFFFFF", "#0E0E0E", "MUSIC", "Track 04", "Clean typographic transport"),
        WidgetPresetCatalogItem("mus_6", "Large Music Card", "Music Player", "Large", "4x4", "#B388FF", "#120824", "MUSIC", "Outrun 1984", "Full lyrics + spectrum analyzer"),
        WidgetPresetCatalogItem("mus_7", "Compact Player", "Music Player", "Compact", "2x2", "#00E676", "#07120C", "MUSIC", "▶ PLAY", "Mini pocket controller"),
        WidgetPresetCatalogItem("mus_8", "RGB Player", "Music Player", "RGB", "4x2", "#FF007F", "#0C0514", "MUSIC", "Chroma Beats", "Rainbow reactive visualizer"),
        WidgetPresetCatalogItem("mus_9", "AMOLED Player", "Music Player", "AMOLED", "4x2", "#00FF66", "#000000", "MUSIC", "Dark Pulse", "Zero-backlit OLED deck"),
        WidgetPresetCatalogItem("mus_10", "Premium Player", "Music Player", "Premium", "4x2", "#FFD700", "#141006", "MUSIC", "Hi-Res FLAC", "Audiophile gold DAC VU meter"),

        // E. WEATHER WIDGETS (10)
        WidgetPresetCatalogItem("wth_1", "Current Weather", "Weather", "Current", "2x2", "#00E5FF", "#081726", "WEATHER", "24°C Clear", "Live condition + humidity"),
        WidgetPresetCatalogItem("wth_2", "Hourly Forecast", "Weather", "Hourly", "4x2", "#0A84FF", "#0D1C33", "WEATHER", "24° • 26° • 23°", "6-hour timeline strip"),
        WidgetPresetCatalogItem("wth_3", "Weekly Forecast", "Weather", "Weekly", "4x4", "#00FF66", "#06120B", "WEATHER", "7-Day Radar", "Full week high/low matrix"),
        WidgetPresetCatalogItem("wth_4", "Temperature Card", "Weather", "Temp", "2x2", "#FF4081", "#180812", "WEATHER", "24° / 18°", "Bold dual thermal readout"),
        WidgetPresetCatalogItem("wth_5", "Animated Weather", "Weather", "Animated", "4x2", "#18FFFF", "#071924", "WEATHER", "Cyber Rain 22°", "Dynamic particle atmosphere"),
        WidgetPresetCatalogItem("wth_6", "Glass Weather", "Weather", "Glass", "4x2", "#80D8FF", "#142638", "WEATHER", "24°C • UV 3", "Translucent frost weather"),
        WidgetPresetCatalogItem("wth_7", "Minimal Weather", "Weather", "Minimal", "2x2", "#FFFFFF", "#0C0C0C", "WEATHER", "24°", "Pure geometric weather glyph"),
        WidgetPresetCatalogItem("wth_8", "Dark Weather", "Weather", "Dark", "4x2", "#B388FF", "#0A0712", "WEATHER", "Night 21°C", "Stealth barometric monitor"),
        WidgetPresetCatalogItem("wth_9", "Neon Weather", "Weather", "Neon", "2x2", "#00FF66", "#030A06", "WEATHER", "24°C AQI 28", "Neon cyber environment sensor"),
        WidgetPresetCatalogItem("wth_10", "Premium Weather", "Weather", "Premium", "4x2", "#FFD700", "#141006", "WEATHER", "24°C • 1013hPa", "Barometer & wind rose"),

        // F. STATISTICS WIDGETS (10)
        WidgetPresetCatalogItem("sta_1", "Screen Time", "Statistics", "ScreenTime", "4x2", "#00E5FF", "#081520", "STATS", "3h 42m", "Daily focus & screen breakdown"),
        WidgetPresetCatalogItem("sta_2", "App Usage", "Statistics", "AppUsage", "4x2", "#B388FF", "#10081C", "STATS", "Top 4 Apps", "Active package usage bars"),
        WidgetPresetCatalogItem("sta_3", "Daily Activity", "Statistics", "Activity", "2x2", "#00FF66", "#05120A", "STATS", "92% GOAL", "3-ring cyber fitness activity"),
        WidgetPresetCatalogItem("sta_4", "Step Counter", "Statistics", "Steps", "2x2", "#76FF03", "#081204", "STATS", "8,420 STEPS", "Pedometer & kcal tracker"),
        WidgetPresetCatalogItem("sta_5", "Network Speed", "Statistics", "NetSpeed", "4x2", "#00E5FF", "#04121A", "STATS", "↓ 148 Mbps ↑ 42", "5G downlink/uplink gauge"),
        WidgetPresetCatalogItem("sta_6", "Data Usage", "Statistics", "DataUsage", "2x2", "#FF007F", "#160610", "STATS", "14.2 GB", "Monthly 5G data quota"),
        WidgetPresetCatalogItem("sta_7", "Device Information", "Statistics", "DeviceInfo", "4x2", "#00FF66", "#050D09", "STATS", "Vivo T2x 5G", "Dimensity 6020 • Android 15"),
        WidgetPresetCatalogItem("sta_8", "Battery Statistics", "Statistics", "BatStats", "4x2", "#00E676", "#06130C", "STATS", "31.5°C • 4.2V", "Thermal & voltage health"),
        WidgetPresetCatalogItem("sta_9", "Storage Statistics", "Statistics", "Storage", "4x2", "#FFD700", "#141005", "STATS", "64 / 128 GB", "Real internal ROM & RAM bar"),
        WidgetPresetCatalogItem("sta_10", "Custom Statistics", "Statistics", "CustomStats", "4x2", "#18FFFF", "#07161E", "STATS", "CPU 18% • RAM 58%", "Configurable dual-gauge HUD"),

        // G. ADDITIONAL WIDGETS (10)
        WidgetPresetCatalogItem("add_1", "Photo Frame Widget", "Additional", "Photo", "2x2", "#00E5FF", "#0A141E", "NOTES", "CYBER ART", "Neon-framed gallery showcase"),
        WidgetPresetCatalogItem("add_2", "Custom Text Banner", "Additional", "Text", "4x2", "#00FF66", "#040C07", "NOTES", "STAY FROSTY", "Scrolling matrix motto banner"),
        WidgetPresetCatalogItem("add_3", "Cyber Notes Pad", "Additional", "Notes", "4x2", "#FCEE09", "#121004", "NOTES", "• Deploy Theme\n• Check 5G", "Quick pinned checklist card"),
        WidgetPresetCatalogItem("add_4", "Universal Search Bar", "Additional", "Search", "4x2", "#00E5FF", "#0B1622", "NOTES", "Search Cyber OS...", "Instant launcher & web search"),
        WidgetPresetCatalogItem("add_5", "App Shortcut Dock", "Additional", "Shortcuts", "4x2", "#00FF66", "#07100B", "NOTES", "4 Quick Apps", "One-tap studio & app launcher"),
        WidgetPresetCatalogItem("add_6", "System Kernel HUD", "Additional", "SysInfo", "4x2", "#D71921", "#0C0C0C", "STATS", "UPTIME 48:12", "Kernel, SDK 35 & RAM matrix"),
        WidgetPresetCatalogItem("add_7", "Wi-Fi & 5G Radar", "Additional", "Network", "2x2", "#00E676", "#05110A", "STATS", "5G NR -68dBm", "Signal strength & SSID card"),
        WidgetPresetCatalogItem("add_8", "Social Command Hub", "Additional", "Social", "4x2", "#B388FF", "#11081E", "NOTES", "Comms Link", "Quick dial & message matrix"),
        WidgetPresetCatalogItem("add_9", "Custom Folder Widget", "Additional", "Folder", "2x2", "#0A84FF", "#0E1A2E", "NOTES", "Cyber Tools", "Expandable 2x2 home folder"),
        WidgetPresetCatalogItem("add_10", "Music Lyrics Ticker", "Additional", "MusicInfo", "4x2", "#FF007F", "#160712", "MUSIC", "♪ Neon Dreams", "Live BPM & track metadata")
    )

    val controlCenterThemes = listOf(
        "iOS Inspired",
        "Cyber Hacker",
        "RGB Neon",
        "Nothing OS",
        "AMOLED Black",
        "Glassmorphism",
        "Purple Glass",
        "Blue Glass",
        "Minimal Dark",
        "Futuristic Gaming"
    )

    val defaultControlCenterTiles = listOf(
        ControlCenterTileItem("wifi", "Wi-Fi", "CyberNet_5G", true, "WIFI_PANEL"),
        ControlCenterTileItem("mobile_data", "Mobile Data", "Vivo 5G NR", true, "INTERNET_PANEL"),
        ControlCenterTileItem("bluetooth", "Bluetooth", "Connected", true, "BLUETOOTH_SETTINGS"),
        ControlCenterTileItem("flashlight", "Flashlight", "Torch Hardware", false, "TORCH_HARDWARE"),
        ControlCenterTileItem("airplane", "Airplane", "Off", false, "AIRPLANE_SETTINGS"),
        ControlCenterTileItem("location", "Location", "High Accuracy", true, "LOCATION_SETTINGS"),
        ControlCenterTileItem("dark_mode", "AMOLED Mode", "Active", true, "TOGGLE_AMOLED"),
        ControlCenterTileItem("battery_saver", "Battery Saver", "Optimized", false, "BATTERY_SAVER"),
        ControlCenterTileItem("auto_rotate", "Auto Rotate", "Portrait Lock", false, "DISPLAY_SETTINGS"),
        ControlCenterTileItem("hotspot", "5G Hotspot", "Ready", false, "TETHER_SETTINGS"),
        ControlCenterTileItem("dnd", "Do Not Disturb", "Focus Off", false, "SOUND_SETTINGS"),
        ControlCenterTileItem("screen_recorder", "Recorder", "1080p 60fps", false, "RECORDER_TIP"),
        ControlCenterTileItem("screenshot", "Screenshot", "Capture View", false, "CAPTURE_PREVIEW")
    )

    val notificationStyles = listOf(
        "Terminal Code Card",
        "Neon Edge Card",
        "RGB Chroma Banner",
        "OLED Hairline Card",
        "Frosted Stack Card",
        "Dot Header Glyph",
        "Gold Filigree Card",
        "Translucent Ice Card",
        "Synthwave Glow Card",
        "HUD Alert Module"
    )

    val statusBarStyles = listOf(
        "Cyber Telemetry HUD",
        "iOS Dynamic Pill",
        "Nothing Dot Bar",
        "AMOLED Stealth Line",
        "RGB Gamer Header",
        "Glass Capsule Bar",
        "Royal Gold Crest",
        "Minimal Centered Clock",
        "Dual-Line Speed + Battery",
        "Vivo Funtouch Pro Bar"
    )

    val lockScreenDesigns = listOf(
        "Cyber Matrix Terminal Lock",
        "iOS 18 Depth Glass Lock",
        "Nothing Dot Matrix AOD",
        "AMOLED Zero-Power Orbit",
        "RGB Hexagon Reactor Lock",
        "Royal Chronograph Lock",
        "Synthwave Sunset Grid Lock",
        "Vertical Typographic Lock",
        "Weather + Music Smart Lock",
        "Futuristic Biometric HUD"
    )

    val dialerThemes = listOf(
        "Cyber Dark",
        "iOS Inspired",
        "RGB Neon",
        "Glass Dialer",
        "AMOLED",
        "Royal Gold",
        "Nothing Style",
        "Minimal White",
        "Gaming",
        "Gradient Pro"
    )

    val fontStyles = listOf(
        "Cyber",
        "Gaming",
        "Modern",
        "Minimal",
        "Handwriting",
        "Rounded",
        "Bold",
        "Elegant",
        "Monospace",
        "Premium"
    )

    val rgbEdgePresets = listOf(
        "Matrix Acid Green",
        "Chroma Rainbow Wave",
        "Cyberpunk Cyan-Magenta",
        "Nothing Crimson Pulse",
        "Ultraviolet Laser",
        "24K Gold Shimmer",
        "Electric Blue Reactor",
        "Vivo FlashCharge Dual",
        "Toxic Biohazard",
        "Arctic Cryo Glow",
        "Solar Flare Orange",
        "Stealth White Hairline"
    )

    val chargingAnimations = listOf(
        "Cyber Charging",
        "Neon Green",
        "RGB Circle",
        "Electric Blue",
        "AMOLED",
        "Glass Battery",
        "Futuristic",
        "Minimal",
        "Rainbow",
        "Gaming"
    )

    val soundPacks = listOf(
        "Cyber Pulse 2099",
        "Matrix Terminal Beep",
        "Neon Crystal Chime",
        "Plasma Reactor Alert",
        "Quantum Harmonic Ring",
        "Sub-Zero Synth",
        "Glitch Core DSP",
        "Royal Acoustic Gold",
        "Minimal Tactile Click",
        "Arcade 8-Bit Overdrive"
    )

    val animationPresets = listOf(
        "Cyber Snap (Ultra Fast)",
        "Holographic Cube 3D",
        "Liquid Glass Morph",
        "Matrix Cascade",
        "iOS Spring Physics",
        "Nothing Dot Dissolve",
        "Neon Portal Zoom",
        "Zero-Gravity Float",
        "Tactical Visor Wipe",
        "Zen Minimal Fade"
    )
}
