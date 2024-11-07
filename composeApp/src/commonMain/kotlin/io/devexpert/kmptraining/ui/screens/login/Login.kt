package io.devexpert.kmptraining.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.devexpert.kmptraining.ui.common.LoadingIndicator
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object LoginScreen

@Composable
fun Login(
    viewModel: LoginViewModel = koinViewModel(),
    onLoggedIn: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Login(
        state = state,
        onLoginClick = viewModel::onLoginClick,
        onSignIn = viewModel::onSignIn,
        onLoggedIn = onLoggedIn
    )
}

@Composable
fun Login(
    state: LoginViewModel.UiState,
    onLoginClick: () -> Unit,
    onSignIn: (String) -> Unit,
    onLoggedIn: () -> Unit
) {
    LaunchedEffect(state.loginState) {
        if (state.loginState == LoginState.LOGGED_IN) {
            onLoggedIn()
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.wrapContentSize()
            ) {
                LoginButton(
                    loginState = state.loginState,
                    onButtonClick = onLoginClick,
                    onSignIn = onSignIn
                )
                Spacer(modifier = Modifier.height(16.dp))
                when {
                    state.loginState == LoginState.LOGGING_IN -> LoadingIndicator(Modifier.wrapContentSize())
                    state.error != null -> Text(stringResource(state.error))
                }
            }
        }
    }
}

@Composable
expect fun LoginButton(
    loginState: LoginState,
    onButtonClick: () -> Unit,
    onSignIn: (String) -> Unit
)