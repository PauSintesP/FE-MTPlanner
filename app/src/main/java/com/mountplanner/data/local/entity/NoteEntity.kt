package com.mountplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val expeditionId: String? = null,
    val dayNumber: Int? = null,
    val title: String,
    val content: String,
    val lat: Double? = null,
    val lng: Double? = null,
    val altitudeM: Double? = null,
    val mood: String? = null, // great | good | tired | difficult | scared
    val weather: String? = null, // sunny | cloudy | rainy | stormy | snow
    val photoPaths: String? = null, // JSON array
    val audioPath: String? = null,
    val tags: String? = null, // JSON array
    val linkedPoiId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "local"
)
