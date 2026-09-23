package com.mountplanner.data.model

data class LocationPoint(
    val lat: Double,
    val lng: Double,
    val altitudeM: Double?,
    val accuracyM: Float?,
    val speedMps: Float?,
    val bearingDeg: Float?,
    val capturedAt: Long
)
