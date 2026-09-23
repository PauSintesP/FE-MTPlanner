package com.mountplanner.ui.poi

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mountplanner.data.preferences.AppPreferences
import com.mountplanner.domain.model.Poi
import com.mountplanner.domain.model.PoiCategory
import com.mountplanner.domain.repository.PoiRepository
import com.mountplanner.domain.usecase.AddPoiUseCase
import com.mountplanner.domain.usecase.GetNearbyPoisUseCase
import com.mountplanner.util.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PoiViewModel @Inject constructor(
    private val poiRepository: PoiRepository,
    private val locationManager: LocationManager,
    private val getNearbyPoisUseCase: GetNearbyPoisUseCase,
    private val addPoiUseCase: AddPoiUseCase,
    private val appPreferences: AppPreferences
) : ViewModel() {
    
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation
    
    val allPois: StateFlow<List<Poi>> = poiRepository.getAllPois()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val nearbyWaterSources: StateFlow<List<Poi>> = combine(
        _currentLocation, poiRepository.getByCategory("WATER_SOURCE")
    ) { loc, pois ->
        if (loc == null) pois
        else getNearbyPoisUseCase(loc.latitude, loc.longitude, pois, maxDistance = 5.0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    data class AddPoiFormState(
        val name: String = "",
        val category: PoiCategory = PoiCategory.WATER_SOURCE,
        val description: String = "",
        val notes: String = "",
        val reliability: String = "reported",
        val lat: Double? = null,
        val lng: Double? = null,
        val useCurrentLocation: Boolean = true
    ) {
        fun toPoi(): Poi {
            return Poi(
                id = 0,
                name = name,
                category = category.name,
                description = description,
                notes = notes,
                reliability = reliability,
                lat = lat ?: 0.0,
                lng = lng ?: 0.0,
                timestamp = System.currentTimeMillis()
            )
        }
    }
    
    private val _addPoiForm = MutableStateFlow(AddPoiFormState())
    val addPoiForm: StateFlow<AddPoiFormState> = _addPoiForm
    
    fun updateCurrentLocation() {
        viewModelScope.launch {
            _currentLocation.value = locationManager.getCurrentLocation()
        }
    }
    
    fun savePoi(form: AddPoiFormState) {
        viewModelScope.launch {
            addPoiUseCase(form.toPoi())
        }
    }
    
    fun deletePoi(poi: Poi) {
        viewModelScope.launch { poiRepository.delete(poi) }
    }
}
