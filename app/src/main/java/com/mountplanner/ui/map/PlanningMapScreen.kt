package com.mountplanner.ui.map

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PlanningMapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val pois by viewModel.pois.collectAsStateWithLifecycle()
    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var tempGeoPoint by remember { mutableStateOf<org.osmdroid.util.GeoPoint?>(null) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        OsmdroidMapView(
            modifier = Modifier.fillMaxSize(),
            pois = pois,
            currentLocation = currentLocation,
            onMapClick = { geoPoint ->
                tempGeoPoint = geoPoint
                showAddDialog = true
            },
            centerOnLocation = false
        )
        
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FloatingActionButton(onClick = { viewModel.centerOnMyLocation() }) {
                Icon(Icons.Default.MyLocation, contentDescription = "Mi posición")
            }
            FloatingActionButton(onClick = { /* Add POI here */ }) {
                Icon(Icons.Default.AddLocation, contentDescription = "Añadir POI aquí")
            }
        }
        
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("¿Añadir POI aquí?") },
                text = { Text("Coordenadas: ${tempGeoPoint?.latitude}, ${tempGeoPoint?.longitude}") },
                confirmButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("SÍ")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("NO")
                    }
                }
            )
        }
    }
}
