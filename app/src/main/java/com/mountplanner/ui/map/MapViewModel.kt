package com.mountplanner.ui.map

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mountplanner.domain.model.LocationPoint
import com.mountplanner.domain.model.Poi
import com.mountplanner.domain.repository.PoiRepository
import com.mountplanner.util.LocationManager
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

    private val _trackPoints = MutableStateFlow<List<LocationPoint>>(emptyList())
    val trackPoints: StateFlow<List<LocationPoint>> = _trackPoints

    private val _selectedPoi = MutableStateFlow<Poi?>(null)
    val selectedPoi: StateFlow<Poi?> = _selectedPoi
    
    fun loadForExpedition(id: String) {
        _expeditionId.value = id
        // TODO: load specific track points or pois for this expedition
    }
    
    fun centerOnMyLocation() {
        viewModelScope.launch {
            _currentLocation.value = locationManager.getCurrentLocation()
        }
    }
    
    fun addPoiAtCenter() {
        // Implementation for adding poi at center
    }
}
