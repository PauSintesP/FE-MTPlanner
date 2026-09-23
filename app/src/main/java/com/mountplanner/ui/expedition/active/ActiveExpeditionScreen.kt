package com.mountplanner.ui.expedition.active

import androidx.compose.foundation.layout.*
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
fun ActiveExpeditionScreen(
    viewModel: ActiveExpeditionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expedición Activa") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A0A0A),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF0A0A0A)
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("POSICIÓN ACTUAL", style = MaterialTheme.typography.titleMedium)
                    Text("Lat: 0.0, Lon: 0.0")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("PROGRESO DEL DÍA", style = MaterialTheme.typography.titleMedium)
                    Text("Km: 12.5")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = viewModel::pauseCamping,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("🏕️ REGISTRAR CAMPAMENTO")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = viewModel::finishExpedition,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("🏁 FINALIZAR EXPEDICIÓN")
            }
        }
    }
}
