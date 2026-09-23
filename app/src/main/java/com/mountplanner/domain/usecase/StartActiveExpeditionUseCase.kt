package com.mountplanner.domain.usecase

import android.content.Context
import com.mountplanner.data.local.preferences.AppPreferences
import com.mountplanner.domain.repository.ExpeditionRepository
import com.mountplanner.data.remote.MountReporterApiService
import com.mountplanner.domain.model.CreateTripRequest
import com.mountplanner.worker.LocationWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StartActiveExpeditionUseCase @Inject constructor(
    private val expeditionRepository: ExpeditionRepository,
    private val appPreferences: AppPreferences,
    private val apiService: MountReporterApiService,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(expeditionId: String) {
        val expedition = expeditionRepository.getById(expeditionId) ?: return
        
        val updated = expedition.copy(
            status = "active",
            actualStartDate = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        var shareToken: String? = null
        var tripId: String? = null
        
        val mountReporterEnabled = appPreferences.mountReporterEnabled.first()
        if (mountReporterEnabled) {
            try {
                val response = apiService.createTrip(
                    CreateTripRequest(
                        userName = appPreferences.userName.first() ?: "Unknown",
                        routeName = updated.name
                    )
                )
                if (response.isSuccessful) {
                    tripId = response.body()?.tripId
                    shareToken = response.body()?.shareToken
                    appPreferences.saveMountReporterTripId(tripId)
                    appPreferences.saveShareToken(shareToken)
                }
            } catch (e: Exception) {
                // Ignore for now
            }
        }
        
        expeditionRepository.save(updated.copy(shareToken = shareToken))
        appPreferences.saveActiveExpeditionId(expeditionId)
        
        LocationWorker.enqueueLocationWorker(context, intervalMinutes = 15)
    }
}
