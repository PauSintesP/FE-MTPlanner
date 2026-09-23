package com.mountplanner.data.repository

import com.mountplanner.data.local.dao.WeatherCacheDao
import com.mountplanner.data.local.entity.WeatherCacheEntity
import com.mountplanner.data.model.WeatherData
import com.mountplanner.data.model.toDomain
import com.mountplanner.data.remote.WeatherApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

data class WeatherForecastResult(
    val data: List<WeatherData>,
    val isFromCache: Boolean,
    val cachedAt: Long?
)

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherCacheDao: WeatherCacheDao,
    private val weatherApiService: WeatherApiService
) {
    suspend fun getForecast(lat: Double, lng: Double): WeatherForecastResult = withContext(Dispatchers.IO) {
        try {
            val response = weatherApiService.getForecast(lat = lat, lng = lng)
            if (response.isSuccessful && response.body() != null) {
                val daily = response.body()!!.daily
                val forecastList = mutableListOf<WeatherData>()
                val now = System.currentTimeMillis()

                for (i in daily.time.indices) {
                    val entity = WeatherCacheEntity(
                        id = "${lat}_${lng}_${daily.time[i]}",
                        lat = lat,
                        lng = lng,
                        date = daily.time[i],
                        maxTempC = daily.temperature2mMax.getOrElse(i) { 0.0 },
                        minTempC = daily.temperature2mMin.getOrElse(i) { 0.0 },
                        precipitationMm = daily.precipitationSum.getOrElse(i) { 0.0 },
                        windspeedKmh = daily.windspeed10mMax.getOrElse(i) { 0.0 },
                        weatherCode = daily.weathercode.getOrElse(i) { 0 },
                        sunriseTs = 0L,
                        sunsetTs = 0L,
                        snowfallCm = daily.snowfallSum.getOrElse(i) { 0.0 },
                        cachedAt = now
                    )
                    weatherCacheDao.insert(entity)
                    forecastList.add(entity.toDomain(getWeatherDescription(entity.weatherCode)))
                }
                return@withContext WeatherForecastResult(forecastList, isFromCache = false, cachedAt = now)
            }
        } catch (e: Exception) {
            // Error en la red, intentar desde cache
        }

        val cachedList = getCachedForecast(lat, lng)
        if (!cachedList.isNullOrEmpty()) {
            return@withContext WeatherForecastResult(cachedList, isFromCache = true, cachedAt = System.currentTimeMillis())
        }

        throw Exception("No se pudo obtener el pronóstico del tiempo")
    }

    suspend fun getCachedForecast(lat: Double, lng: Double): List<WeatherData>? = withContext(Dispatchers.IO) {
        val result = mutableListOf<WeatherData>()
        for (dayOffset in 0..6) {
            val cacheId = "${lat}_${lng}"
            val cached = weatherCacheDao.getWeather(cacheId)
            if (cached != null) {
                result.add(cached.toDomain(getWeatherDescription(cached.weatherCode)))
            }
        }
        if (result.isNotEmpty()) result else null
    }

    suspend fun clearOldCache() {
        val oneHourMs = 60 * 60 * 1000L
        weatherCacheDao.deleteOlderThan(System.currentTimeMillis() - oneHourMs)
    }

    private fun getWeatherDescription(code: Int): String {
        return when (code) {
            0 -> "Despejado"
            1, 2, 3 -> "Parcialmente nublado"
            45, 48 -> "Niebla"
            51, 53, 55 -> "Llovizna"
            56, 57 -> "Llovizna helada"
            61, 63, 65 -> "Lluvia"
            66, 67 -> "Lluvia helada"
            71, 73, 75 -> "Nieve"
            77 -> "Granizo fino"
            80, 81, 82 -> "Chubascos"
            85, 86 -> "Chubascos de nieve"
            95 -> "Tormenta"
            96, 99 -> "Tormenta con granizo"
            else -> "Tiempo variable"
        }
    }
}
