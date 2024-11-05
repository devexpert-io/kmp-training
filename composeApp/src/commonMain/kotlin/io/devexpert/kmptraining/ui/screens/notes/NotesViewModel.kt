package io.devexpert.kmptraining.ui.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.devexpert.kmptraining.data.NotesRepository
import io.devexpert.kmptraining.data.AuthRepository
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.User
import io.devexpert.kmptraining.ui.domain.Action
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.note_cloned
import kmptraining.composeapp.generated.resources.note_deleted
import kmptraining.composeapp.generated.resources.notes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

class NotesViewModel(
    private val notesRepository: NotesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val message = MutableStateFlow<StringResource?>(null)

    val state = notesRepository.notes
        .map { notes -> UiState(notes = notes) }
        .combine(message) { state, message -> state.copy(message = message) }
        .combine(authRepository.user) { state, user -> state.copy(user = user) }
        .catch { e -> emit(UiState(error = e.message)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState(isLoading = true)
        )

    fun onAction(action: Action, note: Note) {
        viewModelScope.launch {
            when (action) {
                Action.CLONE -> {
                    notesRepository.cloneNote(note)
                    message.value = Res.string.note_cloned
                }
                Action.DELETE -> {
                    notesRepository.deleteNote(note)
                    message.value = Res.string.note_deleted
                }
            }
        }
    }

    fun messageShown() {
        message.value = null
    }

    data class UiState(
        val notes: List<Note> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val message: StringResource? = null,
        val user: User? = null
    )
}
