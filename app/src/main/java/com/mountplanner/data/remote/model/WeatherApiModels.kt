package com.mountplanner.data.remote.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val daily: DailyData
)

data class DailyData(
    val time: List<String>,
    @SerializedName("temperature_2m_max") val temperature2mMax: List<Double>,
    @SerializedName("temperature_2m_min") val temperature2mMin: List<Double>,
    @SerializedName("precipitation_sum") val precipitationSum: List<Double>,
    @SerializedName("windspeed_10m_max") val windspeed10mMax: List<Double>,
    @SerializedName("weathercode") val weathercode: List<Int>,
    val sunrise: List<String>,
    val sunset: List<String>,
    @SerializedName("snowfall_sum") val snowfallSum: List<Double>
)
