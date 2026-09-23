package com.mountplanner.data.model

import com.mountplanner.data.local.entity.NoteEntity
import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val expeditionId: String? = null,
    val dayNumber: Int? = null,
    val title: String,
    val content: String,
    val lat: Double? = null,
    val lng: Double? = null,
    val altitudeM: Double? = null,
    val mood: String? = null,
    val weather: String? = null,
    val photoPaths: List<String> = emptyList(),
    val audioPath: String? = null,
    val tags: List<String> = emptyList(),
    val linkedPoiId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "local"
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    expeditionId = expeditionId,
    dayNumber = dayNumber,
    title = title,
    content = content,
    lat = lat,
    lng = lng,
    altitudeM = altitudeM,
    mood = mood,
    weather = weather,
    photoPaths = if (photoPaths.isNotEmpty()) photoPaths.joinToString(",") else null,
    audioPath = audioPath,
    tags = if (tags.isNotEmpty()) tags.joinToString(",") else null,
    linkedPoiId = linkedPoiId,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus
)

fun NoteEntity.toDomain(): Note = Note(
    id = id,
    expeditionId = expeditionId,
    dayNumber = dayNumber,
    title = title,
    content = content,
    lat = lat,
    lng = lng,
    altitudeM = altitudeM,
    mood = mood,
    weather = weather,
    photoPaths = photoPaths?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
    audioPath = audioPath,
    tags = tags?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
    linkedPoiId = linkedPoiId,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus
)
