package io.devexpert.kmptraining.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.devexpert.kmptraining.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

enum class LoginState {
    NOT_STARTED,
    LOGGING_IN,
    LOGGED_IN,
}

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun onLoginClick() {
        viewModelScope.launch {
            if (authRepository.isUserValidated()) {
                _state.update { it.copy(loginState = LoginState.LOGGED_IN) }
            } else {
                _state.update { it.copy(loginState = LoginState.LOGGING_IN) }
            }
        }
    }

    fun onSignIn(token: String) {
        viewModelScope.launch {
            authRepository.validateToken(token)
            _state.update { it.copy(loginState = LoginState.LOGGED_IN) }
        }
    }

    data class UiState(
        val loginState: LoginState = LoginState.NOT_STARTED,
        val error: StringResource? = null
    )
}
