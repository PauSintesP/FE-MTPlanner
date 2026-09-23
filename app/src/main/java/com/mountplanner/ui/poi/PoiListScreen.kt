package com.mountplanner.ui.poi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mountplanner.domain.model.Poi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoiListScreen(
    viewModel: PoiViewModel = hiltViewModel(),
    onAddPoiClick: () -> Unit,
    onPoiClick: (Poi) -> Unit
) {
    val allPois by viewModel.allPois.collectAsStateWithLifecycle()
    val currentLocation by viewModel.currentLocation.collectAsStateWithLifecycle()
    
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("TODOS", "💧 AGUA", "🏠 REFUGIO", "⚠️ PELIGRO", "🏔️ CIMAS", "📍 MÁS")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Puntos de interés") },
                actions = {
                    IconButton(onClick = { /* TODO: Filtro */ }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPoiClick) {
                Icon(Icons.Default.Add, contentDescription = "Añadir POI")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 8.dp
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = allPois,
                    key = { it.id }
                ) { poi ->
                    PoiCard(
                        poi = poi,
                        onClick = { onPoiClick(poi) },
                        onDelete = { viewModel.deletePoi(poi) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoiCard(poi: Poi, onClick: () -> Unit, onDelete: () -> Unit) {
    SwipeToDismissBox(
        state = rememberSwipeToDismissBoxState(
            confirmValueChange = { dismissValue ->
                if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                    onDelete()
                    true
                } else false
            }
        ),
        backgroundContent = {
            // Background for swipe
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "📍", style = MaterialTheme.typography.headlineLarge)
                Column {
                    Text(text = poi.name, style = MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "${poi.category.emoji} ${poi.category.label}")
                        if (poi.reliability == "verified") {
                            Text(text = "✅")
                        } else {
                            Text(text = "⚠️")
                        }
                    }
                }
            }
        }
    }
}
