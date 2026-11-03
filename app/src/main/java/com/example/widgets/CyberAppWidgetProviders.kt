package com.example.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.data.CustomWidgetEntity
import com.example.data.CyberDatabase
import com.example.engine.WallpaperAndWidgetRenderer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object WidgetUpdateCoordinator {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun updateSingleWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        defaultCategory: String,
        defaultDataSource: String,
        defaultTitle: String
    ) {
        val pendingResultScope = scope
        pendingResultScope.launch {
            try {
                val dao = CyberDatabase.getInstance(context).cyberDao()
                val boundConfig = dao.getWidgetForAppWidgetId(appWidgetId)
                    ?: dao.getCustomWidgetsOnce().firstOrNull { it.category.equals(defaultCategory, ignoreCase = true) }
                    ?: CustomWidgetEntity(
                        title = defaultTitle,
                        category = defaultCategory,
                        designPreset = defaultTitle,
                        sizeType = if (defaultCategory == "Clock" || defaultCategory == "Battery") "2x2" else "4x2",
                        bgColorHex = "#080E16",
                        accentColorHex = "#00FF66",
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
                        dataSource = defaultDataSource,
                        boundAppWidgetId = appWidgetId
                    )

                val options = runCatching { appWidgetManager.getAppWidgetOptions(appWidgetId) }.getOrNull()
                val minW = options?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 160)?.coerceAtLeast(110) ?: 180
                val minH = options?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 110)?.coerceAtLeast(90) ?: 120
                val widthPx = (minW * 2.6f).toInt().coerceIn(300, 960)
                val heightPx = (minH * 2.6f).toInt().coerceIn(220, 720)

                val bitmap = WallpaperAndWidgetRenderer.renderWidgetBitmap(
                    context = context,
                    config = boundConfig,
                    widthPx = widthPx,
                    heightPx = heightPx
                )

                val views = RemoteViews(context.packageName, R.layout.widget_cyber_canvas)
                views.setImageViewBitmap(R.id.widget_canvas_image, bitmap)

                val launchIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("OPEN_TAB", "WIDGETS")
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    appWidgetId,
                    launchIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_root_container, pendingIntent)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (_: Exception) {
            }
        }
    }
}

class CyberClockWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { id ->
            WidgetUpdateCoordinator.updateSingleWidget(
                context = context,
                appWidgetManager = appWidgetManager,
                appWidgetId = id,
                defaultCategory = "Clock",
                defaultDataSource = "TIME",
                defaultTitle = "Cyber Clock"
            )
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        WidgetUpdateCoordinator.updateSingleWidget(
            context = context,
            appWidgetManager = appWidgetManager,
            appWidgetId = appWidgetId,
            defaultCategory = "Clock",
            defaultDataSource = "TIME",
            defaultTitle = "Cyber Clock"
        )
    }
}

class CyberBatteryWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { id ->
            WidgetUpdateCoordinator.updateSingleWidget(
                context = context,
                appWidgetManager = appWidgetManager,
                appWidgetId = id,
                defaultCategory = "Battery",
                defaultDataSource = "BATTERY",
                defaultTitle = "Neon Battery"
            )
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        WidgetUpdateCoordinator.updateSingleWidget(
            context = context,
            appWidgetManager = appWidgetManager,
            appWidgetId = appWidgetId,
            defaultCategory = "Battery",
            defaultDataSource = "BATTERY",
            defaultTitle = "Neon Battery"
        )
    }
}

class CyberMultiWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach { id ->
            WidgetUpdateCoordinator.updateSingleWidget(
                context = context,
                appWidgetManager = appWidgetManager,
                appWidgetId = id,
                defaultCategory = "Statistics",
                defaultDataSource = "STATS",
                defaultTitle = "Cyber Telemetry HUD"
            )
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        WidgetUpdateCoordinator.updateSingleWidget(
            context = context,
            appWidgetManager = appWidgetManager,
            appWidgetId = appWidgetId,
            defaultCategory = "Statistics",
            defaultDataSource = "STATS",
            defaultTitle = "Cyber Telemetry HUD"
        )
    }
}

/**
 * Privacy-Safe Notification Listener Service:
 * Only tracks active notification count and package category counts when explicitly enabled by the user.
 * Never reads or stores private message text or personal notification content.
 */
class CyberNotificationListenerService : NotificationListenerService() {

    companion object {
        private val _activeCount = MutableStateFlow(0)
        val activeCount: StateFlow<Int> = _activeCount
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        refreshCountSafely()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        refreshCountSafely()
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        refreshCountSafely()
    }

    private fun refreshCountSafely() {
        runCatching {
            _activeCount.value = activeNotifications?.size ?: 0
        }
    }
}
