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
class ShareLocationUseCase @Inject constructor(
    private val appPreferences: AppPreferences,
    private val expeditionRepository: ExpeditionRepository,
    private val apiService: MountReporterApiService,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(expeditionId: String): String? {
        val expedition = expeditionRepository.getById(expeditionId).first() ?: return null
        val userName = appPreferences.userName.first().ifEmpty { "Senderista" }
        
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
                
                appPreferences.setMountReporterTripId(tripId)
                appPreferences.setMountReporterShareToken(shareToken)
                appPreferences.setMountReporterEnabled(true)
                
                expeditionRepository.setMountReporterData(expeditionId, tripId ?: "", shareToken)
                
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
