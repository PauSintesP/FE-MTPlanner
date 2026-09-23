package com.mountplanner.data.model

import com.mountplanner.data.local.entity.ExpeditionEntity
import java.util.UUID

data class Expedition(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String? = null,
    val mountainRange: String? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val actualStartDate: Long? = null,
    val actualEndDate: Long? = null,
    val status: String = "planning",
    val totalDistanceKm: Double? = null,
    val elevationGainM: Double? = null,
    val elevationLossM: Double? = null,
    val maxAltitudeM: Double? = null,
    val participants: List<String> = emptyList(),
    val difficulty: String? = null,
    val terrainType: String? = null,
    val thumbnailPhotoPath: String? = null,
    val gpxFilePath: String? = null,
    val mountReporterTripId: String? = null,
    val mountReporterShareToken: String? = null,
    val mountReporterEnabled: Boolean = false,
    val pingIntervalMinutes: Long = 30L,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "local"
)

fun Expedition.toEntity(): ExpeditionEntity {
    return ExpeditionEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        mountainRange = this.mountainRange,
        startDate = this.startDate,
        endDate = this.endDate,
        actualStartDate = this.actualStartDate,
        actualEndDate = this.actualEndDate,
        status = this.status,
        totalDistanceKm = this.totalDistanceKm,
        elevationGainM = this.elevationGainM,
        elevationLossM = this.elevationLossM,
        maxAltitudeM = this.maxAltitudeM,
        participants = if (this.participants.isNotEmpty()) this.participants.joinToString(",") else null,
        difficulty = this.difficulty,
        terrainType = this.terrainType,
        thumbnailPhotoPath = this.thumbnailPhotoPath,
        gpxFilePath = this.gpxFilePath,
        mountReporterTripId = this.mountReporterTripId,
        mountReporterShareToken = this.mountReporterShareToken,
        mountReporterEnabled = this.mountReporterEnabled,
        pingIntervalMinutes = this.pingIntervalMinutes,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        syncStatus = this.syncStatus
    )
}

fun ExpeditionEntity.toDomain(): Expedition {
    return Expedition(
        id = this.id,
        name = this.name,
        description = this.description,
        mountainRange = this.mountainRange,
        startDate = this.startDate,
        endDate = this.endDate,
        actualStartDate = this.actualStartDate,
        actualEndDate = this.actualEndDate,
        status = this.status,
        totalDistanceKm = this.totalDistanceKm,
        elevationGainM = this.elevationGainM,
        elevationLossM = this.elevationLossM,
        maxAltitudeM = this.maxAltitudeM,
        participants = this.participants?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
        difficulty = this.difficulty,
        terrainType = this.terrainType,
        thumbnailPhotoPath = this.thumbnailPhotoPath,
        gpxFilePath = this.gpxFilePath,
        mountReporterTripId = this.mountReporterTripId,
        mountReporterShareToken = this.mountReporterShareToken,
        mountReporterEnabled = this.mountReporterEnabled,
        pingIntervalMinutes = this.pingIntervalMinutes,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        syncStatus = this.syncStatus
    )
}
