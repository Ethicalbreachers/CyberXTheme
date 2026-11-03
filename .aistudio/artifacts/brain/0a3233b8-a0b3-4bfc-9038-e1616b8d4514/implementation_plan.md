# CYBER THEME X — Ultimate Android Personalization & Customization Ecosystem

**CYBER THEME X** is an all-in-one Android personalization ecosystem tailored for Android 15 and Vivo T2x 5G (Funtouch OS). It empowers users to transform their phone's appearance across home screens, real Android AppWidgets, wallpapers, custom launcher layouts, icon packs, Control Center panels, dialer themes, notifications, status bar & lock screen previews, RGB edge lighting, charging animations, fonts, and sound packs through a unified One-Click Apply engine.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> Since the initial clarification questions were skipped, we have selected the recommended defaults that maximize immediate usability, Play Policy compliance, and Vivo T2x 5G compatibility without requiring external account sign-in or invasive permissions.

- **Confirmed Decision 1 — Persistence & Theme Store**: **Local Room Database + Jetpack DataStore + JSON File Import/Export**. All user-created themes, custom widgets, icon packs, wallpapers, launcher layouts, and store downloads persist reliably across app restarts via Room and DataStore, with structured JSON theme import/export for sharing and backup.
- **Confirmed Decision 2 — System Integration & Vivo T2x 5G Honesty**: **Real Android Home Launcher + Real `AppWidgetProvider` Home Widgets + Safe System Intents**.
  - **Supported Native Actions**: Real Home/Lock Wallpaper application (`WallpaperManager`), real installable Android Home Screen Widgets (`AppWidgetProvider` + `RemoteViews` + `requestPinAppWidget` + configuration activity), optional Default Home Launcher role (`CATEGORY_HOME`), real Dialer keypad launching `Intent.ACTION_DIAL`, and real system settings panels (`Settings.Panel` / `Settings.ACTION_*`) for Control Center toggles.
  - **Transparent Vivo / Android 15 Boundaries**: Where Funtouch OS or Android 15 restricts third-party apps from replacing the native lock screen security layer, status bar system UI, or system-wide fonts without root, the One-Click Apply Engine and Compatibility Center explicitly categorize those items as *"Applied to Cyber Launcher & In-App Experience / System Preview"* rather than faking system-wide changes.
- **Confirmed Decision 3 — Navigation & Studio Organization**: **5-Tab Bottom Navigation (`Home`, `Themes`, `Widgets`, `Customize`, `Settings`)** where the **Customize** hub acts as a searchable command center hosting all specialized studios (Wallpaper, Icon, Cyber Launcher, Control Center, Notification, Status Bar, Lock Screen, Dialer, Font, RGB & Edge Lighting, Charging Animation, Sound, Animation, Theme Creator, and Backup & Restore).

---

## 1. Overview & Core Concept

- **What It Does**: Provides a complete theming and customization suite where users can either apply 10+ complete pre-engineered themes in one click or craft bespoke setups across 15+ specialized studios, complete with a working custom Android Launcher and real resizable Android Home Screen Widgets.
- **Target Audience / Persona**: Power users, gamers, and customization enthusiasts on Vivo T2x 5G (Funtouch OS / Android 15) and modern Android smartphones who want cohesive cyberpunk, glassmorphism, AMOLED, Nothing OS, or iOS-inspired aesthetics.
- **Key Value**: Bridges the gap between standalone wallpaper/widget apps and custom launchers by synchronizing theme palettes, typography, icons, widgets, and interactive overlays from a single source of truth.

---

## 2. User Experience & Visual Design

### Key User Flows
1. **Home Dashboard & One-Click Transformation**:
   - Launch into the futuristic dashboard displaying the active theme, live interactive phone frame preview, quick-launch tiles for Wallpaper, Icon Studio, Widget Studio, Control Center, and Cyber Launcher, plus horizontal carousels for Recently Used, Favorites, Recommended, and Downloaded themes, alongside a live **Vivo T2x 5G Compatibility Status** card.
   - Tap **Apply Theme** to launch the **One-Click Apply Engine** modal, which inspects device compatibility, checks permissions, lists components that apply directly to the Android OS vs. Cyber Launcher, executes real wallpaper/widget/launcher updates, and displays a detailed verification report.
2. **Theme Store & Theme Creator**:
   - Browse, search, and filter 10+ distinct themes (*Cyber Hacker, Neon Green, RGB Gaming, AMOLED Black, iOS Inspired, Nothing OS, Royal Gold, Glassmorphism, Purple Neon, Futuristic Pro*).
   - Inspect every theme's sub-components (Wallpaper, Icon Pack, Clock/Battery/Calendar/Music Widgets, Dock, Folder, Control Center, Notification Card, Typography), favorite, download, duplicate, customize, share/export as validated JSON, or reset.
3. **Advanced Widget Studio, Widget Creator & Real Home Screen Widgets**:
   - Browse 70+ widget designs across 7 categories (*Clock, Calendar, Battery, Music Player, Weather, Statistics, and Additional Widgets* including Photo, Notes, Search, Shortcuts, System Info, Network, Social, Custom Folder).
   - Open the visual **Widget Creator** to customize widget dimensions, background color/gradient, transparency, corner radius, neon border, shadow, font family/size/alignment, icon, and live data source (real battery level, live time/date, storage/RAM stats, weather, or custom notes).
   - Tap **Add to Home Screen** (`AppWidgetManager.requestPinAppWidget`) or add directly from the Android OS Widget Picker (supporting Small 2x2, Wide 4x2, and Large 4x4 `AppWidgetProvider` receivers with a dedicated Widget Configuration handling flow).
4. **Cyber Launcher (Full Optional Home Screen)**:
   - Accessible both inside the app and as an installable Android Default Home app (`Intent.CATEGORY_HOME`).
   - Features multi-page home screens, live theme wallpaper & custom widgets, customizable grid (4x5, 5x5, 5x6), custom icon packs & shapes, custom folders with selectable layouts/colors, customizable dock, swipe-up App Drawer with real installed package discovery + search + app hiding, swipe-down universal search, double-tap gesture actions, and layout backup/restore.
5. **Specialized Customization Studios (via Customize Hub)**:
   - **Wallpaper Studio**: 10 categories (*Hacker, Cyberpunk, AMOLED, Gaming, Anime, Nature, Space, Abstract, Minimal, 3D*), procedural 4K/HD & animated canvas wallpapers, Android Photo Picker gallery import, real-time crop/blur/brightness/color-filter/gradient editor, Home/Lock screen preview, and real `WallpaperManager` application (Home, Lock, or Both).
   - **Icon Studio**: 10 icon packs, custom icon creator (shape: Rounded, Square, Circular, Squircle, Hex; neon borders; gradients; shadows; size & label toggles; gallery photo import), per-app icon assignment for Cyber Launcher, and pack import/export.
   - **Control Center Studio**: 10 themes (*iOS Inspired, Cyber Hacker, RGB Neon, Nothing OS, AMOLED Black, Glassmorphism, Purple Glass, Blue Glass, Minimal Dark, Futuristic Gaming*), drag/reorder tile editor, customizable sliders (brightness/volume), media & connectivity cards, and functional tiles (real Torch/Flashlight toggle via `CameraManager`, Wi-Fi/Internet panel, Bluetooth, DND, Auto-Rotate, Battery Saver, Hotspot, Location, Dark Mode, Screenshot/Recorder shortcuts via supported Android intents).
   - **Notification, Status Bar & Lock Screen Studios**: 10 visual designs each with color/blur/corner customization, live interactive previews, AOD-inspired lock screens, and explicit opt-in `NotificationListenerService` support that respects user privacy and Android security.
   - **Dialer Studio**: 10 dialer themes (*Cyber Dark, iOS Inspired, RGB Neon, Glass, AMOLED, Royal Gold, Nothing Style, Minimal White, Gaming, Gradient Pro*) with working T9/number pad, interactive incoming/outgoing call surface simulator, favorite contacts, and real call initiation via `Intent.ACTION_DIAL`.
   - **Font, RGB Edge Lighting, Charging Animation, Sound & Animation Studios**: 10+ presets in each studio—custom bundled fonts (`Orbitron`, `Share Tech Mono`, `Rajdhani`, `JetBrains Mono`, `Space Grotesk`), animated Canvas RGB edge borders & real battery-charging broadcast receiver animations, synthesized/procedural cyberpunk audio tone generator (`AudioTrack`) for ringtone/notification/UI sound previews, and launcher transition physics presets with a Reduce Motion toggle.

### Visual Identity & Theme
- **Aesthetic Direction**: High-contrast **Cyber-Glass AMOLED**. Deep obsidian surfaces paired with surgical neon green accents, holographic rainbow gradient highlights, frosted translucent cards, and crisp technical typography.
- **Color Palette & Mood**:
  - `Background Primary`: `#000000` (Pure AMOLED Black) & `#070A0E` (Deep Cyber Obsidian)
  - `Surface Glass`: `#121820` with `80%` alpha and subtle `1.dp` border highlights (`#1E293B` to `#00FF66`)
  - `Primary Accent`: `#00FF66` (Matrix / Cyber Neon Green)
  - `Secondary Accent`: `#00E5FF` (Electric Cyan) & `#B388FF` (Neon Violet)
  - `Rainbow Gradient`: Multi-stop linear/sweep brush (`#00FF66` → `#00E5FF` → `#7C4DFF` → `#FF007F` → `#FFD600`)
  - `Semantic Status`: `#00E676` (Compatible / Applied), `#FFAB00` (Requires User Action / Permission), `#FF5252` (OS Restricted / Unsupported)
- **Typography & Hierarchy**:
  - Locally bundled Google Fonts via `font-util`: **Orbitron** & **Rajdhani** for futuristic display headers and numeric readouts, **Space Grotesk** for clean UI body copy, and **Share Tech Mono** for Vivo T2x 5G telemetry, hex codes, and terminal diagnostics.
- **Component Styling & Layout**:
  - Edge-to-edge layout (`enableEdgeToEdge()`) with proper `WindowInsets.safeDrawing` and `navigationBarsPadding()`.
  - Glassmorphic cards with `20.dp` rounded corners, subtle gradient borders, spring-animated tab transitions, and `48.dp` minimum touch targets with Material 3 ripples.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Real `AppWidgetProvider` + Canvas Bitmap Rendering for Home Screen Widgets**
  - *Chosen Approach*: Combine standard `RemoteViews` layouts with high-DPI offscreen `Bitmap` rendering (`Canvas`) inside `CyberClockWidgetProvider`, `CyberBatteryWidgetProvider`, and `CyberMultiWidgetProvider`.
  - *Why*: Android's `RemoteViews` limits custom fonts, arbitrary neon glow shaders, and glassmorphic shapes. By rendering the user's exact custom widget configuration (neon rings, analog/cyber clocks, battery arcs, custom gradients) onto a crisp bitmap coupled with clickable `PendingIntent` actions, widgets on the Vivo T2x 5G home screen look 100% identical to the in-app studio previews and support resizing across 2x2, 4x2, and 4x4 grids.
  - *Alternatives Considered*: Text-only `RemoteViews` (rejected because all 10+ designs per category would look generic and lose custom fonts/neon shaders).

- **Decision 2: Unified Local Persistence (Room + DataStore) with Validated JSON Schema**
  - *Chosen Approach*: Store structured entities (Themes, Custom Widgets, Custom Icons, Wallpaper History, Launcher Items/Folders, Backups) in **Room Database** and reactive user preferences (active theme ID, AMOLED/Dark/Light mode, animation speed, reduce motion, RGB edge lighting state, launcher grid settings) in **Preferences DataStore**.
  - *Why*: Guarantees instant startup on Vivo T2x 5G, zero network latency, full offline reliability, and safe schema validation when importing/exporting `.cybertheme.json` backup files.

- **Decision 3: Honest Android 15 & Funtouch OS Compatibility Engine**
  - *Chosen Approach*: Inspect `Build.MANUFACTURER`, `Build.MODEL`, `Build.VERSION.SDK_INT`, default launcher role, `WallpaperManager.isSetWallpaperAllowed`, `CameraManager` torch availability, and notification listener status dynamically.
  - *Why*: Respects Google Play Developer Policies and user trust by clearly distinguishing between changes applied to the Android OS (wallpapers, home widgets, torch, system panels, default launcher) and changes scoped to the Cyber Launcher / in-app studios.

---

## 4. Technical Architecture & Data Strategy

### Architecture & Component Diagram

```
┌──────────────────────────────────────────────────────────────────────────────┐
│                        CYBER THEME X — PRESENTATION LAYER                    │
│                                                                              │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌───────────┐ ┌────────┐ │
│  │  Home Tab    │ │  Themes Tab  │ │ Widgets Tab  │ │ Customize │ │Settings│ │
│  │ - Active Card│ │ - 10+ Themes │ │ - 70+ Designs│ │ - 15+     │ │- Theme │ │
│  │ - Quick Hubs │ │ - Store/Filt │ │ - 7 Categor. │ │   Studios │ │  Modes │ │
│  │ - Vivo Status│ │ - One-Click  │ │ - Pin Widget │ │ - Creator │ │- Backup│ │
│  └──────┬───────┘ └──────┬───────┘ └──────┬───────┘ └─────┬─────┘ └───┬────┘ │
│         └────────────────┴────────────────┴───────────────┴───────────┘      │
│                                           │                                  │
│                     ┌─────────────────────▼─────────────────────┐            │
│                     │        CyberThemeViewModel (MVVM)         │            │
│                     │   StateFlow<CyberUiState> + Studio Events │            │
│                     └─────────────────────┬─────────────────────┘            │
└───────────────────────────────────────────┼──────────────────────────────────┘
                                            │
┌───────────────────────────────────────────▼──────────────────────────────────┐
│                        DOMAIN & SYSTEM INTEGRATION ENGINES                   │
│                                                                              │
│  ┌────────────────────┐  ┌────────────────────┐  ┌────────────────────────┐  │
│  │ One-Click Apply    │  │ Wallpaper & Media  │  │ Android System Bridges │  │
│  │ Engine             │  │ Engine             │  │ - AppWidgetManager     │  │
│  │ - Compat Audit     │  │ - Canvas Renderer  │  │ - CameraManager Torch  │  │
│  │ - Step Execution   │  │ - Blur/Filter/Crop │  │ - Telecom ACTION_DIAL  │  │
│  │ - Detailed Report  │  │ - WallpaperManager │  │ - AudioTrack Synth     │  │
│  └─────────┬──────────┘  └─────────┬──────────┘  └───────────┬────────────┘  │
└────────────┼───────────────────────┼─────────────────────────┼───────────────┘
             │                       │                         │
┌────────────▼───────────────────────▼─────────────────────────▼───────────────┐
│                    DATA LAYER & ANDROID OS ENTRY POINTS                      │
│                                                                              │
│  ┌──────────────────────────────┐     ┌───────────────────────────────────┐  │
│  │ Room Database (CyberDatabase)│     │ Native Android OS Components      │  │
│  │ - ThemeEntity (Built-in+User)│     │ - CyberClockWidgetProvider (2x2)  │  │
│  │ - CustomWidgetEntity         │     │ - CyberBatteryWidgetProvider(2x2) │  │
│  │ - CustomIconEntity           │     │ - CyberMultiWidgetProvider (4x2)  │  │
│  │ - WallpaperRecordEntity      │     │ - CyberWidgetConfigActivity       │  │
│  │ - LauncherItemEntity         │     │ - CyberLauncherActivity (HOME)    │  │
│  │ - BackupSnapshotEntity       │     │ - CyberNotificationService        │  │
│  └──────────────────────────────┘     └───────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────────────────────┘
```

### Data Model & State
- **`ThemeEntity` / `CyberThemeModel`**: Stores complete theme metadata (`id`, `name`, `category`, `isBuiltIn`, `isFavorite`, `isDownloaded`, `primaryColorHex`, `secondaryColorHex`, `backgroundColorHex`, `wallpaperStyle`, `iconPackId`, `clockWidgetStyle`, `batteryWidgetStyle`, `calendarWidgetStyle`, `musicWidgetStyle`, `dockStyle`, `folderStyle`, `controlCenterStyle`, `notificationStyle`, `fontFamilyName`). Pre-seeded with 10 distinct themes plus online catalog items.
- **`CustomWidgetEntity`**: Stores user-designed and studio widgets (`id`, `title`, `category`, `designPreset`, `sizeType`, `bgColorHex`, `accentColorHex`, `transparency`, `blurRadius`, `cornerRadius`, `borderWidth`, `showShadow`, `fontFamily`, `fontSizeSp`, `textAlign`, `customText`, `isPinnedToHome`).
- **`CustomIconEntity`**: Stores icon packs and per-app icon overrides (`id`, `packName`, `targetPackage`, `label`, `shape`, `bgColorHex`, `fgColorHex`, `neonBorder`, `hasShadow`, `sizeScale`, `showLabel`).
- **`LauncherState`**: Tracks multi-page home grid items, custom folders (name, color, layout style, apps list), dock shortcuts, hidden package names, grid dimensions, gesture bindings, and page transition preset.
- **`DeviceCompatibilityState`**: Real-time snapshot of device manufacturer/model (highlighting Vivo T2x 5G / Funtouch OS 15 profiles), Android SDK version, default launcher status, wallpaper permission, notification listener access, flashlight hardware availability, and setup instructions.

### Interactive Component & State Mapping
- **One-Click Apply Flow**: Selecting *Apply Theme* triggers `applyTheme(theme)`:
  1. Generates a real-time compatibility checklist across Wallpaper, Launcher, AppWidgets, Icons, Control Center, and System Previews.
  2. Renders the theme's procedural high-resolution wallpaper bitmap and applies it via `WallpaperManager` (Home, Lock, or Both).
  3. Updates the active theme in `DataStore` and `Room`, refreshes all active `AppWidgetProvider` instances via `AppWidgetManager.notifyAppWidgetViewDataChanged` / broadcast, and updates Cyber Launcher's live state.
  4. Displays a structured verification sheet summarizing applied components, optional user-action items (e.g., pinning a widget or setting default home), and OS-restricted previews.
- **Studio Editors**: Every studio (Wallpaper, Icon, Widget Creator, Control Center, Notification, Status Bar, Lock Screen, Dialer, Font, RGB, Charging, Sound, Animation, Theme Creator) features live reactive state controls (sliders, color swatches, shape pickers, drag/reorder controls) that immediately update a live interactive preview canvas and persist to Room/DataStore on save or apply.
