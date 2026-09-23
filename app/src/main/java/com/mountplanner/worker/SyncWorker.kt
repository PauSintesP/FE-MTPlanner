package com.mountplanner.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import com.mountplanner.core.prefs.AppPreferences
import com.mountplanner.data.local.dao.LocationLogDao
import com.mountplanner.data.remote.MountReporterApiService
import com.mountplanner.data.remote.PingRequest
import java.time.Instant

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val locationLogDao: LocationLogDao,
    private val appPreferences: AppPreferences,
    private val apiService: MountReporterApiService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val pendingPings = locationLogDao.getPendingSync()
            val tripId = appPreferences.mountReporterTripId.first()

            if (tripId.isNullOrEmpty() || pendingPings.isEmpty()) {
                return Result.success()
            }

            for (ping in pendingPings) {
                try {
                    val request = PingRequest(
                        tripId = tripId,
                        lat = ping.lat,
                        lng = ping.lng,
                        accuracyM = ping.accuracyM,
                        batteryPct = ping.batteryPct,
                        clientTs = Instant.ofEpochMilli(ping.capturedAt).toString()
                    )
                    val response = apiService.sendPing(request)
                    if (response.isSuccessful) {
                        val backendPingId = response.body()?.pingId?.toString() ?: ""
                        locationLogDao.markAsSentToBackend(ping.id, backendPingId)
                    }
                } catch (e: Exception) {
                    // Si falla este ping, continúa con los demás y se reintentará en el próximo ciclo
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.success()
        }
    }
}
