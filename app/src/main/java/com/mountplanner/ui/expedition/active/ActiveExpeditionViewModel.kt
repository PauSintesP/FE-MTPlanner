package com.mountplanner.ui.expedition.active

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class ActiveExpeditionViewModel @Inject constructor() : ViewModel() {
    val isOnline = MutableStateFlow(true)
    fun pauseCamping() {}
    fun resumeMarching() {}
    fun finishExpedition() {}
    fun addQuickPoi() {}
}
