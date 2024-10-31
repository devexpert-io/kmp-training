package io.devexpert.kmptraining.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.short_password
import kmptraining.composeapp.generated.resources.wrong_username
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun loginClicked(user: String, pass: String) {
        viewModelScope.launch {
            _state.update {
                when {
                    !user.contains('@') -> it.copy(error = Res.string.wrong_username)
                    pass.length < 5 -> it.copy(error = Res.string.short_password)
                    else -> it.copy(loggedIn = true, error = null)
                }
            }
        }
    }

    data class UiState(
        val loggedIn: Boolean = false,
        val error: StringResource? = null,
        val loading: Boolean = false,
    )
}
