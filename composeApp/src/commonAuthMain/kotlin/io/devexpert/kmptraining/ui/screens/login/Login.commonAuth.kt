package io.devexpert.kmptraining.ui.screens.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton

@Composable
actual fun LoginButton(
    loginState: LoginState,
    onButtonClick: () -> Unit,
    onSignIn: (String) -> Unit
) {
    GoogleButtonUiContainer(onGoogleSignInResult = { googleUser ->
        onSignIn(googleUser?.idToken ?: "")
    }) {
        LaunchedEffect(loginState) {
            if (loginState == LoginState.LOGGING_IN) {
                onClick()
            }
        }
        GoogleSignInButton(onClick = {
            if (loginState == LoginState.NOT_STARTED) {
                onButtonClick()
            }
        })
    }
}