package com.mountplanner.domain.usecase

import android.content.Context
import com.mountplanner.data.local.preferences.AppPreferences
import com.mountplanner.domain.repository.ExpeditionRepository
import com.mountplanner.data.local.dao.LocationLogDao
import com.mountplanner.data.remote.MountReporterApiService
import com.mountplanner.domain.model.ResumeTripRequest
import com.mountplanner.worker.LocationWorker
import com.mountplanner.util.HaversineCalculator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinishExpeditionUseCase @Inject constructor(
    private val expeditionRepository: ExpeditionRepository,
    private val locationLogDao: LocationLogDao,
    private val appPreferences: AppPreferences,
    private val apiService: MountReporterApiService,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(expeditionId: String) {
        val expedition = expeditionRepository.getById(expeditionId) ?: return
        
        val logs = locationLogDao.getByExpeditionId(expeditionId)
        var totalDistanceKm = 0.0
        var elevationGainM = 0.0
        var elevationLossM = 0.0
        
        for (i in 1 until logs.size) {
            val prev = logs[i - 1]
            val curr = logs[i]
            
            totalDistanceKm += HaversineCalculator.calculateDistanceKm(
                prev.lat, prev.lng, curr.lat, curr.lng
            )
            
            val diff = curr.altitude - prev.altitude
            if (diff > 0) {
                elevationGainM += diff
            } else {
                elevationLossM += kotlin.math.abs(diff)
            }
        }
        
        val updated = expedition.copy(
            status = "finished",
            actualEndDate = System.currentTimeMillis(),
            totalDistanceKm = totalDistanceKm,
            elevationGainM = elevationGainM,
            elevationLossM = elevationLossM,
            updatedAt = System.currentTimeMillis()
        )
        
        expeditionRepository.save(updated)
        
        LocationWorker.cancelLocationWorker(context)
        appPreferences.clearActiveExpeditionData()
        
        val mountReporterEnabled = appPreferences.mountReporterEnabled.first()
        val tripId = appPreferences.mountReporterTripId.first()
        
        if (mountReporterEnabled && tripId != null) {
            try {
                apiService.resumeTrip(ResumeTripRequest(tripId = tripId))
            } catch (e: Exception) {
                // Ignore
            }
        }
    }
}
