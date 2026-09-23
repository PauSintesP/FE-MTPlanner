package com.mountplanner.data.remote.model

import com.google.gson.annotations.SerializedName

data class CreateTripRequest(
    val name: String,
    val description: String?,
    val startDate: Long?,
    val endDate: Long?
)

data class TripResponse(
    val id: String,
    val shareToken: String,
    val name: String,
    val status: String
)

data class PingRequest(
    val lat: Double,
    val lng: Double,
    val altitude: Double?,
    val battery: Int?,
    val timestamp: Long
)

data class PingSuccessResponse(
    val pingId: String,
    val status: String
)

data class CampRequest(
    val lat: Double,
    val lng: Double,
    val name: String?,
    val notes: String?
)

data class OkResponse(
    val status: String,
    val message: String?
)

data class StatsResponse(
    val totalDistanceKm: Double,
    val elevationGainM: Double,
    val maxAltitudeM: Double,
    val pingCount: Int
)

data class PingsResponse(
    val pings: List<PingPoint>
)

data class PingPoint(
    val id: String,
    val lat: Double,
    val lng: Double,
    val timestamp: Long
)
