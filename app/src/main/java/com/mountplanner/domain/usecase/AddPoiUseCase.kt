package com.mountplanner.domain.usecase

import com.google.android.gms.location.Priority
import com.mountplanner.core.location.LocationManager
import com.mountplanner.core.prefs.AppPreferences
import com.mountplanner.data.model.Poi
import com.mountplanner.data.model.PoiCategory
import com.mountplanner.data.repository.PoiRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddPoiUseCase @Inject constructor(
    private val poiRepository: PoiRepository,
    private val appPreferences: AppPreferences,
    private val locationManager: LocationManager
) {
    suspend operator fun invoke(
        name: String, 
        description: String, 
        category: String, 
        lat: Double?, 
        lng: Double?
    ): String {
        var finalLat = lat
        var finalLng = lng
        
        if (finalLat == null || finalLng == null) {
            val location = locationManager.getCurrentLocation(
                priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                timeoutMs = 5000L
            )
            if (location != null) {
                finalLat = location.latitude
                finalLng = location.longitude
            } else {
                finalLat = 0.0
                finalLng = 0.0
            }
        }
        
        val activeExpeditionId = appPreferences.activeExpeditionId.first()
        val parsedCategory = try {
            PoiCategory.valueOf(category.uppercase())
        } catch (e: Exception) {
            PoiCategory.OTHER
        }
        
        val poi = Poi(
            id = UUID.randomUUID().toString(),
            expeditionId = activeExpeditionId,
            name = name,
            description = description,
            category = parsedCategory,
            lat = finalLat,
            lng = finalLng,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        poiRepository.save(poi)
        return poi.id
    }
}
