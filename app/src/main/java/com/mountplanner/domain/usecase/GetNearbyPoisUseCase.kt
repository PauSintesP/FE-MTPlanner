package com.mountplanner.domain.usecase

import com.mountplanner.core.HaversineCalculator
import com.mountplanner.data.model.Poi
import com.mountplanner.data.repository.PoiRepository
import kotlinx.coroutines.flow.first
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
            poiRepository.getByCategory(category).first()
        } else {
            poiRepository.getAllPois().first()
        }
        
        return HaversineCalculator.sortByDistance(currentLat, currentLng, pois)
            .take(limit)
    }
}
