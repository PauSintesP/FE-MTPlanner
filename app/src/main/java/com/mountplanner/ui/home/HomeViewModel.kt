package com.mountplanner.ui.home

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mountplanner.core.location.LocationManager
import com.mountplanner.core.offline.ConnectivityObserver
import com.mountplanner.core.prefs.AppPreferences
import com.mountplanner.data.model.Expedition
import com.mountplanner.data.model.Note
import com.mountplanner.data.repository.ExpeditionRepository
import com.mountplanner.data.repository.NoteRepository
import com.mountplanner.data.repository.PoiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val expeditionRepository: ExpeditionRepository,
    private val poiRepository: PoiRepository,
    private val noteRepository: NoteRepository,
    private val locationManager: LocationManager,
    private val connectivityObserver: ConnectivityObserver,
    private val appPreferences: AppPreferences
) : ViewModel() {
    
    val activeExpedition: StateFlow<Expedition?> = expeditionRepository.getActiveExpedition()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    val upcomingExpeditions: StateFlow<List<Expedition>> = expeditionRepository
        .getUpcomingExpeditions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val isOnline: StateFlow<Boolean> = connectivityObserver.isConnected
    
    val currentLocation: StateFlow<Location?> = flow {
        emit(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    
    val lastNote: StateFlow<Note?> = noteRepository.getLastNote()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
