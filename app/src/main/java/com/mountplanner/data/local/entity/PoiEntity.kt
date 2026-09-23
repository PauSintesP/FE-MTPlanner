package com.mountplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "pois")
data class PoiEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val expeditionId: String? = null,
    val name: String,
    val description: String? = null,
    val lat: Double,
    val lng: Double,
    val altitudeM: Double? = null,
    val category: String,
    val subcategory: String? = null,
    val reliability: String = "uncertain", // verified | reported | uncertain
    val seasonalAvailability: String? = null,
    val notes: String? = null,
    val photoPaths: String? = null, // JSON array of strings
    val isPersonal: Boolean = true,
    val externalId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
