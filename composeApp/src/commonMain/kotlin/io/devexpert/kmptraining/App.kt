package io.devexpert.kmptraining

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.devexpert.kmptraining.ui.screens.login.Login
import io.devexpert.kmptraining.ui.screens.login.LoginScreen
import io.devexpert.kmptraining.ui.screens.notes.Notes
import io.devexpert.kmptraining.ui.screens.notes.NotesScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = LoginScreen) {

            composable<LoginScreen> {
                Login(onLoggedIn = { navController.navigate(NotesScreen) {
                    popUpTo(LoginScreen) { inclusive = true }
                } })
            }

            composable<NotesScreen> { Notes() }
        }
    }
}