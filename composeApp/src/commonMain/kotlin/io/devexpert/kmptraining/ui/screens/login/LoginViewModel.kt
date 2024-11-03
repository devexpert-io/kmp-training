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

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun onLoginClick() {
        _state.update { it.copy(loading = true) }
    }

    fun onSignIn(token: String) {
        viewModelScope.launch {
            authRepository.validateToken(token)
            _state.update { it.copy(loggedIn = true) }
        }
    }

    data class UiState(
        val loggedIn: Boolean = false,
        val error: StringResource? = null,
        val loading: Boolean = false,
    )
}
