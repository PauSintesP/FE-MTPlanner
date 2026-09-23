package com.mountplanner.data.model

import com.mountplanner.data.local.entity.WeatherCacheEntity

data class WeatherData(
    val date: String,
    val maxTempC: Double,
    val minTempC: Double,
    val precipitationMm: Double,
    val windspeedKmh: Double,
    val weatherCode: Int,
    val sunriseTs: Long,
    val sunsetTs: Long,
    val snowfallCm: Double,
    val description: String
)

fun WeatherCacheEntity.toDomain(description: String): WeatherData = WeatherData(
    date = date,
    maxTempC = maxTempC,
    minTempC = minTempC,
    precipitationMm = precipitationMm,
    windspeedKmh = windspeedKmh,
    weatherCode = weatherCode,
    sunriseTs = sunriseTs,
    sunsetTs = sunsetTs,
    snowfallCm = snowfallCm,
    description = description
)
