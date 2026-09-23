package com.mountplanner.data.repository

import com.mountplanner.data.local.dao.NoteDao
import com.mountplanner.data.model.Note
import com.mountplanner.data.model.toDomain
import com.mountplanner.data.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getAllNotes(): Flow<List<Note>> =
        noteDao.getAllNotes().map { list -> list.map { it.toDomain() } }

    fun getByExpedition(expeditionId: String): Flow<List<Note>> =
        noteDao.getByExpedition(expeditionId).map { list -> list.map { it.toDomain() } }

    fun getByExpeditionAndDay(expeditionId: String, dayNumber: Int): Flow<List<Note>> =
        noteDao.getByExpeditionAndDay(expeditionId, dayNumber).map { list -> list.map { it.toDomain() } }

    fun getLastNote(): Flow<Note?> =
        noteDao.getAllNotes().map { list -> list.firstOrNull()?.toDomain() }

    suspend fun getById(id: String): Note? =
        noteDao.getById(id)?.toDomain()

    suspend fun save(note: Note) = noteDao.insert(note.toEntity())
    
    suspend fun update(note: Note) = noteDao.update(note.toEntity())
    
    suspend fun delete(note: Note) = noteDao.delete(note.toEntity())
}
