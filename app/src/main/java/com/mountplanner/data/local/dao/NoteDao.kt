package com.mountplanner.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mountplanner.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE expeditionId = :expeditionId ORDER BY dayNumber ASC, createdAt DESC")
    fun getByExpedition(expeditionId: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE expeditionId = :expeditionId AND dayNumber = :dayNumber ORDER BY createdAt DESC")
    fun getByExpeditionAndDay(expeditionId: String, dayNumber: Int): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getById(id: String): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)
}
