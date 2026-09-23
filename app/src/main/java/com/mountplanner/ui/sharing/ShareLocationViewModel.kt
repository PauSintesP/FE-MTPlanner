package com.mountplanner.ui.sharing

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareLocationViewModel @Inject constructor() : ViewModel() {
    fun activate(expeditionId: String) {}
    fun deactivate() {}
}
