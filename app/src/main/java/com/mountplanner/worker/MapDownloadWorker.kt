package com.mountplanner.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.mountplanner.util.MapTileDownloader

@HiltWorker
class MapDownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val mapTileDownloader: MapTileDownloader
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val minLat = inputData.getDouble("minLat", 0.0)
            val maxLat = inputData.getDouble("maxLat", 0.0)
            val minLng = inputData.getDouble("minLng", 0.0)
            val maxLng = inputData.getDouble("maxLng", 0.0)
            val minZoom = inputData.getInt("minZoom", 10)
            val maxZoom = inputData.getInt("maxZoom", 16)

            mapTileDownloader.downloadTiles(
                minLat, maxLat, minLng, maxLng, minZoom, maxZoom
            ) { progress ->
                setProgress(workDataOf("progress" to progress))
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
