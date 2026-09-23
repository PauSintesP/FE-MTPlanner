package com.mountplanner.ui.expedition.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class CreateExpeditionUiState(
    val currentStep: Int = 0,
    val name: String = "",
    val description: String = "",
    val mountainRange: String = "",
    val difficulty: String = "moderate",
    val terrainType: String = "senderismo",
    val participants: List<String> = listOf(),
    val startDate: Long? = null,
    val endDate: Long? = null,
    val gpxFilePath: String? = null,
    val enableMountReporter: Boolean = false,
    val pingIntervalMinutes: Long = 30L,
    val selectedTemplate: String = "alta_montana_verano",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CreateExpeditionViewModel @Inject constructor(
    // private val createExpeditionUseCase: CreateExpeditionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateExpeditionUiState())
    val uiState: StateFlow<CreateExpeditionUiState> = _uiState.asStateFlow()

    fun nextStep() {
        _uiState.update { if (it.currentStep < 4) it.copy(currentStep = it.currentStep + 1) else it }
    }

    fun previousStep() {
        _uiState.update { if (it.currentStep > 0) it.copy(currentStep = it.currentStep - 1) else it }
    }

    fun updateName(name: String) = _uiState.update { it.copy(name = name) }
    
    fun createExpedition() {
        // createExpeditionUseCase.invoke(uiState.value)
    }
}
