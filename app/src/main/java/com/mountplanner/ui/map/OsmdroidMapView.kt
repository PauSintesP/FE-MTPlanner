package com.mountplanner.ui.map

import android.content.Context
import android.graphics.Color as AndroidColor
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mountplanner.domain.model.LocationPoint
import com.mountplanner.domain.model.Poi
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.TilesOverlay

// Fuente de mapa Topográfico de Montaña (OpenTopoMap) — Estilo Mapy.cz / Outdoor
// Incluye: Curvas de nivel, sombreado de relieve, cotas de cimas, fuentes y senderos
val OpenTopoMapTileSource = XYTileSource(
    "OpenTopoMap",
    0, 17, 256, ".png",
    arrayOf(
        "https://a.tile.opentopomap.org/",
        "https://b.tile.opentopomap.org/",
        "https://c.tile.opentopomap.org/"
    ),
    "© OpenTopoMap (CC-BY-SA)"
)

// Capa superpuesta de rutas oficiales senderistas (GR, PR, senderos marcados)
val HikingRoutesTileSource = XYTileSource(
    "WaymarkedHiking",
    0, 18, 256, ".png",
    arrayOf("https://tile.waymarkedtrails.org/hiking/"),
    "© Waymarked Trails"
)

enum class MapStyle(val title: String) {
    TOPOGRAPHIC("⛰️ Topográfico Montaña (Mapy)"),
    STANDARD("🗺️ OpenStreetMap Estándar")
}

@Composable
fun OsmdroidMapView(
    modifier: Modifier = Modifier,
    pois: List<Poi> = emptyList(),
    trackPoints: List<LocationPoint> = emptyList(),
    currentLocation: Location? = null,
    onMapClick: (GeoPoint) -> Unit = {},
    onPoiClick: (Poi) -> Unit = {},
    centerOnLocation: Boolean = false,
    showLayerSwitcher: Boolean = true
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var currentStyle by remember { mutableStateOf(MapStyle.TOPOGRAPHIC) }
    var showHikingOverlay by remember { mutableStateOf(true) }
    var showMenu by remember { mutableStateOf(false) }

    val mapView = remember {
        MapView(context).apply {
            // Por defecto mapa topográfico de montaña estilo Mapy
            setTileSource(OpenTopoMapTileSource)
            setMultiTouchControls(true)
            controller.setZoom(14.0)
            isTilesScaledToDpi = true
        }
    }

    // Capa de rutas senderistas marcadas (GR, PR)
    val hikingOverlay = remember {
        val provider = MapTileProviderBasic(context, HikingRoutesTileSource)
        TilesOverlay(provider, context).apply {
            loadingBackgroundColor = AndroidColor.TRANSPARENT
        }
    }

    // Cambiar fuente del mapa según estilo seleccionado
    LaunchedEffect(currentStyle) {
        when (currentStyle) {
            MapStyle.TOPOGRAPHIC -> mapView.setTileSource(OpenTopoMapTileSource)
            MapStyle.STANDARD -> mapView.setTileSource(TileSourceFactory.MAPNIK)
        }
        mapView.invalidate()
    }

    // Activar / desactivar capa de senderos marcados
    LaunchedEffect(showHikingOverlay) {
        if (showHikingOverlay) {
            if (!mapView.overlays.contains(hikingOverlay)) {
                mapView.overlays.add(0, hikingOverlay)
            }
        } else {
            mapView.overlays.remove(hikingOverlay)
        }
        mapView.invalidate()
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

    // Actualizar marcadores de POIs
    LaunchedEffect(pois) {
        // Eliminar markers antiguos preservando la capa base
        val toRemove = mapView.overlays.filterIsInstance<Marker>()
        mapView.overlays.removeAll(toRemove)

        pois.forEach { poi ->
            val marker = Marker(mapView).apply {
                position = GeoPoint(poi.lat, poi.lng)
                title = poi.name
                snippet = "${poi.category} · Alt: ${poi.altitudeM?.toInt() ?: "--"}m"
                setOnMarkerClickListener { m, _ ->
                    onPoiClick(poi)
                    m.showInfoWindow()
                    true
                }
            }
            mapView.overlays.add(marker)
        }
        mapView.invalidate()
    }

    // Track GPS real o ruta planificada
    LaunchedEffect(trackPoints) {
        val oldLines = mapView.overlays.filterIsInstance<Polyline>()
        mapView.overlays.removeAll(oldLines)

        if (trackPoints.isNotEmpty()) {
            val polyline = Polyline().apply {
                setPoints(trackPoints.map { GeoPoint(it.lat, it.lng) })
                outlinePaint.color = AndroidColor.parseColor("#00E676") // Verde brillante
                outlinePaint.strokeWidth = 10f
            }
            mapView.overlays.add(polyline)
            mapView.invalidate()
        }
    }

    // Posición actual del montañero
    LaunchedEffect(currentLocation) {
        currentLocation?.let { loc ->
            val myMarker = Marker(mapView).apply {
                position = GeoPoint(loc.latitude, loc.longitude)
                title = "📍 Mi Posición"
                snippet = "Alt: ${loc.altitude.toInt()}m · Precisión: ±${loc.accuracy.toInt()}m"
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            }
            mapView.overlays.add(myMarker)
            if (centerOnLocation) {
                mapView.controller.animateTo(GeoPoint(loc.latitude, loc.longitude))
            }
            mapView.invalidate()
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        // Selector de capas flotante (estilo Mapy / Outdoor)
        if (showLayerSwitcher) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                FloatingActionButton(
                    onClick = { showMenu = !showMenu },
                    containerColor = Color(0xFF1E1E1E),
                    contentColor = Color.White,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Layers, contentDescription = "Capas de mapa")
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color(0xFF242424))
                ) {
                    Text(
                        text = "Tipo de mapa",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    DropdownMenuItem(
                        text = { Text("⛰️ Topográfico Montaña (Mapy)", color = if (currentStyle == MapStyle.TOPOGRAPHIC) Color(0xFF00E676) else Color.White) },
                        onClick = {
                            currentStyle = MapStyle.TOPOGRAPHIC
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("🗺️ OpenStreetMap Estándar", color = if (currentStyle == MapStyle.STANDARD) Color(0xFF00E676) else Color.White) },
                        onClick = {
                            currentStyle = MapStyle.STANDARD
                            showMenu = false
                        }
                    )
                    HorizontalDivider(color = Color.DarkGray)
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🥾 Red Senderista (GR/PR)", color = Color.White)
                                Switch(
                                    checked = showHikingOverlay,
                                    onCheckedChange = { showHikingOverlay = it }
                                )
                            }
                        },
                        onClick = { showHikingOverlay = !showHikingOverlay }
                    )
                }
            }
        }
    }
}
