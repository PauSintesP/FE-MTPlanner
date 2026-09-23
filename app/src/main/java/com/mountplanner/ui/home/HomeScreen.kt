package com.mountplanner.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
    onNavigateToCreateExpedition: () -> Unit,
    onNavigateToActiveExpedition: () -> Unit
) {
    val activeExpedition by viewModel.activeExpedition.collectAsStateWithLifecycle()
    val upcomingExpeditions by viewModel.upcomingExpeditions.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle(initialValue = true)
    val lastNote by viewModel.lastNote.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MountPlanner") },
                navigationIcon = { Icon(Icons.Default.Terrain, contentDescription = "Logo") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A0A0A),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateExpedition,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Expedición")
            }
        },
        containerColor = Color(0xFF0A0A0A)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(if (isOnline) Color(0xFF00E676) else Color.Red)
                )
            }

            item {
                if (activeExpedition != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Expedición Activa: ${activeExpedition?.name}", style = MaterialTheme.typography.titleLarge, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onNavigateToActiveExpedition,
                                modifier = Modifier.fillMaxWidth().height(56.dp)
                            ) {
                                Text("VER EXPEDICIÓN ACTIVA")
                            }
                        }
                    }
                } else if (upcomingExpeditions.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D47A1))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Próxima Expedición: ${upcomingExpeditions.first().name}", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        }
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { /* TODO */ }, modifier = Modifier.weight(1f).height(56.dp)) { Text("Fuentes cerca") }
                    Button(onClick = { /* TODO */ }, modifier = Modifier.weight(1f).height(56.dp)) { Text("Dónde dormir") }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { /* TODO */ }, modifier = Modifier.weight(1f).height(56.dp)) { Text("Cuaderno") }
                    Button(onClick = { /* TODO */ }, modifier = Modifier.weight(1f).height(56.dp)) { Text("Checklist") }
                }
            }

            item {
                lastNote?.let { note ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Última nota", style = MaterialTheme.typography.labelSmall)
                            Text(note.content, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
