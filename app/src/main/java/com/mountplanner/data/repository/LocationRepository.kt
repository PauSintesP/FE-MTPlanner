package com.mountplanner.data.repository

import com.mountplanner.data.local.dao.LocationLogDao
import com.mountplanner.data.local.entity.LocationLogEntity
import com.mountplanner.data.model.LocationPoint
import com.mountplanner.data.model.Poi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.*

@Singleton
class LocationRepository @Inject constructor(
    private val locationLogDao: LocationLogDao
) {
    suspend fun saveLocation(log: LocationLogEntity): Long {
        return locationLogDao.insert(log)
    }

    suspend fun getAllForExpedition(expeditionId: String): List<LocationLogEntity> {
        return locationLogDao.getAllForExpedition(expeditionId)
    }

    suspend fun getPendingSync(): List<LocationLogEntity> {
        return locationLogDao.getPendingSync()
    }

    suspend fun markAsSentToBackend(id: Long, backendPingId: String) {
        locationLogDao.markAsSentToBackend(id, backendPingId)
    }

    suspend fun getLastForExpedition(expeditionId: String): LocationLogEntity? {
        return locationLogDao.getLastForExpedition(expeditionId)
    }

    fun getNearbyPois(currentLat: Double, currentLng: Double, pois: List<Poi>, maxDistanceKm: Double = 5.0): List<Pair<Poi, Double>> {
        return pois.mapNotNull { poi ->
            val distance = calculateHaversineDistanceKm(currentLat, currentLng, poi.lat, poi.lng)
            if (distance <= maxDistanceKm) {
                Pair(poi, distance)
            } else {
                null
            }
        }.sortedBy { it.second }
    }

    private fun calculateHaversineDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Radius of earth in KM
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}
