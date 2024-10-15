package io.devexpert.kmptraining

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import io.devexpert.kmptraining.data.NotesRepository
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.ui.screens.notes.Notes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val notes by produceState(initialValue = emptyList<Note>()) {
            value = NotesRepository().getNotes()
        }

        Notes(notes)
    }
}