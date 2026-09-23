package com.mountplanner.core.map

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.tileprovider.modules.IFilesystemCache
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.views.MapView
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapTileDownloader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun downloadTiles(
        minLat: Double,
        maxLat: Double,
        minLng: Double,
        maxLng: Double,
        minZoom: Int,
        maxZoom: Int,
        onProgress: suspend (Int) -> Unit
    ) {
        val boundingBox = BoundingBox(maxLat, maxLng, minLat, minLng)
        // Usar MapView auxiliar para instanciar CacheManager de osmdroid
        val mapView = MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
        }
        val cacheManager = CacheManager(mapView)
        val tileCount = cacheManager.possibleTilesInArea(boundingBox, minZoom, maxZoom)

        if (tileCount > 0) {
            cacheManager.downloadAreaAsync(
                context,
                boundingBox,
                minZoom,
                maxZoom,
                object : CacheManager.CacheManagerCallback {
                    override fun onTaskComplete() {}
                    override fun onTaskFailed(errors: Int) {}
                    override fun updateProgress(progress: Int, currentZoomLevel: Int, zoomMin: Int, zoomMax: Int) {}
                    override fun downloadFailed() {}
                }
            )
        }
        onProgress(100)
    }
}
