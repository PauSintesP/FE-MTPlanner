package com.mountplanner.ui.map

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mountplanner.core.location.LocationManager
import com.mountplanner.data.model.LocationPoint
import com.mountplanner.data.model.Poi
import com.mountplanner.data.repository.PoiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val poiRepository: PoiRepository,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _expeditionId = MutableStateFlow<String?>(null)
    
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation

    val pois: StateFlow<List<Poi>> = poiRepository.getAllPois()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
    val trackPoints = MutableStateFlow<List<LocationPoint>>(emptyList())

    fun loadForExpedition(id: String) {
        _expeditionId.value = id
    }

    fun centerOnMyLocation() {
        viewModelScope.launch {
            _currentLocation.value = locationManager.getCurrentLocation()
        }
    }
}
