package com.mountplanner.ui.checklist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(
    viewModel: ChecklistViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    val totalWeight by viewModel.totalWeight.collectAsStateWithLifecycle()
    val checkedCount by viewModel.checkedCount.collectAsStateWithLifecycle()
    val totalCount by viewModel.totalCount.collectAsStateWithLifecycle()
    
    var showAddDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Equipaje - Expedición A")
                        Text("Peso: ${totalWeight / 1000f} kg", style = MaterialTheme.typography.labelMedium)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Compartir */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir lista")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir item")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LinearProgressIndicator(
                progress = if (totalCount > 0) checkedCount.toFloat() / totalCount.toFloat() else 0f,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = "$checkedCount / $totalCount ítems confirmados",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.labelSmall
            )
            
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // Example of group
                item {
                    Text("Ropa", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Checkbox(checked = false, onCheckedChange = { })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Chaqueta impermeable")
                        Spacer(modifier = Modifier.weight(1f))
                        Text("350g")
                    }
                }
            }
        }
        
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Añadir Ítem") },
                text = { Text("Formulario para añadir...") },
                confirmButton = { TextButton(onClick = { showAddDialog = false }) { Text("Añadir") } }
            )
        }
    }
}
