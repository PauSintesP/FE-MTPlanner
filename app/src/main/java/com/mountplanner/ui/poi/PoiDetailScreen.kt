package com.mountplanner.ui.poi

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mountplanner.domain.model.Poi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoiDetailScreen(
    poi: Poi,
    onBackClick: () -> Unit,
    onMapClick: (Poi) -> Unit,
    onNavigateClick: (Poi) -> Unit,
    onEditClick: (Poi) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(poi.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(poi) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(text = "📍", style = MaterialTheme.typography.displayMedium)
                Column {
                    Text(text = poi.name, style = MaterialTheme.typography.headlineSmall)
                    Text(text = poi.category, style = MaterialTheme.typography.bodyLarge)
                }
            }
            
            Divider()
            
            Text(text = "Lat: ${poi.lat}, Lng: ${poi.lng}", style = MaterialTheme.typography.bodyMedium)
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Badge(containerColor = if (poi.reliability == "verified") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error) {
                    Text(text = if (poi.reliability == "verified") "Verificado" else "Reportado")
                }
            }
            
            Text(text = "Notas:", style = MaterialTheme.typography.titleMedium)
            Text(text = poi.notes.ifEmpty { "Sin notas" }, style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.weight(1f))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = { onMapClick(poi) }) {
                    Icon(Icons.Default.Map, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver en mapa")
                }
                Button(onClick = { onNavigateClick(poi) }) {
                    Icon(Icons.Default.Navigation, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Navegar")
                }
            }
        }
    }
}
