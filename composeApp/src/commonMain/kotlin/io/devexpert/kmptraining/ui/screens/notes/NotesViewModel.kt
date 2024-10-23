package io.devexpert.kmptraining.ui.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.devexpert.kmptraining.data.NotesRepository
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.ui.domain.Action
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    val state = repository.notes
        .map { notes -> UiState(notes = notes) }
        .catch { e -> emit(UiState(error = e.message)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState(isLoading = true)
        )

    fun onAction(action: Action, note: Note) {
        viewModelScope.launch {
            when (action) {
                Action.CLONE -> repository.cloneNote(note)
                Action.DELETE -> repository.deleteNote(note)
            }
        }
    }

    data class UiState(
        val notes: List<Note> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )
}
