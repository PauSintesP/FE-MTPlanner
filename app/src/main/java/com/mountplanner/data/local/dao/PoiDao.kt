package com.mountplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mountplanner.data.local.entity.PoiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoiDao {
    @Query("SELECT * FROM pois ORDER BY createdAt DESC")
    fun getAllPois(): Flow<List<PoiEntity>>

    @Query("SELECT * FROM pois WHERE expeditionId = :expeditionId ORDER BY createdAt DESC")
    fun getByExpedition(expeditionId: String): Flow<List<PoiEntity>>

    @Query("SELECT * FROM pois WHERE category = :category ORDER BY createdAt DESC")
    fun getByCategory(category: String): Flow<List<PoiEntity>>

    @Query("SELECT * FROM pois WHERE id = :id")
    fun getById(id: String): Flow<PoiEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(poi: PoiEntity)

    @Update
    suspend fun update(poi: PoiEntity)

    @Delete
    suspend fun delete(poi: PoiEntity)

    @Query("DELETE FROM pois WHERE expeditionId = :expeditionId")
    suspend fun deleteByExpedition(expeditionId: String)
}
