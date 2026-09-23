package com.mountplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mountplanner.data.local.entity.ChecklistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {
    @Query("SELECT * FROM checklist_items WHERE expeditionId = :expeditionId ORDER BY sortOrder ASC")
    fun getByExpedition(expeditionId: String): Flow<List<ChecklistItemEntity>>

    @Query("SELECT * FROM checklist_items WHERE templateId = :templateId ORDER BY sortOrder ASC")
    fun getByTemplate(templateId: String): Flow<List<ChecklistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ChecklistItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ChecklistItemEntity>)

    @Update
    suspend fun update(item: ChecklistItemEntity)

    @Query("UPDATE checklist_items SET isChecked = :isChecked WHERE id = :id")
    suspend fun updateChecked(id: String, isChecked: Boolean)

    @Query("UPDATE checklist_items SET isPacked = :isPacked WHERE id = :id")
    suspend fun updatePacked(id: String, isPacked: Boolean)

    @Delete
    suspend fun delete(item: ChecklistItemEntity)

    @Query("DELETE FROM checklist_items WHERE expeditionId = :expeditionId")
    suspend fun deleteByExpedition(expeditionId: String)
}
