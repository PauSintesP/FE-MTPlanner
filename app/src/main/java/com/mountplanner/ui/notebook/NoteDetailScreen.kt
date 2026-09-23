package com.mountplanner.ui.notebook

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mountplanner.domain.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    note: Note,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onMapClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de la nota") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(note.moodEmoji, style = MaterialTheme.typography.headlineMedium)
                Text(note.weatherEmoji, style = MaterialTheme.typography.headlineMedium)
                Text(note.title, style = MaterialTheme.typography.headlineSmall)
            }
            
            Text(note.content, style = MaterialTheme.typography.bodyLarge)
            
            Divider()
            
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Coordenadas: Lat ${note.lat}, Lng ${note.lng}")
                IconButton(onClick = onMapClick) {
                    Icon(Icons.Default.Map, contentDescription = "Ver en mapa")
                }
            }
        }
    }
}
