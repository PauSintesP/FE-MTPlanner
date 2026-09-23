package com.mountplanner.ui.expedition.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpeditionListScreen(
    viewModel: ExpeditionListViewModel = hiltViewModel(),
    onNavigateToCreate: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val expeditions by viewModel.expeditions.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Expediciones") },
                actions = {
                    IconButton(onClick = { /* TODO Expand Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A0A0A),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreate) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Expedición")
            }
        },
        containerColor = Color(0xFF0A0A0A)
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            ScrollableTabRow(
                selectedTabIndex = 0,
                containerColor = Color(0xFF0A0A0A),
                contentColor = Color.White
            ) {
                Tab(selected = true, onClick = { viewModel.setFilter("all") }, text = { Text("Todas") })
                Tab(selected = false, onClick = { viewModel.setFilter("planning") }, text = { Text("Planificando") })
                Tab(selected = false, onClick = { viewModel.setFilter("active") }, text = { Text("Activa") })
                Tab(selected = false, onClick = { viewModel.setFilter("finished") }, text = { Text("Finalizadas") })
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(expeditions) { exp ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onNavigateToDetail(exp.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(exp.name, style = MaterialTheme.typography.titleMedium)
                            Text(exp.status, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
