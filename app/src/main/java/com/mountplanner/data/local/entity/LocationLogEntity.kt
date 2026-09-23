package com.mountplanner.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_logs")
data class LocationLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val expeditionId: String,
    val lat: Double,
    val lng: Double,
    val altitudeM: Double? = null,
    val accuracyM: Float? = null,
    val speedMps: Float? = null,
    val bearingDeg: Float? = null,
    val batteryPct: Int? = null,
    val capturedAt: Long,
    val sentToBackend: Boolean = false,
    val backendPingId: String? = null
)
