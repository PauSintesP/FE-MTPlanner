package com.mountplanner.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor() {

    companion object {
        const val CHANNEL_LOCATION_TRACKING = "channel_location_tracking"
        const val CHANNEL_SYNC = "channel_sync"
        const val CHANNEL_ALERTS = "channel_alerts"
        const val NOTIFICATION_ID_TRACKING = 1001
        const val NOTIFICATION_ID_SYNC = 1002
    }

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val trackingChannel = NotificationChannel(
                CHANNEL_LOCATION_TRACKING,
                "Seguimiento GPS",
                NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Notificaciones de seguimiento GPS silencioso" }

            val syncChannel = NotificationChannel(
                CHANNEL_SYNC,
                "Sincronización",
                NotificationManager.IMPORTANCE_MIN
            ).apply { description = "Notificaciones de sincronización en segundo plano" }

            val alertsChannel = NotificationChannel(
                CHANNEL_ALERTS,
                "Alertas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Alertas importantes durante expedición" }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(listOf(trackingChannel, syncChannel, alertsChannel))
        }
    }

    fun getTrackingForegroundInfo(context: Context): ForegroundInfo {
        val notification = NotificationCompat.Builder(context, CHANNEL_LOCATION_TRACKING)
            .setContentTitle("Seguimiento GPS activo")
            .setContentText("MountPlanner está registrando tu ubicación")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                NOTIFICATION_ID_TRACKING, 
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            ForegroundInfo(NOTIFICATION_ID_TRACKING, notification)
        }
    }

    fun showSyncNotification(context: Context, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, CHANNEL_SYNC)
            .setContentTitle("Sincronizando...")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()
        notificationManager.notify(NOTIFICATION_ID_SYNC, notification)
    }
}
