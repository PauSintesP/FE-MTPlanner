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
class ShareLocationUseCase @Inject constructor(
    private val appPreferences: AppPreferences,
    private val expeditionRepository: ExpeditionRepository,
    private val apiService: MountReporterApiService,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(expeditionId: String): String? {
        val expedition = expeditionRepository.getById(expeditionId) ?: return null
        val userName = appPreferences.userName.first() ?: "Unknown User"
        
        return try {
            val response = apiService.createTrip(
                CreateTripRequest(
                    userName = userName,
                    routeName = expedition.name
                )
            )
            
            if (response.isSuccessful) {
                val tripId = response.body()?.tripId
                val shareToken = response.body()?.shareToken ?: return null
                
                appPreferences.saveMountReporterTripId(tripId)
                appPreferences.saveShareToken(shareToken)
                
                expeditionRepository.save(expedition.copy(shareToken = shareToken))
                
                LocationWorker.enqueueLocationWorker(context, intervalMinutes = 15)
                
                shareToken
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
