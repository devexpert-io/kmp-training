package io.devexpert.kmptraining.ui.screens.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
            val infiniteTransition = rememberInfiniteTransition(label = "bgColorTransition")
            val bgColor by infiniteTransition.animateColor(
                initialValue = MaterialTheme.colorScheme.surface,
                targetValue = MaterialTheme.colorScheme.surfaceVariant,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "bgColor"
            )

            LoginForm(
                onLoginClick = onLoginClick,
                errorMessage = state.error?.let { stringResource(it) },
                modifier = Modifier
                    .background(bgColor)
                    .padding(16.dp),
            )

        }
    }
}

@Composable
fun LoginForm(
    onLoginClick: (String, String) -> Unit,
    errorMessage: String?,
    modifier: Modifier = Modifier
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

        AnimatedVisibility(isLoginEnabled) {
            StyledButton(
                onClick = { onLoginClick(username, password) }
            ) {
                Text(stringResource(Res.string.login_button))
            }
        }

        AnimatedVisibility(errorMessage != null) {
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
