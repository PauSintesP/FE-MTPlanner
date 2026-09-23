package com.mountplanner.ui.notebook

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    viewModel: NotebookViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    
    val moods = listOf("😊", "🙂", "😐", "😓", "😵")
    val weathers = listOf("☀️", "🌤️", "⛅", "🌧️", "⛈️", "🌨️")
    val tags = listOf("Agua", "Refugio", "Peligro", "Cumbre", "Vista", "Flora", "Fauna", "Técnico")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva nota") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Contenido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp),
                maxLines = 10
            )
            
            Text("Estado de ánimo:")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                moods.forEach { emoji ->
                    TextButton(onClick = { /* Select */ }) { Text(emoji) }
                }
            }
            
            Text("Clima:")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                weathers.forEach { emoji ->
                    TextButton(onClick = { /* Select */ }) { Text(emoji) }
                }
            }
            
            Text("Etiquetas rápidas:")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.take(4).forEach { tag ->
                    FilterChip(selected = false, onClick = { /* Select */ }, label = { Text(tag) })
                }
            }
            
            Button(onClick = { /* Abrir cámara */ }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Tomar foto")
            }
            
            Text("Coordenadas actuales: Lat: 42.123, Lng: -0.456", style = MaterialTheme.typography.labelSmall)
            
            Button(onClick = { /* viewModel.createNote(...) */ }, modifier = Modifier.fillMaxWidth()) {
                Text("GUARDAR NOTA")
            }
        }
    }
}
