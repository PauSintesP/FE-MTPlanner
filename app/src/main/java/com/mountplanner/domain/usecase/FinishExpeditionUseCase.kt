package com.mountplanner.domain.usecase

import android.content.Context
import com.mountplanner.core.HaversineCalculator
import com.mountplanner.core.prefs.AppPreferences
import com.mountplanner.data.local.dao.LocationLogDao
import com.mountplanner.data.remote.MountReporterApiService
import com.mountplanner.data.remote.ResumeRequest
import com.mountplanner.data.repository.ExpeditionRepository
import com.mountplanner.worker.LocationWorker
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
        val expedition = expeditionRepository.getById(expeditionId).first() ?: return
        
        val logs = locationLogDao.getAllForExpedition(expeditionId)
        var totalDistanceKm = 0.0
        var elevationGainM = 0.0
        var elevationLossM = 0.0
        
        for (i in 1 until logs.size) {
            val prev = logs[i - 1]
            val curr = logs[i]
            
            totalDistanceKm += HaversineCalculator.distanceKm(
                prev.lat, prev.lng, curr.lat, curr.lng
            )
            
            val prevAlt = prev.altitudeM ?: 0.0
            val currAlt = curr.altitudeM ?: 0.0
            val diff = currAlt - prevAlt
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
        appPreferences.setActiveExpeditionId(null)
        
        val mountReporterEnabled = appPreferences.mountReporterEnabled.first()
        val tripId = appPreferences.mountReporterTripId.first()
        
        if (mountReporterEnabled && !tripId.isNullOrEmpty()) {
            try {
                apiService.resumeRoute(ResumeRequest(tripId = tripId))
            } catch (e: Exception) {
                // Ignore
            }
        }
    }
}
