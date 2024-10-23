package io.devexpert.kmptraining

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.devexpert.kmptraining.ui.screens.login.Login
import io.devexpert.kmptraining.ui.screens.login.LoginScreen
import io.devexpert.kmptraining.ui.screens.notedetail.NoteDetail
import io.devexpert.kmptraining.ui.screens.notedetail.NoteDetailScreen
import io.devexpert.kmptraining.ui.screens.notes.Notes
import io.devexpert.kmptraining.ui.screens.notes.NotesScreen
import io.devexpert.kmptraining.ui.theme.KmpTrainingTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Preview
fun App() {
    KoinContext {
        KmpTrainingTheme {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = LoginScreen) {

                composable<LoginScreen> {
                    Login(onLoggedIn = {
                        navController.navigate(NotesScreen) {
                            popUpTo(LoginScreen) { inclusive = true }
                        }
                    }
                    )
                }

                composable<NotesScreen> {
                    Notes(
                        onNoteClick = { navController.navigate(NoteDetailScreen(it.id)) }
                    )
                }

                composable<NoteDetailScreen> { backStackEntry ->
                    val noteScreen = backStackEntry.toRoute<NoteDetailScreen>()
                    NoteDetail(
                        viewModel = koinViewModel { parametersOf(noteScreen.noteId) },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
