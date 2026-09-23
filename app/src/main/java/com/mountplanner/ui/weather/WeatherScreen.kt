package com.mountplanner.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mountplanner.domain.model.WeatherData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    lat: Double = 42.63,
    lng: Double = 0.05
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tiempo · Monte Perdido") },
                actions = {
                    IconButton(onClick = { viewModel.loadWeather(lat, lng) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = uiState) {
                is WeatherViewModel.WeatherUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
                }
                is WeatherViewModel.WeatherUiState.Success -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (state.isFromCache) {
                            Badge { Text("Datos del caché") }
                        }
                        
                        // Today card
                        val today = state.forecast.firstOrNull()
                        if (today != null) {
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(weatherCodeToEmoji(today.weatherCode), style = MaterialTheme.typography.displayLarge)
                                    Text("Máx: ${today.maxTemp}° Mín: ${today.minTemp}°")
                                    Text("Precip: ${today.precipitation} mm")
                                }
                            }
                        }
                        
                        // Forecast
                        LazyColumn {
                            items(state.forecast.drop(1)) { day ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(day.date)
                                    Text(weatherCodeToEmoji(day.weatherCode))
                                    Text("${day.maxTemp}° / ${day.minTemp}°")
                                }
                            }
                        }
                    }
                }
                is WeatherViewModel.WeatherUiState.Error -> {
                    Text(text = "Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

fun weatherCodeToEmoji(code: Int): String {
    return when (code) {
        0 -> "☀️"
        1, 2, 3 -> "⛅"
        45, 48 -> "🌫️"
        51, 53, 55 -> "🌧️"
        61, 63, 65 -> "🌧️"
        71, 73, 75 -> "🌨️"
        95, 96, 99 -> "⛈️"
        else -> "☁️"
    }
}
