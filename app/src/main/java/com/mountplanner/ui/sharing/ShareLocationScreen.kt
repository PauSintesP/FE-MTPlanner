package com.mountplanner.ui.sharing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ShareLocationScreen(
    viewModel: ShareLocationViewModel = hiltViewModel()
) {
    Surface(color = Color(0xFF0A0A0A), modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Compartir Ubicación", color = Color.White)
        }
    }
}
