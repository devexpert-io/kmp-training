package io.devexpert.kmptraining.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.devexpert.kmptraining.ui.common.StyledButton
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.login_button
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
object LoginScreen

@Composable
fun Login(
    viewModel: LoginViewModel = viewModel { LoginViewModel() },
    onLoggedIn: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Login(
        state = state,
        onLoginClick = viewModel::loginClicked,
        onLoggedIn = onLoggedIn
    )
}

@Composable
fun Login(
    state: LoginViewModel.UiState,
    onLoginClick: (String, String) -> Unit,
    onLoggedIn: () -> Unit
) {
    LaunchedEffect(state.loggedIn) {
        if (state.loggedIn) {
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
            LoginForm(
                onLoginClick = onLoginClick,
                errorMessage = state.error?.let { stringResource(it) }
            )

        }
    }
}

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    onLoginClick: (String, String) -> Unit,
    errorMessage: String?
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val isLoginEnabled = username.isNotBlank() && password.isNotBlank()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserTextField(
            value = username,
            onValueChange = { username = it }
        )
        PassTextField(
            value = password,
            onValueChange = { password = it },
            onDone = { if (isLoginEnabled) onLoginClick(username, password) }
        )
        StyledButton(
            onClick = { onLoginClick(username, password) },
            enabled = isLoginEnabled
        ) {
            Text(stringResource(Res.string.login_button))
        }
        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
