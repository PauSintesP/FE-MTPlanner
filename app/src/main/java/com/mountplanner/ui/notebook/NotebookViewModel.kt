package com.mountplanner.ui.notebook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mountplanner.domain.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotebookViewModel @Inject constructor() : ViewModel() {

    private val _allNotes = MutableStateFlow<List<Note>>(emptyList())
    val allNotes: StateFlow<List<Note>> = _allNotes
    
    private val _notesByExpedition = MutableStateFlow<Map<String, List<Note>>>(emptyMap())
    val notesByExpedition: StateFlow<Map<String, List<Note>>> = _notesByExpedition
    
    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote

    fun createNote(note: Note) {
        // Implementation
    }
    
    fun updateNote(note: Note) {
        // Implementation
    }
    
    fun deleteNote(note: Note) {
        // Implementation
    }
}
