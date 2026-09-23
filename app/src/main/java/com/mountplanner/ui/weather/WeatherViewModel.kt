package com.mountplanner.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mountplanner.domain.model.WeatherData
import com.mountplanner.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    
    sealed class WeatherUiState {
        object Loading : WeatherUiState()
        data class Success(val forecast: List<WeatherData>, val isFromCache: Boolean, val cachedAt: Long?) : WeatherUiState()
        data class Error(val message: String, val cachedData: List<WeatherData>?) : WeatherUiState()
    }
    
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState
    
    fun loadWeather(lat: Double, lng: Double) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val result = weatherRepository.getForecast(lat, lng)
                _uiState.value = WeatherUiState.Success(
                    forecast = result.data,
                    isFromCache = result.isFromCache,
                    cachedAt = result.cachedAt
                )
            } catch (e: Exception) {
                val cached = weatherRepository.getCachedForecast(lat, lng)
                _uiState.value = WeatherUiState.Error(e.message ?: "Error", cached)
            }
        }
    }
}
