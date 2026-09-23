package com.mountplanner.data.model

import com.mountplanner.data.local.entity.PoiEntity
import java.util.UUID

enum class PoiCategory(val label: String, val emoji: String) {
    PEAK("Peak", "⛰️"),
    WATER_SOURCE("Water Source", "💧"),
    CAMP("Camp", "⛺"),
    HUT("Hut/Refuge", "🛖"),
    PASS("Mountain Pass", "🏔️"),
    VIEWPOINT("Viewpoint", "👁️"),
    DANGER("Danger", "⚠️"),
    PARKING("Parking", "🅿️"),
    TRAILHEAD("Trailhead", "🥾"),
    SHELTER("Shelter", "🏚️"),
    CROSSING("River Crossing", "🌊"),
    CAVE("Cave", "🕳️"),
    SCENIC("Scenic Spot", "📸"),
    FOOD("Food", "🍔"),
    WILDLIFE("Wildlife", "🦌"),
    MEDICAL("Medical", "⚕️"),
    INFO("Information", "ℹ️"),
    CHECKPOINT("Checkpoint", "📍"),
    RESCUE("Rescue Point", "🚁"),
    BIVOUAC("Bivouac", "🏕️"),
    GLACIER("Glacier", "🧊"),
    OTHER("Other", "🔹")
}

data class Poi(
    val id: String = UUID.randomUUID().toString(),
    val expeditionId: String? = null,
    val name: String,
    val description: String? = null,
    val lat: Double,
    val lng: Double,
    val altitudeM: Double? = null,
    val category: PoiCategory = PoiCategory.OTHER,
    val subcategory: String? = null,
    val reliability: String = "uncertain",
    val seasonalAvailability: String? = null,
    val notes: String? = null,
    val photoPaths: List<String> = emptyList(),
    val isPersonal: Boolean = true,
    val externalId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

fun Poi.toEntity(): PoiEntity = PoiEntity(
    id = id,
    expeditionId = expeditionId,
    name = name,
    description = description,
    lat = lat,
    lng = lng,
    altitudeM = altitudeM,
    category = category.name,
    subcategory = subcategory,
    reliability = reliability,
    seasonalAvailability = seasonalAvailability,
    notes = notes,
    photoPaths = if (photoPaths.isNotEmpty()) photoPaths.joinToString(",") else null,
    isPersonal = isPersonal,
    externalId = externalId,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun PoiEntity.toDomain(): Poi = Poi(
    id = id,
    expeditionId = expeditionId,
    name = name,
    description = description,
    lat = lat,
    lng = lng,
    altitudeM = altitudeM,
    category = try { PoiCategory.valueOf(category) } catch (e: Exception) { PoiCategory.OTHER },
    subcategory = subcategory,
    reliability = reliability,
    seasonalAvailability = seasonalAvailability,
    notes = notes,
    photoPaths = photoPaths?.split(",")?.filter { it.isNotBlank() } ?: emptyList(),
    isPersonal = isPersonal,
    externalId = externalId,
    createdAt = createdAt,
    updatedAt = updatedAt
)
