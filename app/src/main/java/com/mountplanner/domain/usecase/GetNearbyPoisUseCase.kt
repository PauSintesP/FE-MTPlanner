package com.mountplanner.domain.usecase

import com.mountplanner.domain.model.Poi
import com.mountplanner.domain.repository.PoiRepository
import com.mountplanner.util.HaversineCalculator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNearbyPoisUseCase @Inject constructor(
    private val poiRepository: PoiRepository
) {
    suspend operator fun invoke(
        currentLat: Double, 
        currentLng: Double, 
        limit: Int = 20, 
        category: String? = null
    ): List<Poi> {
        val pois = if (category != null) {
            poiRepository.getByCategory(category)
        } else {
            poiRepository.getAll()
        }
        
        return HaversineCalculator.sortByDistance(currentLat, currentLng, pois)
            .take(limit)
    }
}
