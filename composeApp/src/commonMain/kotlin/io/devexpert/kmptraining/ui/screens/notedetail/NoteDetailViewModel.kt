package io.devexpert.kmptraining.ui.screens.notedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.devexpert.kmptraining.data.NotesRepository
import io.devexpert.kmptraining.domain.Note
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    noteId: Int,
    private val repository: NotesRepository
) : ViewModel() {

    val state = if (noteId == Note.Empty.id) {
        flowOf(UiState(note = Note.Empty))
    } else {
        repository
            .getNote(noteId)
            .map { UiState(note = it) }
            .catch { e -> emit(UiState(error = e.message)) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState(isLoading = true)
    )

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
