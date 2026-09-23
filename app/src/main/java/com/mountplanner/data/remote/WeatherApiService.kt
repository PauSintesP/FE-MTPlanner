package com.mountplanner.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Data class placeholder
data class WeatherApiResponse(
    val latitude: Double,
    val longitude: Double,
    val daily: Any? // Can be expanded with exact Open-Meteo response structure
)

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,precipitation_sum,windspeed_10m_max,weathercode,sunrise,sunset,snowfall_sum",
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") days: Int = 7
    ): Response<WeatherApiResponse>
}
