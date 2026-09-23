package com.mountplanner.ui.poi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPoiScreen(
    viewModel: PoiViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val formState by viewModel.addPoiForm.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir POI") },
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
                value = formState.name,
                onValueChange = { /* viewModel.updateName(it) */ },
                label = { Text("Nombre (obligatorio)") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = formState.description,
                onValueChange = { /* viewModel.updateDescription(it) */ },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            OutlinedTextField(
                value = formState.notes,
                onValueChange = { /* viewModel.updateNotes(it) */ },
                label = { Text("Notas personales") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Switch(
                    checked = formState.useCurrentLocation,
                    onCheckedChange = { /* viewModel.updateUseLocation(it) */ }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Usar posición actual")
            }
            
            Button(
                onClick = {
                    viewModel.savePoi(formState)
                    onSaveSuccess()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GUARDAR POI")
            }
        }
    }
}
