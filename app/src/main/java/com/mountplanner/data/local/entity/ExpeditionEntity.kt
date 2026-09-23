package com.mountplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "expeditions")
data class ExpeditionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val mountainRange: String? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val actualStartDate: Long? = null,
    val actualEndDate: Long? = null,
    val status: String = "planning", // planning | active | finished | cancelled
    val totalDistanceKm: Double? = null,
    val elevationGainM: Double? = null,
    val elevationLossM: Double? = null,
    val maxAltitudeM: Double? = null,
    val participants: String? = null, // JSON array ["Yo","Juan"]
    val difficulty: String? = null, // easy | moderate | hard | expert
    val terrainType: String? = null,
    val thumbnailPhotoPath: String? = null,
    val gpxFilePath: String? = null,
    val mountReporterTripId: String? = null,
    val mountReporterShareToken: String? = null,
    val mountReporterEnabled: Boolean = false,
    val pingIntervalMinutes: Long = 30L,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "local" // local | synced | pending_sync
)
