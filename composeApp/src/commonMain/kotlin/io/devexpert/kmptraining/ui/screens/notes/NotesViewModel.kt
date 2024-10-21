package io.devexpert.kmptraining.ui.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.devexpert.kmptraining.data.NotesRepository
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.ui.domain.Action
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {

    private val repository = NotesRepository()

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loadNotes()
        }
    }

    private suspend fun loadNotes() {
            _state.update { it.copy(isLoading = true) }
            _state.value = try {
                UiState(notes = repository.getNotes())
            } catch (e: Exception) {
                UiState(error = e.message)
            }
    }

    fun onAction(action: Action, note: Note) {
        viewModelScope.launch {
            try {
                when (action) {
                    Action.CLONE -> repository.cloneNote(note)
                    Action.DELETE -> repository.deleteNote(note)
                }
                loadNotes()
            } catch (e: Exception) {
                _state.value = UiState(error = e.message)
            }
        }
    }

    data class UiState(
        val notes: List<Note> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )
}
