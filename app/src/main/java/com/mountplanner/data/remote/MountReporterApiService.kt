package com.mountplanner.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// ─── Request models ───────────────────────────────────────────────────────────

data class CreateTripRequest(
    @SerializedName("user_name") val userName: String,
    @SerializedName("route_name") val routeName: String
)

data class PingRequest(
    @SerializedName("trip_id") val tripId: String,
    val lat: Double,
    val lng: Double,
    @SerializedName("accuracy_m") val accuracyM: Float?,
    @SerializedName("battery_pct") val batteryPct: Int?,
    @SerializedName("client_ts") val clientTs: String  // ISO 8601
)

data class CampRequest(
    @SerializedName("trip_id") val tripId: String,
    val lat: Double,
    val lng: Double,
    val fatigue: Int,           // 1-5
    val water: Boolean,
    val food: Boolean,
    @SerializedName("shelter_type") val shelterType: String?,  // hammock | tarp | tent | none
    val notes: String?
)

data class ResumeRequest(
    @SerializedName("trip_id") val tripId: String
)

// ─── Response models ──────────────────────────────────────────────────────────

data class TripResponse(
    @SerializedName("trip_id") val tripId: String,
    @SerializedName("share_token") val shareToken: String
)

data class PingSuccessResponse(
    val ok: Boolean,
    @SerializedName("ping_id") val pingId: Long
)

data class OkResponse(val ok: Boolean)

data class LastPingData(
    val lat: Double,
    val lng: Double,
    @SerializedName("battery_pct") val batteryPct: Int?,
    @SerializedName("received_at") val receivedAt: String,
    @SerializedName("minutes_ago") val minutesAgo: Int
)

data class LastCampData(
    val fatigue: Int,
    val water: Boolean,
    val food: Boolean,
    @SerializedName("shelter_type") val shelterType: String?,
    @SerializedName("reported_at") val reportedAt: String
)

data class StatsResponse(
    @SerializedName("trip_id") val tripId: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("route_name") val routeName: String?,
    val status: String,
    @SerializedName("days_on_route") val daysOnRoute: Int,
    @SerializedName("total_distance_km") val totalDistanceKm: Double?,
    @SerializedName("elevation_gain_m") val elevationGainM: Double?,
    @SerializedName("elevation_loss_m") val elevationLossM: Double?,
    @SerializedName("moving_time_hours") val movingTimeHours: Double?,
    @SerializedName("camping_time_hours") val campingTimeHours: Double?,
    @SerializedName("avg_speed_kmh") val avgSpeedKmh: Double?,
    @SerializedName("last_ping") val lastPing: LastPingData?,
    @SerializedName("last_camp_report") val lastCampReport: LastCampData?,
    val alert: Any?
)

data class PingPoint(
    val lat: Double,
    val lng: Double,
    @SerializedName("altitude_m") val altitudeM: Double?,
    @SerializedName("received_at") val receivedAt: String,
    @SerializedName("battery_pct") val batteryPct: Int?
)

data class PingsResponse(
    @SerializedName("trip_id") val tripId: String,
    val pings: List<PingPoint>
)

// ─── Retrofit Interface ───────────────────────────────────────────────────────

interface MountReporterApiService {

    @POST("api/trips")
    suspend fun createTrip(@Body request: CreateTripRequest): Response<TripResponse>

    @POST("api/ping")
    suspend fun sendPing(@Body request: PingRequest): Response<PingSuccessResponse>

    @POST("api/camp")
    suspend fun reportCamp(@Body request: CampRequest): Response<OkResponse>

    @POST("api/resume")
    suspend fun resumeRoute(@Body request: ResumeRequest): Response<OkResponse>

    @GET("api/stats/{tripId}")
    suspend fun getStats(@Path("tripId") tripId: String): Response<StatsResponse>

    @GET("api/pings/{tripId}")
    suspend fun getPings(
        @Path("tripId") tripId: String,
        @Query("since") since: String? = null  // ISO 8601 timestamp
    ): Response<PingsResponse>
}
