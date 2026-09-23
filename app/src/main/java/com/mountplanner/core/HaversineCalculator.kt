package com.mountplanner.core

import com.mountplanner.data.local.entity.LocationPoint
import com.mountplanner.data.local.entity.Poi
import kotlin.math.*

object HaversineCalculator {
    private const val EARTH_RADIUS_KM = 6371.0

    fun distanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS_KM * c
    }

    fun totalDistanceKm(points: List<LocationPoint>): Double {
        if (points.size < 2) return 0.0
        var total = 0.0
        for (i in 0 until points.size - 1) {
            val p1 = points[i]
            val p2 = points[i + 1]
            total += distanceKm(p1.latitude, p1.longitude, p2.latitude, p2.longitude)
        }
        return total
    }

    fun elevationGainM(points: List<LocationPoint>): Double {
        var gain = 0.0
        for (i in 0 until points.size - 1) {
            val e1 = points[i].elevation ?: continue
            val e2 = points[i + 1].elevation ?: continue
            if (e2 > e1) {
                gain += (e2 - e1)
            }
        }
        return gain
    }

    fun elevationLossM(points: List<LocationPoint>): Double {
        var loss = 0.0
        for (i in 0 until points.size - 1) {
            val e1 = points[i].elevation ?: continue
            val e2 = points[i + 1].elevation ?: continue
            if (e1 > e2) {
                loss += (e1 - e2)
            }
        }
        return loss
    }

    fun distanceToPoi(currentLat: Double, currentLng: Double, poi: Poi): Double {
        return distanceKm(currentLat, currentLng, poi.latitude, poi.longitude)
    }

    fun sortByDistance(currentLat: Double, currentLng: Double, pois: List<Poi>): List<Poi> {
        return pois.sortedBy { distanceToPoi(currentLat, currentLng, it) }
    }
}
