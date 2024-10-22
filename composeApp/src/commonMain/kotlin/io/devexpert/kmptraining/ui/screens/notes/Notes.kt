package io.devexpert.kmptraining.ui.screens.notes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.ui.domain.Action
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.notes
import kmptraining.composeapp.generated.resources.switch_to_grid_view
import kmptraining.composeapp.generated.resources.switch_to_list_view
import org.jetbrains.compose.resources.stringResource

@Composable
fun Notes(viewModel: NotesViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Notes(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notes(
    state: NotesViewModel.UiState,
    onAction: (Action, Note) -> Unit
) {
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
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Add, contentDescription = "Add note")
            }
        }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    Text(
                        text = state.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            else -> {
                if (isGrid) {
                    NotesGrid(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )
                } else {
                    NotesList(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    )
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
