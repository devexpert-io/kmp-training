package io.devexpert.kmptraining.ui.screens.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.ui.domain.Action

@Composable
fun NotesList(
    state: NotesViewModel.UiState,
    onAction: (Action, Note) -> Unit,
    onNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(
            items = state.notes,
            key = { _, note -> note.id }
        ) { index, note ->
            if (index > 0) {
                HorizontalDivider()
            }
            NoteListItem(
                note = note,
                onAction = { action -> onAction(action, note) },
                onClick = { onNoteClick(note) },
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
fun NoteListItem(
    note: Note,
    onAction: (Action) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(text = note.title) },
        supportingContent = { Text(text = note.content) },
        trailingContent = { MoreActionsIconButton(onAction) },
        modifier = modifier.clickable(onClick = onClick)
    )
}
