package com.mountplanner.data.remote

import com.mountplanner.data.remote.model.ForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,precipitation_sum,windspeed_10m_max,weathercode,sunrise,sunset,snowfall_sum",
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") days: Int = 7
    ): Response<ForecastResponse>
}
