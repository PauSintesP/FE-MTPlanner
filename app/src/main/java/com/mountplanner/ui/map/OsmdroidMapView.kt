package com.mountplanner.ui.map

import android.location.Location
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mountplanner.domain.model.LocationPoint
import com.mountplanner.domain.model.Poi
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun OsmdroidMapView(
    modifier: Modifier = Modifier,
    pois: List<Poi> = emptyList(),
    trackPoints: List<LocationPoint> = emptyList(),
    currentLocation: Location? = null,
    onMapClick: (GeoPoint) -> Unit = {},
    onPoiClick: (Poi) -> Unit = {},
    centerOnLocation: Boolean = false
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(13.0)
        }
    }
    
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }
    
    LaunchedEffect(pois) {
        mapView.overlays.clear()
        pois.forEach { poi ->
            val marker = Marker(mapView)
            marker.position = GeoPoint(poi.lat, poi.lng)
            marker.title = poi.name
            marker.snippet = poi.category
            marker.setOnMarkerClickListener { m, _ -> 
                onPoiClick(poi)
                m.showInfoWindow()
                true
            }
            mapView.overlays.add(marker)
        }
        mapView.invalidate()
    }
    
    LaunchedEffect(trackPoints) {
        if (trackPoints.isNotEmpty()) {
            val polyline = Polyline()
            polyline.setPoints(trackPoints.map { GeoPoint(it.lat, it.lng) })
            polyline.color = 0xFF00E676.toInt()
            mapView.overlays.add(polyline)
            mapView.invalidate()
        }
    }
    
    LaunchedEffect(currentLocation) {
        currentLocation?.let { loc ->
            val myMarker = Marker(mapView)
            myMarker.position = GeoPoint(loc.latitude, loc.longitude)
            myMarker.title = "Tú estás aquí"
            myMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            mapView.overlays.add(myMarker)
            if (centerOnLocation) {
                mapView.controller.animateTo(GeoPoint(loc.latitude, loc.longitude))
            }
            mapView.invalidate()
        }
    }
    
    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}
