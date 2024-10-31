package io.devexpert.kmptraining.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            when {
                state.loading -> LoadingIndicator()
                state.error != null -> Text(stringResource(state.error))
                else -> LoginButton()
            }

        }
    }
}

@Composable
fun LoginButton() {
    GoogleButtonUiContainer(onGoogleSignInResult = { googleUser ->
        println("GOOGLE USER = ${googleUser?.idToken}")
    }) {
        GoogleSignInButton(
            //modifier = Modifier.fillMaxWidth().height(44.dp),
            //fontSize = 19.sp
        ) { this.onClick() }
    }
}