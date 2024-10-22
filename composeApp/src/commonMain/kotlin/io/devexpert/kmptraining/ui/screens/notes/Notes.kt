package io.devexpert.kmptraining.ui.screens.notes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.ui.domain.Action

@Composable
fun Notes(viewModel: NotesViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Notes(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun Notes(
    state: NotesViewModel.UiState,
    onAction: (Action, Note) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error != null -> {
                    Text(
                        text = state.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    NotesGrid(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}