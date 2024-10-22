package io.devexpert.kmptraining.ui.screens.notedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.ui.common.ErrorMessage
import io.devexpert.kmptraining.ui.common.LoadingIndicator
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.back
import kmptraining.composeapp.generated.resources.note_content
import kmptraining.composeapp.generated.resources.note_detail
import kmptraining.composeapp.generated.resources.note_title
import kmptraining.composeapp.generated.resources.save
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data class NoteDetailScreen(val noteId: Int)

@Composable
fun NoteDetail(viewModel: NoteDetailViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    NoteDetail(
        state = state,
        onSave = { viewModel.saveNote(it); onBack() },
        onBack = onBack
    )
}

@Composable
fun NoteDetail(
    state: NoteDetailViewModel.UiState,
    onSave: (Note) -> Unit,
    onBack: () -> Unit
) {
    var updatedNote by remember(state.note) { mutableStateOf(state.note ?: Note.Empty) }

    Scaffold(
        topBar = {
            DetailTopAppBar(
                onBack = onBack,
                onSave = { onSave(updatedNote) }
            )
        }
    ) { innerPadding ->
        when {
            state.isLoading -> LoadingIndicator(modifier = Modifier.padding(innerPadding))

            state.error != null -> {
                ErrorMessage(
                    error = state.error,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
                state.note?.let { note ->
                    NoteDetailContent(
                        note = updatedNote,
                        onUpdate = { updatedNote = it },
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(
    onBack: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(Res.string.note_detail)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(Res.string.back)
                )
            }
        },
        actions = {
            IconButton(onClick = onSave) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = stringResource(Res.string.save)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun NoteDetailContent(note: Note, onUpdate: (Note) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = note.title,
            onValueChange = { onUpdate(note.copy(title = it)) },
            label = { Text(stringResource(Res.string.note_title)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = note.content,
            onValueChange = { onUpdate(note.copy(content = it)) },
            label = { Text(stringResource(Res.string.note_content)) },
            modifier = Modifier.fillMaxSize()
        )
    }
}