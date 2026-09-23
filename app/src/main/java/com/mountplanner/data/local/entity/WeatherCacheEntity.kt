package com.mountplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherCacheEntity(
    @PrimaryKey val id: String, // e.g., "lat_lng_date"
    val lat: Double,
    val lng: Double,
    val date: String,
    val maxTempC: Double,
    val minTempC: Double,
    val precipitationMm: Double,
    val windspeedKmh: Double,
    val weatherCode: Int,
    val sunriseTs: Long,
    val sunsetTs: Long,
    val snowfallCm: Double,
    val cachedAt: Long = System.currentTimeMillis()
)
