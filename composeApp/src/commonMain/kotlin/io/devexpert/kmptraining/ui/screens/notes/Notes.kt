package io.devexpert.kmptraining.ui.screens.notes

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.ui.common.ErrorMessage
import io.devexpert.kmptraining.ui.common.LoadingIndicator
import io.devexpert.kmptraining.ui.domain.Action
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.notes
import kmptraining.composeapp.generated.resources.switch_to_grid_view
import kmptraining.composeapp.generated.resources.switch_to_list_view
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object NotesScreen

@Composable
fun Notes(
    viewModel: NotesViewModel = koinViewModel(),
    onNoteClick: (Note) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Notes(
        state = state,
        onAction = viewModel::onAction,
        onNoteClick = onNoteClick,
        onMessageRemoved = viewModel::messageShown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notes(
    state: NotesViewModel.UiState,
    onAction: (Action, Note) -> Unit,
    onNoteClick: (Note) -> Unit,
    onMessageRemoved: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val message = state.message?.let { stringResource(it) }
    LaunchedEffect(state.message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            onMessageRemoved()
        }
    }

    var isGrid by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NotesTopAppBar(
                isGrid = isGrid,
                onToggleView = { isGrid = !isGrid },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onNoteClick(Note.Empty) }) {
                Icon(Icons.Default.Add, contentDescription = "Add note")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when {
            state.isLoading -> LoadingIndicator(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )

            state.error != null -> {
                ErrorMessage(
                    error = state.error,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            else -> {
                Crossfade(targetState = isGrid) { isGrid ->
                    if (isGrid) {
                        NotesGrid(
                            state = state,
                            onAction = onAction,
                            onNoteClick = onNoteClick,
                            modifier = Modifier.fillMaxSize().padding(innerPadding)
                        )
                    } else {
                        NotesList(
                            state = state,
                            onAction = onAction,
                            onNoteClick = onNoteClick,
                            modifier = Modifier.fillMaxSize().padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopAppBar(
    isGrid: Boolean,
    onToggleView: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { Text(stringResource(Res.string.notes)) },
        actions = {
            IconButton(onClick = onToggleView) {
                Icon(
                    imageVector = if (isGrid) Icons.AutoMirrored.Default.ViewList else Icons.Default.GridView,
                    contentDescription = stringResource(if (isGrid) Res.string.switch_to_list_view else Res.string.switch_to_grid_view)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}
