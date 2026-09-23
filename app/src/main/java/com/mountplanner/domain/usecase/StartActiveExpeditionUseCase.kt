package com.mountplanner.domain.usecase

import android.content.Context
import com.mountplanner.core.prefs.AppPreferences
import com.mountplanner.data.remote.CreateTripRequest
import com.mountplanner.data.remote.MountReporterApiService
import com.mountplanner.data.repository.ExpeditionRepository
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
        val expedition = expeditionRepository.getById(expeditionId).first() ?: return
        
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
                val userName = appPreferences.userName.first().ifEmpty { "Senderista" }
                val response = apiService.createTrip(
                    CreateTripRequest(
                        userName = userName,
                        routeName = updated.name
                    )
                )
                if (response.isSuccessful) {
                    tripId = response.body()?.tripId
                    shareToken = response.body()?.shareToken
                    appPreferences.setMountReporterTripId(tripId)
                    appPreferences.setMountReporterShareToken(shareToken)
                }
            } catch (e: Exception) {
                // Continuar aunque no haya conexión inmediata
            }
        }
        
        val finalExpedition = if (shareToken != null) {
            updated.copy(mountReporterShareToken = shareToken, mountReporterTripId = tripId)
        } else {
            updated
        }
        expeditionRepository.save(finalExpedition)
        appPreferences.setActiveExpeditionId(expeditionId)
        appPreferences.setTripStatus("active")
        
        LocationWorker.enqueueLocationWorker(context, intervalMinutes = 15)
    }
}
