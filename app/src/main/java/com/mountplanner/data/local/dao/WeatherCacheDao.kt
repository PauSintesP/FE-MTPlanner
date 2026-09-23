package com.mountplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mountplanner.data.local.entity.WeatherCacheEntity

@Dao
interface WeatherCacheDao {
    @Query("SELECT * FROM weather_cache WHERE id = :id")
    suspend fun getWeather(id: String): WeatherCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WeatherCacheEntity)

    @Query("DELETE FROM weather_cache WHERE cachedAt < :timestamp")
    suspend fun deleteOlderThan(timestamp: Long)
}
