package io.devexpert.kmptraining.ui.screens.login

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
actual fun LoginButton(
    loginState: LoginState,
    onButtonClick: () -> Unit,
    onSignIn: (String) -> Unit
) {
    Button(onClick = {}) {
        Text("I'm a useless login button")
    }
}