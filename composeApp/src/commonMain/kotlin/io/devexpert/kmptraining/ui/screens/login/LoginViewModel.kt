package io.devexpert.kmptraining.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.devexpert.kmptraining.data.AuthRepository
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.could_not_login
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun loginClicked() {
        viewModelScope.launch {
            try {
                _state.value = UiState(loading = true)
                val loginUrl = authRepository.initiateOAuth()
                _state.update { it.copy(loginUrl = loginUrl) }

                authRepository.handleAuthCode()
                _state.value = UiState(loggedIn = true)
            } catch (e: Exception) {
                _state.value = UiState(error = Res.string.could_not_login)
                authRepository.stop()
            }
        }
    }

    data class UiState(
        val loggedIn: Boolean = false,
        val loading: Boolean = false,
        val loginUrl: String? = null,
        val error: StringResource? = null
    )
}
