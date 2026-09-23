package com.mountplanner.ui.expedition.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mountplanner.data.repository.ExpeditionRepository
import com.mountplanner.domain.model.Expedition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ExpeditionListViewModel @Inject constructor(
    private val expeditionRepository: ExpeditionRepository
) : ViewModel() {

    private val _filterStatus = MutableStateFlow("all") // "all" | "planning" | "active" | "finished"
    val filterStatus: StateFlow<String> = _filterStatus

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val expeditions: StateFlow<List<Expedition>> = combine(
        expeditionRepository.getAllExpeditions(),
        _filterStatus,
        _searchQuery
    ) { exps, status, query ->
        exps.filter { 
            (status == "all" || it.status == status) &&
            (query.isEmpty() || it.name.contains(query, ignoreCase = true))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(status: String) {
        _filterStatus.value = status
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteExpedition(expeditionId: String) {
        // expeditionRepository.deleteExpedition(expeditionId)
    }
}
