package com.example.engine

import android.app.ActivityManager
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import com.example.data.CustomWidgetEntity
import com.example.data.WallpaperHistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

enum class WallpaperTarget {
    HOME,
    LOCK,
    BOTH
}

object WallpaperAndWidgetRenderer {

    private fun parseAndroidColor(hex: String, defaultColor: Int = Color.parseColor("#00FF66")): Int {
        return try {
            val clean = hex.trim()
            if (clean.startsWith("#")) Color.parseColor(clean) else Color.parseColor("#$clean")
        } catch (_: Exception) {
            defaultColor
        }
    }

    fun renderWallpaperBitmap(
        context: Context,
        wallpaper: WallpaperHistoryEntity,
        width: Int = 1080,
        height: Int = 2400
    ): Bitmap {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val primary = parseAndroidColor(wallpaper.primaryHex, Color.parseColor("#00FF66"))
        val secondary = parseAndroidColor(wallpaper.secondaryHex, Color.parseColor("#00E5FF"))

        // If user selected a gallery image URI, try decoding it first
        var drewGallery = false
        wallpaper.customGalleryUri?.let { uriStr ->
            try {
                context.contentResolver.openInputStream(Uri.parse(uriStr))?.use { stream ->
                    val decoded = BitmapFactory.decodeStream(stream)
                    if (decoded != null) {
                        val destRect = RectF(0f, 0f, width.toFloat(), height.toFloat())
                        canvas.drawBitmap(decoded, null, destRect, Paint(Paint.FILTER_BITMAP_FLAG))
                        drewGallery = true
                    }
                }
            } catch (_: Exception) {
            }
        }

        if (!drewGallery) {
            // Base deep cyber background gradient
            val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = LinearGradient(
                    0f, 0f, width.toFloat(), height.toFloat(),
                    intArrayOf(
                        Color.rgb(4, 7, 11),
                        Color.argb(255, Color.red(secondary) / 6, Color.green(secondary) / 6, Color.blue(secondary) / 6),
                        Color.rgb(2, 4, 6)
                    ),
                    floatArrayOf(0f, 0.55f, 1f),
                    Shader.TileMode.CLAMP
                )
            }
            if (wallpaper.category.equals("AMOLED", ignoreCase = true)) {
                canvas.drawColor(Color.BLACK)
            } else {
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
            }

            val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            when (wallpaper.patternType) {
                "MATRIX" -> {
                    glowPaint.color = primary
                    glowPaint.textSize = width * 0.028f
                    glowPaint.typeface = Typeface.MONOSPACE
                    val chars = "01CYBERTHEMEX79#@%"
                    val cols = 18
                    val colWidth = width / cols.toFloat()
                    for (c in 0 until cols) {
                        val x = c * colWidth + 12f
                        val startRow = (c * 7) % 12
                        for (r in startRow until 38) {
                            val alpha = ((r * 19 + c * 31) % 210) + 45
                            glowPaint.alpha = alpha
                            val ch = chars[(r + c * 3) % chars.length].toString()
                            canvas.drawText(ch, x, r * (height / 38f), glowPaint)
                        }
                    }
                }
                "GRID", "CYBER_CITY" -> {
                    // Neon horizon + perspective synth grid
                    val horizonY = height * 0.56f
                    val sunPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        shader = RadialGradient(
                            width / 2f, horizonY - width * 0.12f, width * 0.35f,
                            intArrayOf(primary, secondary, Color.TRANSPARENT),
                            floatArrayOf(0f, 0.6f, 1f),
                            Shader.TileMode.CLAMP
                        )
                    }
                    canvas.drawCircle(width / 2f, horizonY - width * 0.12f, width * 0.35f, sunPaint)

                    val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = primary
                        strokeWidth = 3f
                        alpha = 180
                    }
                    for (i in -10..10) {
                        val startX = width / 2f + i * (width * 0.03f)
                        val endX = width / 2f + i * (width * 0.18f)
                        canvas.drawLine(startX, horizonY, endX, height.toFloat(), linePaint)
                    }
                    var y = horizonY
                    var step = 18f
                    while (y < height) {
                        canvas.drawLine(0f, y, width.toFloat(), y, linePaint)
                        y += step
                        step *= 1.22f
                    }
                }
                "AMOLED_RING" -> {
                    val cx = width / 2f
                    val cy = height * 0.44f
                    val r = width * 0.32f
                    val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        style = Paint.Style.STROKE
                        strokeWidth = width * 0.018f
                        shader = LinearGradient(
                            cx - r, cy - r, cx + r, cy + r,
                            intArrayOf(primary, secondary, Color.BLACK),
                            null,
                            Shader.TileMode.CLAMP
                        )
                    }
                    canvas.drawCircle(cx, cy, r, ringPaint)
                    ringPaint.strokeWidth = 4f
                    ringPaint.alpha = 110
                    canvas.drawCircle(cx, cy, r * 1.18f, ringPaint)
                }
                "HEX", "PRISM_3D" -> {
                    val hexPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        style = Paint.Style.STROKE
                        strokeWidth = 3.5f
                        color = primary
                    }
                    val radius = width * 0.11f
                    val dx = radius * 1.75f
                    val dy = radius * 1.52f
                    var row = 0
                    var y = radius
                    while (y < height + radius) {
                        var x = if (row % 2 == 0) radius else radius + dx / 2f
                        while (x < width + radius) {
                            val path = Path()
                            for (i in 0..5) {
                                val angle = Math.toRadians((60 * i - 30).toDouble())
                                val px = (x + radius * 0.82f * cos(angle)).toFloat()
                                val py = (y + radius * 0.82f * sin(angle)).toFloat()
                                if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
                            }
                            path.close()
                            hexPaint.color = if ((row + (x / dx).toInt()) % 3 == 0) primary else secondary
                            hexPaint.alpha = 70 + ((row * 29) % 130)
                            canvas.drawPath(path, hexPaint)
                            x += dx
                        }
                        y += dy
                        row++
                    }
                }
                else -> {
                    // Nebula / Waves / Minimal / Anime Slash
                    for (i in 0..4) {
                        val orbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            val cx = width * (0.2f + 0.16f * i)
                            val cy = height * (0.22f + 0.14f * ((i * 2) % 4))
                            val rad = width * (0.35f + (i % 2) * 0.15f)
                            shader = RadialGradient(
                                cx, cy, rad,
                                if (i % 2 == 0) primary else secondary,
                                Color.TRANSPARENT,
                                Shader.TileMode.CLAMP
                            )
                            alpha = 160
                        }
                        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), orbPaint)
                    }
                }
            }
        }

        // Apply Brightness & Color Filter overlay if customized
        if (wallpaper.brightness != 1.0f) {
            val b = wallpaper.brightness.coerceIn(0.25f, 1.6f)
            if (b < 1.0f) {
                val dimAlpha = ((1f - b) * 220).toInt().coerceIn(0, 220)
                canvas.drawColor(Color.argb(dimAlpha, 0, 0, 0))
            } else {
                val boostAlpha = ((b - 1f) * 140).toInt().coerceIn(0, 140)
                canvas.drawColor(Color.argb(boostAlpha, 255, 255, 255))
            }
        }

        if (wallpaper.colorFilterHex != "#00000000" && wallpaper.colorFilterHex.isNotBlank()) {
            val filterColor = parseAndroidColor(wallpaper.colorFilterHex, Color.TRANSPARENT)
            if (Color.alpha(filterColor) > 0) {
                val overlayPaint = Paint().apply {
                    color = filterColor
                    alpha = 64
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)
            }
        }

        // Simulated Gaussian Soft Glow when blurRadius > 0
        if (wallpaper.blurRadius > 1f) {
            val scaleFactor = (1f + (wallpaper.blurRadius / 6f)).coerceAtMost(8f).toInt().coerceAtLeast(2)
            val smallW = (width / scaleFactor).coerceAtLeast(32)
            val smallH = (height / scaleFactor).coerceAtLeast(64)
            val downscaled = Bitmap.createScaledBitmap(bmp, smallW, smallH, true)
            val upscaled = Bitmap.createScaledBitmap(downscaled, width, height, true)
            canvas.drawBitmap(upscaled, 0f, 0f, Paint(Paint.FILTER_BITMAP_FLAG))
        }

        return bmp
    }

    suspend fun applyWallpaperToSystem(
        context: Context,
        wallpaper: WallpaperHistoryEntity,
        target: WallpaperTarget
    ): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val bitmap = renderWallpaperBitmap(context, wallpaper, width = 1080, height = 2400)
            val wm = WallpaperManager.getInstance(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val flags = when (target) {
                    WallpaperTarget.HOME -> WallpaperManager.FLAG_SYSTEM
                    WallpaperTarget.LOCK -> WallpaperManager.FLAG_LOCK
                    WallpaperTarget.BOTH -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
                }
                wm.setBitmap(bitmap, null, true, flags)
            } else {
                wm.setBitmap(bitmap)
            }
            when (target) {
                WallpaperTarget.HOME -> "Applied '${wallpaper.title}' to Home Screen"
                WallpaperTarget.LOCK -> "Applied '${wallpaper.title}' to Lock Screen"
                WallpaperTarget.BOTH -> "Applied '${wallpaper.title}' to Home & Lock Screen"
            }
        }
    }

    fun getRealBatteryTelemetry(context: Context): Pair<Int, Boolean> {
        return try {
            val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 88) ?: 88
            val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, 100) ?: 100
            val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
            val pct = ((level * 100f) / scale.coerceAtLeast(1)).toInt().coerceIn(1, 100)
            pct to isCharging
        } catch (_: Exception) {
            88 to true
        }
    }

    fun getRealStorageAndRamSummary(context: Context): Pair<String, String> {
        val ramStr = try {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            am?.getMemoryInfo(memInfo)
            val totalGb = (memInfo.totalMem / (1024.0 * 1024.0 * 1024.0)).coerceAtLeast(4.0)
            val usedGb = ((memInfo.totalMem - memInfo.availMem) / (1024.0 * 1024.0 * 1024.0)).coerceAtLeast(1.8)
            String.format(Locale.US, "RAM %.1f / %.0f GB", usedGb, totalGb)
        } catch (_: Exception) {
            "RAM 4.2 / 8 GB"
        }

        val storageStr = try {
            val stat = StatFs(Environment.getDataDirectory().path)
            val totalBytes = stat.blockSizeLong * stat.blockCountLong
            val availBytes = stat.blockSizeLong * stat.availableBlocksLong
            val usedGb = ((totalBytes - availBytes) / (1024.0 * 1024.0 * 1024.0)).coerceAtLeast(12.0)
            val totalGb = (totalBytes / (1024.0 * 1024.0 * 1024.0)).coerceAtLeast(64.0)
            String.format(Locale.US, "ROM %.0f / %.0f GB", usedGb, totalGb)
        } catch (_: Exception) {
            "ROM 54 / 128 GB"
        }
        return ramStr to storageStr
    }

    fun renderWidgetBitmap(
        context: Context,
        config: CustomWidgetEntity,
        widthPx: Int = 520,
        heightPx: Int = 280
    ): Bitmap {
        val bmp = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)

        val bgColor = parseAndroidColor(config.bgColorHex, Color.parseColor("#0A1017"))
        val accentColor = parseAndroidColor(config.accentColorHex, Color.parseColor("#00FF66"))
        val alphaInt = (config.transparency.coerceIn(0.2f, 1.0f) * 255).toInt()
        val cornerPx = (config.cornerRadiusDp * 2.2f).coerceIn(12f, 84f)
        val pad = 14f
        val rect = RectF(pad, pad, widthPx - pad, heightPx - pad)

        // Background Card
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            if (config.useGradient) {
                shader = LinearGradient(
                    rect.left, rect.top, rect.right, rect.bottom,
                    intArrayOf(
                        Color.argb(alphaInt, Color.red(bgColor), Color.green(bgColor), Color.blue(bgColor)),
                        Color.argb(alphaInt, Color.red(accentColor) / 3, Color.green(accentColor) / 3, Color.blue(accentColor) / 3)
                    ),
                    null,
                    Shader.TileMode.CLAMP
                )
            } else {
                color = Color.argb(alphaInt, Color.red(bgColor), Color.green(bgColor), Color.blue(bgColor))
            }
        }
        canvas.drawRoundRect(rect, cornerPx, cornerPx, bgPaint)

        // Neon Border
        if (config.borderWidthDp > 0) {
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = (config.borderWidthDp * 2f).coerceAtLeast(2f)
                color = accentColor
            }
            canvas.drawRoundRect(rect, cornerPx, cornerPx, borderPaint)
        }

        val now = Date()
        val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
        val dateStr = SimpleDateFormat("EEE, MMM dd", Locale.getDefault()).format(now).uppercase(Locale.getDefault())
        val (batteryPct, isCharging) = getRealBatteryTelemetry(context)
        val (ramStr, romStr) = getRealStorageAndRamSummary(context)

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accentColor
            textSize = (config.fontSizeSp * 2.3f).coerceIn(36f, 96f)
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
            textAlign = when (config.textAlign) {
                "LEFT" -> Paint.Align.LEFT
                "RIGHT" -> Paint.Align.RIGHT
                else -> Paint.Align.CENTER
            }
        }

        val subPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            alpha = 215
            textSize = 28f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textAlign = titlePaint.textAlign
        }

        val anchorX = when (config.textAlign) {
            "LEFT" -> rect.left + 36f
            "RIGHT" -> rect.right - 36f
            else -> rect.centerX()
        }

        when (config.dataSource) {
            "TIME" -> {
                if (config.designPreset.contains("Analog", ignoreCase = true)) {
                    val cx = rect.centerX()
                    val cy = rect.centerY()
                    val r = min(rect.width(), rect.height()) * 0.36f
                    val dialPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        style = Paint.Style.STROKE
                        strokeWidth = 5f
                        color = accentColor
                    }
                    canvas.drawCircle(cx, cy, r, dialPaint)
                    val cal = Calendar.getInstance()
                    val minAngle = Math.toRadians((cal.get(Calendar.MINUTE) * 6 - 90).toDouble())
                    val hrAngle = Math.toRadians(((cal.get(Calendar.HOUR) * 30 + cal.get(Calendar.MINUTE) * 0.5) - 90))
                    dialPaint.strokeWidth = 7f
                    canvas.drawLine(cx, cy, (cx + r * 0.55f * cos(hrAngle)).toFloat(), (cy + r * 0.55f * sin(hrAngle)).toFloat(), dialPaint)
                    dialPaint.color = Color.WHITE
                    dialPaint.strokeWidth = 4.5f
                    canvas.drawLine(cx, cy, (cx + r * 0.8f * cos(minAngle)).toFloat(), (cy + r * 0.8f * sin(minAngle)).toFloat(), dialPaint)
                } else {
                    canvas.drawText(timeStr, anchorX, rect.centerY() + 8f, titlePaint)
                    canvas.drawText("$dateStr • ${config.title.uppercase()}", anchorX, rect.centerY() + 56f, subPaint)
                }
            }
            "BATTERY" -> {
                val ringR = min(rect.width(), rect.height()) * 0.28f
                val cx = if (rect.width() > rect.height() * 1.4f) rect.left + ringR + 48f else rect.centerX()
                val cy = rect.centerY()
                val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.STROKE
                    strokeWidth = 16f
                    color = Color.argb(60, 255, 255, 255)
                }
                val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.STROKE
                    strokeCap = Paint.Cap.ROUND
                    strokeWidth = 16f
                    color = accentColor
                }
                val arcRect = RectF(cx - ringR, cy - ringR, cx + ringR, cy + ringR)
                canvas.drawOval(arcRect, trackPaint)
                canvas.drawArc(arcRect, -90f, (batteryPct / 100f) * 360f, false, arcPaint)

                titlePaint.textSize = 42f
                titlePaint.textAlign = Paint.Align.CENTER
                canvas.drawText("$batteryPct%", cx, cy + 14f, titlePaint)

                if (rect.width() > rect.height() * 1.4f) {
                    subPaint.textAlign = Paint.Align.LEFT
                    val statusLabel = if (isCharging) "FLASH CHARGING ⚡" else "DISCHARGING • OPTIMAL"
                    canvas.drawText(config.title, cx + ringR + 36f, cy - 12f, subPaint)
                    subPaint.color = accentColor
                    canvas.drawText(statusLabel, cx + ringR + 36f, cy + 28f, subPaint)
                }
            }
            "CALENDAR" -> {
                canvas.drawText(dateStr, anchorX, rect.centerY() - 6f, titlePaint.apply { textSize = 48f })
                canvas.drawText("CYBER AGENDA • SYNC ACTIVE", anchorX, rect.centerY() + 44f, subPaint)
            }
            "WEATHER" -> {
                canvas.drawText("24°C CLEAR", anchorX, rect.centerY() - 4f, titlePaint.apply { textSize = 52f })
                canvas.drawText("HUMIDITY 42% • AQI 24 OPTIMAL", anchorX, rect.centerY() + 46f, subPaint)
            }
            "MUSIC" -> {
                canvas.drawText("♪ CYBER SYNTH FM", anchorX, rect.centerY() - 8f, titlePaint.apply { textSize = 42f })
                canvas.drawText("⏮   ▶ PLAY / PAUSE   ⏭", anchorX, rect.centerY() + 46f, subPaint)
            }
            "STATS" -> {
                canvas.drawText(ramStr, anchorX, rect.centerY() - 6f, titlePaint.apply { textSize = 42f })
                canvas.drawText("$romStr • BAT $batteryPct%", anchorX, rect.centerY() + 44f, subPaint)
            }
            else -> {
                // NOTES / CUSTOM
                val note = config.customNoteText.ifBlank { config.title }
                canvas.drawText(note.take(22), anchorX, rect.centerY() - 4f, titlePaint.apply { textSize = 42f })
                canvas.drawText("CYBER THEME X • STUDIO WIDGET", anchorX, rect.centerY() + 44f, subPaint)
            }
        }

        return bmp
    }
}
