package com.mountplanner.domain.usecase

import com.mountplanner.data.local.dao.LocationLogDao
import com.mountplanner.util.GpxExporter
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExportGpxUseCase @Inject constructor(
    private val locationLogDao: LocationLogDao,
    private val gpxExporter: GpxExporter
) {
    suspend operator fun invoke(expeditionId: String): File? {
        val logs = locationLogDao.getByExpeditionId(expeditionId)
        if (logs.isEmpty()) return null
        
        return gpxExporter.exportToGpx(expeditionId, logs)
    }
}
