package io.devexpert.kmptraining.ui.screens.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.ui.domain.Action

@Composable
fun NotesGrid(
    state: NotesViewModel.UiState,
    onAction: (Action, Note) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(
            items = state.notes,
            key = { note -> note.id }
        ) { note ->
            NoteGridItem(
                note = note,
                onAction = { action -> onAction(action, note) }
            )
        }
    }
}

@Composable
fun NoteGridItem(note: Note, onAction: (Action) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            MoreActionsIconButton(
                onAction = onAction,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
