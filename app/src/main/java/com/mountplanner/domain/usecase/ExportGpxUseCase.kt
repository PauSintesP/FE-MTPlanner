package com.mountplanner.domain.usecase

import com.mountplanner.core.export.GpxExporter
import com.mountplanner.data.local.dao.LocationLogDao
import com.mountplanner.data.model.LocationPoint
import com.mountplanner.data.repository.ExpeditionRepository
import kotlinx.coroutines.flow.first
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExportGpxUseCase @Inject constructor(
    private val locationLogDao: LocationLogDao,
    private val expeditionRepository: ExpeditionRepository,
    private val gpxExporter: GpxExporter
) {
    suspend operator fun invoke(expeditionId: String): File? {
        val logs = locationLogDao.getAllForExpedition(expeditionId)
        if (logs.isEmpty()) return null
        
        val expedition = expeditionRepository.getById(expeditionId).first()
        val expeditionName = expedition?.name ?: "Expedition_$expeditionId"
        
        val points = logs.map { log ->
            LocationPoint(
                lat = log.lat,
                lng = log.lng,
                altitudeM = log.altitudeM,
                accuracyM = log.accuracyM,
                speedMps = log.speedMps,
                bearingDeg = log.bearingDeg,
                capturedAt = log.capturedAt
            )
        }
        
        return gpxExporter.exportToGpx(expeditionId, expeditionName, points)
    }
}
