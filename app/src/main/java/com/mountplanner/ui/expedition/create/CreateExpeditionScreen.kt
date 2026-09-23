package com.mountplanner.ui.expedition.create

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
fun CreateExpeditionScreen(
    viewModel: CreateExpeditionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Expedición - Paso ${uiState.currentStep + 1} de 5") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A0A0A),
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF0A0A0A)
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            LinearProgressIndicator(
                progress = { (uiState.currentStep + 1) / 5f },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (uiState.currentStep) {
                0 -> {
                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = viewModel::updateName,
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                1 -> { Text("Fechas y participantes", color = Color.White) }
                2 -> { Text("Importar GPX", color = Color.White) }
                3 -> { Text("MountReporter (Seguimiento)", color = Color.White) }
                4 -> { Text("Plantilla de Checklist", color = Color.White) }
            }

            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                if (uiState.currentStep > 0) {
                    Button(onClick = viewModel::previousStep, modifier = Modifier.height(56.dp)) {
                        Text("Anterior")
                    }
                } else {
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                if (uiState.currentStep < 4) {
                    Button(onClick = viewModel::nextStep, modifier = Modifier.height(56.dp)) {
                        Text("Siguiente")
                    }
                } else {
                    Button(onClick = { viewModel.createExpedition(); onNavigateBack() }, modifier = Modifier.height(56.dp)) {
                        Text("Crear")
                    }
                }
            }
        }
    }
}
