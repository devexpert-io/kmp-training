package io.devexpert.kmptraining.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.short_password
import kmptraining.composeapp.generated.resources.wrong_username
import org.jetbrains.compose.resources.StringResource

class LoginViewModel : ViewModel() {

    var state by mutableStateOf(UiState())

    data class UiState(
        val loggedIn: Boolean = false,
        val error: StringResource? = null
    )

    fun loginClicked(user: String, pass: String) {
        state = when {
            !user.contains('@') -> UiState(error = Res.string.wrong_username)
            pass.length < 5 -> UiState(error = Res.string.short_password)
            else -> UiState(loggedIn = true)
        }
    }
}