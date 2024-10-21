package io.devexpert.kmptraining.ui.screens.notes

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.ui.domain.Action

@Composable
fun NoteItem(note: Note, onAction: (Action) -> Unit) {
    ListItem(
        headlineContent = { Text(text = note.title) },
        supportingContent = { Text(text = note.content) },
        trailingContent = { MoreActionsIconButton(onAction) }
    )
}