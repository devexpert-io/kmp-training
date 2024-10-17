package io.devexpert.kmptraining.ui.screens.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.devexpert.kmptraining.data.NotesRepository
import io.devexpert.kmptraining.domain.Note
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {
    private val repository = NotesRepository()

    var state by mutableStateOf(UiState())
        private set

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val notes = repository.getNotes()
                state = state.copy(notes = notes, isLoading = false)
            } catch (e: Exception) {
                state = state.copy(error = e.message, isLoading = false)
            }
        }
    }

    data class UiState(
        val notes: List<Note> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )
}
