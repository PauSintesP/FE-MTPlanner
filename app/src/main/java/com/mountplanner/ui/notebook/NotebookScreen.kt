package com.mountplanner.ui.notebook

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mountplanner.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotebookScreen(
    viewModel: NotebookViewModel = hiltViewModel(),
    onAddNoteClick: () -> Unit,
    onNoteClick: (Note) -> Unit
) {
    val allNotes by viewModel.allNotes.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cuaderno de Campo") },
                actions = {
                    IconButton(onClick = { /* Filtro */ }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNoteClick) {
                Icon(Icons.Default.Add, contentDescription = "Nota rápida")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Mock grouped items
            item {
                Text("Expedición Monte Perdido - Día 1", style = MaterialTheme.typography.titleMedium)
            }
            items(allNotes) { note ->
                NoteCard(note = note, onClick = { onNoteClick(note) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(note: Note, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(note.moodEmoji)
                Text(note.weatherEmoji)
                Text(note.title, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(note.content, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(note.timestamp.toString(), style = MaterialTheme.typography.labelSmall)
        }
    }
}
