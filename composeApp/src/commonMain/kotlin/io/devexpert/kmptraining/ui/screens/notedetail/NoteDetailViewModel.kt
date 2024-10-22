package io.devexpert.kmptraining.ui.screens.notedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.devexpert.kmptraining.data.NotesRepository
import io.devexpert.kmptraining.domain.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    noteId: Int,
    private val repository: NotesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init {
        if (noteId == Note.Empty.id) {
            _state.value = UiState(note = Note.Empty)
        } else {
            loadNote(noteId)
        }
    }

    private fun loadNote(noteId: Int) {
        viewModelScope.launch {
            val note = repository.getNote(noteId)
            _state.value = UiState(note = note)
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            repository.saveNote(note)
        }
    }

    data class UiState(
        val note: Note? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}
