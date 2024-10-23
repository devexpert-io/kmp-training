package io.devexpert.kmptraining

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.devexpert.kmptraining.data.DriverFactory
import io.devexpert.kmptraining.data.NotesLocalDataSource
import io.devexpert.kmptraining.data.NotesRemoteDataSource
import io.devexpert.kmptraining.data.NotesRepository
import io.devexpert.kmptraining.data.createDatabase
import io.devexpert.kmptraining.ui.screens.login.Login
import io.devexpert.kmptraining.ui.screens.login.LoginScreen
import io.devexpert.kmptraining.ui.screens.notedetail.NoteDetail
import io.devexpert.kmptraining.ui.screens.notedetail.NoteDetailScreen
import io.devexpert.kmptraining.ui.screens.notedetail.NoteDetailViewModel
import io.devexpert.kmptraining.ui.screens.notes.Notes
import io.devexpert.kmptraining.ui.screens.notes.NotesScreen
import io.devexpert.kmptraining.ui.screens.notes.NotesViewModel
import io.devexpert.kmptraining.ui.theme.KmpTrainingTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(driverFactory: DriverFactory) {
    KmpTrainingTheme {
        val navController = rememberNavController()
        val notesRepository = remember {
            NotesRepository(
                localDataSource = NotesLocalDataSource(createDatabase(driverFactory).notesQueries),
                remoteDataSource = NotesRemoteDataSource()
            )
        }
        NavHost(navController = navController, startDestination = NotesScreen) {

            composable<LoginScreen> {
                Login(onLoggedIn = {
                    navController.navigate(NotesScreen) {
                        popUpTo(LoginScreen) { inclusive = true }
                    }
                })
            }

            composable<NotesScreen> {
                Notes(
                    viewModel = viewModel { NotesViewModel(notesRepository) },
                    onNoteClick = { navController.navigate(NoteDetailScreen(it.id)) }
                )
            }

            composable<NoteDetailScreen> { backStackEntry ->
                val noteScreen = backStackEntry.toRoute<NoteDetailScreen>()
                val vm = viewModel { NoteDetailViewModel(noteScreen.noteId, notesRepository) }
                NoteDetail(
                    viewModel = vm,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}