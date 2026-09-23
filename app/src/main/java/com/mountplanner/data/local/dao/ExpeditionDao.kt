package com.mountplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mountplanner.data.local.entity.ExpeditionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpeditionDao {
    @Query("SELECT * FROM expeditions ORDER BY createdAt DESC")
    fun getAllExpeditions(): Flow<List<ExpeditionEntity>>
    
    @Query("SELECT * FROM expeditions WHERE status = :status ORDER BY createdAt DESC")
    fun getByStatus(status: String): Flow<List<ExpeditionEntity>>
    
    @Query("SELECT * FROM expeditions WHERE id = :id")
    fun getById(id: String): Flow<ExpeditionEntity?>
    
    @Query("SELECT * FROM expeditions WHERE status = 'active' LIMIT 1")
    fun getActiveExpedition(): Flow<ExpeditionEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expedition: ExpeditionEntity)
    
    @Update
    suspend fun update(expedition: ExpeditionEntity)
    
    @Delete
    suspend fun delete(expedition: ExpeditionEntity)
    
    @Query("UPDATE expeditions SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: String, status: String, updatedAt: Long = System.currentTimeMillis())
    
    @Query("UPDATE expeditions SET mountReporterTripId = :tripId, mountReporterShareToken = :shareToken, mountReporterEnabled = 1 WHERE id = :id")
    suspend fun setMountReporterData(id: String, tripId: String, shareToken: String)
}
