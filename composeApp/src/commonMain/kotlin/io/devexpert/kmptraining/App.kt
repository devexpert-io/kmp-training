package io.devexpert.kmptraining

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.devexpert.kmptraining.ui.screens.notedetail.NoteDetail
import io.devexpert.kmptraining.ui.screens.notedetail.NoteDetailScreen
import io.devexpert.kmptraining.ui.screens.notes.Notes
import io.devexpert.kmptraining.ui.screens.notes.NotesScreen
import io.devexpert.kmptraining.ui.theme.KmpTrainingTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

enum class UiMode {
    Light, Dark, System
}

val LocalUiMode = compositionLocalOf { UiMode.System }

@Composable
@Preview
fun App() {
    KoinContext {
        var uiMode by remember { mutableStateOf(UiMode.System) }
        CompositionLocalProvider(LocalUiMode provides uiMode) {
            KmpTrainingTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NotesScreen
                    ) {
                        composable<NotesScreen> {
                            MaxWidthContainer {
                                Notes(
                                    onNoteClick = { navController.navigate(NoteDetailScreen(it.id)) },
                                    onUiModeChange = { uiMode = it }
                                )
                            }
                        }

                        composable<NoteDetailScreen> { backStackEntry ->
                            val noteScreen = backStackEntry.toRoute<NoteDetailScreen>()
                            MaxWidthContainer {
                                NoteDetail(
                                    viewModel = koinViewModel { parametersOf(noteScreen.noteId) },
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MaxWidthContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.widthIn(max = 1024.dp)
        ) {
            content()
        }
    }
}