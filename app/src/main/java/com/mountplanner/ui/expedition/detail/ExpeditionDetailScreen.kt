package com.mountplanner.ui.expedition.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpeditionDetailScreen(
    expeditionId: String,
    viewModel: ExpeditionDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Resumen", "Mapa", "POIs", "Notas", "Checklist", "Tiempo")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Expedición") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A0A0A),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF0A0A0A)
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF0A0A0A),
                contentColor = Color.White
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                when (selectedTab) {
                    0 -> {
                        Column {
                            Text("Resumen", color = Color.White)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = viewModel::startExpedition,
                                modifier = Modifier.fillMaxWidth().height(56.dp)
                            ) {
                                Text("INICIAR EXPEDICIÓN")
                            }
                        }
                    }
                    1 -> Text("Mapa", color = Color.White)
                    2 -> Text("POIs", color = Color.White)
                    3 -> Text("Notas", color = Color.White)
                    4 -> Text("Checklist", color = Color.White)
                    5 -> Text("Tiempo", color = Color.White)
                }
            }
        }
    }
}
