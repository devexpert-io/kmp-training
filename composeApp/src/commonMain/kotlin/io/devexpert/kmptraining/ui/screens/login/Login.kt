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
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.wrapContentSize()
            ) {
                LoginButton(onLoginClick, onSignIn)
                Spacer(modifier = Modifier.height(16.dp))
                when {
                    state.loading -> LoadingIndicator()
                    state.error != null -> Text(stringResource(state.error))
                }
            }
        }
    }
}

@Composable
fun LoginButton(
    onButtonClick: () -> Unit,
    onSignIn: (String) -> Unit
) {
    GoogleButtonUiContainer(onGoogleSignInResult = { googleUser ->
        onSignIn(googleUser?.idToken ?: "")
    }) {
        GoogleSignInButton(onClick = {
            onButtonClick()
            this.onClick()
        })
    }
}