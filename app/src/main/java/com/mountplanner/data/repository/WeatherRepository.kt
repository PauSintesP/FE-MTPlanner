package com.mountplanner.data.repository

import com.mountplanner.data.local.dao.WeatherCacheDao
import com.mountplanner.data.local.entity.WeatherCacheEntity
import com.mountplanner.data.model.WeatherData
import com.mountplanner.data.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherCacheDao: WeatherCacheDao
    // private val weatherApiService: WeatherApiService // To be injected when api is ready
) {
    suspend fun getWeather(lat: Double, lng: Double, date: String): WeatherData? = withContext(Dispatchers.IO) {
        val cacheId = "${lat}_${lng}_$date"
        val cached = weatherCacheDao.getWeather(cacheId)

        val oneHourMs = 60 * 60 * 1000L
        if (cached != null && (System.currentTimeMillis() - cached.cachedAt) < oneHourMs) {
            return@withContext cached.toDomain(getWeatherDescription(cached.weatherCode))
        }

        // TODO: Call API, fetch weather, and save to cache
        // val response = weatherApiService.getForecast(...)
        // val entity = WeatherCacheEntity(...)
        // weatherCacheDao.insert(entity)
        // return entity.toDomain(...)

        return@withContext cached?.toDomain(getWeatherDescription(cached.weatherCode))
    }

    suspend fun clearOldCache() {
        val oneHourMs = 60 * 60 * 1000L
        weatherCacheDao.deleteOlderThan(System.currentTimeMillis() - oneHourMs)
    }

    private fun getWeatherDescription(code: Int): String {
        return when (code) {
            0 -> "Clear sky"
            1, 2, 3 -> "Mainly clear, partly cloudy, and overcast"
            45, 48 -> "Fog and depositing rime fog"
            51, 53, 55 -> "Drizzle: Light, moderate, and dense intensity"
            56, 57 -> "Freezing Drizzle: Light and dense intensity"
            61, 63, 65 -> "Rain: Slight, moderate and heavy intensity"
            66, 67 -> "Freezing Rain: Light and heavy intensity"
            71, 73, 75 -> "Snow fall: Slight, moderate, and heavy intensity"
            77 -> "Snow grains"
            80, 81, 82 -> "Rain showers: Slight, moderate, and violent"
            85, 86 -> "Snow showers slight and heavy"
            95 -> "Thunderstorm: Slight or moderate"
            96, 99 -> "Thunderstorm with slight and heavy hail"
            else -> "Unknown weather code"
        }
    }
}
