package com.mountplanner.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import com.mountplanner.data.local.preferences.AppPreferences
import com.mountplanner.data.local.dao.LocationLogDao
import com.mountplanner.data.remote.MountReporterApiService
import com.mountplanner.domain.model.PingRequest

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val locationLogDao: LocationLogDao,
    private val appPreferences: AppPreferences,
    private val apiService: MountReporterApiService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val pendingPings = locationLogDao.getPendingPings()
            val tripId = appPreferences.mountReporterTripId.first()
            
            if (tripId == null || pendingPings.isEmpty()) {
                return Result.success()
            }

            for (ping in pendingPings) {
                try {
                    val request = PingRequest(
                        tripId = tripId,
                        lat = ping.lat,
                        lng = ping.lng,
                        alt = ping.altitude,
                        battery = ping.batteryLevel
                    )
                    val response = apiService.sendPing(request)
                    if (response.isSuccessful) {
                        val backendPingId = response.body()?.id ?: ""
                        locationLogDao.markAsSentToBackend(ping.id, backendPingId)
                    }
                } catch (e: Exception) {
                    // Si falla, lo deja como pendiente para el próximo intento
                }
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.success()
        }
    }
}
