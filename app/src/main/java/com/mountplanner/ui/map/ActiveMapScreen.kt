package com.mountplanner.ui.map

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ActiveMapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val pois by viewModel.pois.collectAsStateWithLifecycle()
    val trackPoints by viewModel.trackPoints.collectAsStateWithLifecycle()
    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()
    
    Box(modifier = Modifier.fillMaxSize()) {
        OsmdroidMapView(
            modifier = Modifier.fillMaxSize(),
            pois = pois,
            trackPoints = trackPoints,
            currentLocation = currentLocation,
            centerOnLocation = true
        )
        
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(onClick = { viewModel.centerOnMyLocation() }) {
                Icon(Icons.Default.MyLocation, contentDescription = "Centrar en mí")
            }
            FloatingActionButton(onClick = { /* Ver ruta completa */ }) {
                Icon(Icons.Default.Fullscreen, contentDescription = "Ver ruta completa")
            }
        }
        
        // Overlay inferior
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Posición actual:", style = MaterialTheme.typography.labelMedium)
                if (currentLocation != null) {
                    Text("Lat: ${currentLocation!!.latitude}", style = MaterialTheme.typography.bodyMedium)
                    Text("Lng: ${currentLocation!!.longitude}", style = MaterialTheme.typography.bodyMedium)
                    Text("Alt: ${currentLocation!!.altitude} m", style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text("Buscando GPS...")
                }
            }
        }
    }
}
