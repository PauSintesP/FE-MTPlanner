package com.mountplanner.worker

import android.content.Context
import android.os.BatteryManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.android.gms.location.Priority
import com.mountplanner.core.location.LocationManager
import com.mountplanner.core.prefs.AppPreferences
import com.mountplanner.data.local.dao.LocationLogDao
import com.mountplanner.data.local.entity.LocationLogEntity
import com.mountplanner.data.remote.MountReporterApiService
import com.mountplanner.data.remote.PingRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.util.concurrent.TimeUnit

@HiltWorker
class LocationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val appPreferences: AppPreferences,
    private val locationLogDao: LocationLogDao,
    private val locationManager: LocationManager,
    private val apiService: MountReporterApiService
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "MountPlannerTracking"

        fun enqueueLocationWorker(context: Context, intervalMinutes: Long) {
            val request = PeriodicWorkRequestBuilder<LocationWorker>(
                repeatInterval = intervalMinutes,
                repeatIntervalTimeUnit = TimeUnit.MINUTES
            )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                        .build()
                )
                .addTag("MountPlannerLocation")
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "MountPlannerLocationWork",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        fun cancelLocationWorker(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork("MountPlannerLocationWork")
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                CHANNEL_ID,
                "Seguimiento GPS",
                android.app.NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Grabando ruta de expedición en segundo plano" }
            val manager = context.getSystemService(android.app.NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("📍 MountPlanner")
            .setContentText("Grabando ruta en segundo plano")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .setSilent(true)
            .build()

        return ForegroundInfo(NOTIFICATION_ID, notification)
    }

    override suspend fun doWork(): Result {
        return try {
            setForeground(getForegroundInfo())

            val activeExpeditionId = appPreferences.activeExpeditionId.first()
            val mountReporterEnabled = appPreferences.mountReporterEnabled.first()
            val tripStatus = appPreferences.tripStatus.first()
            val tripId = appPreferences.mountReporterTripId.first()

            // No hay expedición activa
            if (activeExpeditionId.isNullOrEmpty()) return Result.success()

            // Pings pausados porque está acampando
            if (tripStatus == "camping") return Result.success()

            // Capturar ubicación
            val location = locationManager.getCurrentLocation(
                priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                timeoutMs = 10_000L
            ) ?: return Result.success()

            // Nivel de batería
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

            // Guardar en Room (siempre, independientemente de si hay red)
            val logEntity = LocationLogEntity(
                expeditionId = activeExpeditionId,
                lat = location.latitude,
                lng = location.longitude,
                altitudeM = location.altitude,
                accuracyM = location.accuracy,
                speedMps = location.speed,
                bearingDeg = location.bearing,
                batteryPct = batteryLevel,
                capturedAt = System.currentTimeMillis(),
                sentToBackend = false,
                backendPingId = null
            )
            val logId = locationLogDao.insert(logEntity)

            // Enviar a MountReporter si está activo
            if (mountReporterEnabled && !tripId.isNullOrEmpty()) {
                try {
                    val pingRequest = PingRequest(
                        tripId = tripId,
                        lat = location.latitude,
                        lng = location.longitude,
                        accuracyM = location.accuracy,
                        batteryPct = batteryLevel,
                        clientTs = Instant.ofEpochMilli(logEntity.capturedAt).toString()
                    )
                    val response = apiService.sendPing(pingRequest)
                    if (response.isSuccessful) {
                        val backendPingId = response.body()?.pingId?.toString()
                        locationLogDao.markAsSentToBackend(logId, backendPingId)
                    } else {
                        enqueueSyncWorker()
                    }
                } catch (e: Exception) {
                    // Sin red: queda pendiente en Room, SyncWorker enviará cuando vuelva la red
                    enqueueSyncWorker()
                }
            }

            Result.success()
        } catch (e: Exception) {
            // Nunca Result.failure() salvo error completamente irrecuperable
            Result.success()
        }
    }

    private fun enqueueSyncWorker() {
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "MountPlannerSyncWork",
            ExistingWorkPolicy.KEEP,
            syncRequest
        )
    }
}
