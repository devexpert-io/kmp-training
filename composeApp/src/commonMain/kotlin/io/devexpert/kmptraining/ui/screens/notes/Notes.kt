package io.devexpert.kmptraining.ui.screens.notes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.devexpert.kmptraining.domain.Note

@Composable
fun Notes(notes: List<Note>) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(notes, key = { it.id }) { note ->
                ListItem(
                    headlineContent = { Text(text = note.title) },
                    supportingContent = { Text(text = note.content) }
                )
            }
        }
    }
}