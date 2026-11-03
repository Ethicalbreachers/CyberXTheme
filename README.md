# CYBER THEME X — Ultimate Android Personalization & Customization Ecosystem

**CYBER THEME X** is a production-grade Android personalization ecosystem engineered in Kotlin and Jetpack Compose for **Vivo T2x 5G (Funtouch OS 15)** and **Android 15** smartphones.

## Included Modules & Features

1. **Home Dashboard**: Live interactive theme preview, One-Click Apply button, 5 Studio shortcuts (Wallpaper, Icon Studio, Widget Studio, Control Center, Cyber Launcher), Recently Used / Favorites / Recommended / Downloaded carousels, and Vivo T2x 5G Compatibility status card.
2. **Complete Theme Store**: 10+ complete built-in themes (*Cyber Hacker, Neon Green, RGB Gaming, AMOLED Black, iOS Inspired, Nothing OS, Royal Gold, Glassmorphism, Purple Neon, Futuristic Pro*) plus Online Store downloadable themes. Every theme synchronizes 14 modules.
3. **Wallpaper Studio**: 10 categories (*Hacker, Cyberpunk, AMOLED, Gaming, Anime, Nature, Space, Abstract, Minimal, 3D*), procedural 4K/HD/Live rendering, Android Photo Picker gallery import, Blur/Brightness/Color Filter editor, Home/Lock preview, and real `WallpaperManager` application.
4. **Icon Studio**: 10 icon packs, Custom Icon Creator, 5 shapes (Rounded, Square, Circular, Squircle, Hexagon), neon borders, size scaling, label customization, and per-app launcher icon overrides.
5. **Cyber Launcher**: Optional Android Default Home Launcher (`CATEGORY_HOME`) with multi-page home screens, swipe-up app drawer, app search, app hiding, custom folders, dock, and grid editor.
6. **Advanced Widget Studio & Real Android Home Screen Widgets**: 70 widget designs across 7 categories (*Clock, Calendar, Battery, Music Player, Weather, Statistics, Additional*) backed by real `AppWidgetProvider` receivers (`CyberClockWidgetProvider`, `CyberBatteryWidgetProvider`, `CyberMultiWidgetProvider`), `CyberWidgetConfigActivity`, and a visual no-code **Widget Creator**.
7. **Control Center, Notification, Status Bar, Lock Screen & Dialer Studios**: 10 themes/designs each, hardware Flashlight Torch control (`CameraManager`), system connectivity panels, privacy-safe `NotificationListenerService`, interactive T9 Dialer with `Intent.ACTION_DIAL`, and AOD-inspired lock screen previews.
8. **Font, RGB Edge Lighting, Charging Animation, Sound & Animation Studios**: 10+ presets each, locally bundled Google Fonts (`Orbitron`, `Rajdhani`, `Share Tech Mono`, `Space Grotesk`, `Pacifico`, `Playfair Display`), animated Canvas RGB edge borders, real `BatteryManager` charging animations, and `AudioTrack` PCM synthesized audio previews.
9. **Theme Creator, Local Backup/Restore & Vivo T2x 5G Compatibility Center**: Full Room SQLite + Jetpack DataStore persistence and validated `CYBER_THEME_X_V1` JSON export/import.

## Build & Verification Instructions

- **Compile Debug APK**:
  ```bash
  gradle assembleDebug
  ```
- **Run Local Unit & Robolectric Tests**:
  ```bash
  gradle :app:testDebugUnitTest
  ```
