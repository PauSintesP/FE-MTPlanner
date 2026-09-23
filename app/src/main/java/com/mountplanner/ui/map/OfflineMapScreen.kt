package com.mountplanner.ui.map

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineMapScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedZoom by remember { mutableStateOf("general") }
    
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mapas Offline") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Descarga mapas para usarlos sin conexión en tus expediciones.")
            
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar zona (ej: Ordesa)") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") }
            )
            
            Text("Nivel de detalle:")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = selectedZoom == "general",
                    onClick = { selectedZoom = "general" },
                    label = { Text("Zona general (z8-10)") }
                )
                FilterChip(
                    selected = selectedZoom == "senderos",
                    onClick = { selectedZoom = "senderos" },
                    label = { Text("Senderos (z11-13)") }
                )
                FilterChip(
                    selected = selectedZoom == "detalle",
                    onClick = { selectedZoom = "detalle" },
                    label = { Text("Detalle (z14-16)") }
                )
            }
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Estimación de descarga:", style = MaterialTheme.typography.titleMedium)
                    Text("~2500 tiles (aprox. 50 MB)")
                }
            }
            
            Button(
                onClick = { /* Iniciar MapDownloadWorker */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("DESCARGAR PARA USO OFFLINE")
            }
            
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text("Mapas descargados", style = MaterialTheme.typography.titleMedium)
            
            // Lista mock
            ListItem(
                headlineContent = { Text("Ordesa y Monte Perdido") },
                supportingContent = { Text("52 MB - Descargado 12/04") },
                trailingContent = { 
                    TextButton(onClick = { /* Borrar */ }) { Text("Borrar") }
                }
            )
        }
    }
}
