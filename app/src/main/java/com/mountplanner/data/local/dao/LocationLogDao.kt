package com.mountplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mountplanner.data.local.entity.LocationLogEntity

@Dao
interface LocationLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: LocationLogEntity): Long

    @Query("SELECT * FROM location_logs WHERE expeditionId = :expeditionId ORDER BY capturedAt ASC")
    suspend fun getAllForExpedition(expeditionId: String): List<LocationLogEntity>

    @Query("SELECT * FROM location_logs WHERE sentToBackend = 0 ORDER BY capturedAt ASC")
    suspend fun getPendingSync(): List<LocationLogEntity>

    @Query("UPDATE location_logs SET sentToBackend = 1, backendPingId = :backendPingId WHERE id = :id")
    suspend fun markAsSentToBackend(id: Long, backendPingId: String?)

    @Query("SELECT * FROM location_logs WHERE expeditionId = :expeditionId ORDER BY capturedAt DESC LIMIT 1")
    suspend fun getLastForExpedition(expeditionId: String): LocationLogEntity?
}
