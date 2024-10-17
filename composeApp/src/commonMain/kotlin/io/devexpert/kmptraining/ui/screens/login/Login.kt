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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.login_button
import kmptraining.composeapp.generated.resources.login_successful
import org.jetbrains.compose.resources.stringResource

@Composable
fun Login(viewModel: LoginViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.loggedIn) {
                Text(
                    text = stringResource(Res.string.login_successful),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium
                )
            } else {
                LoginForm(
                    onLoginClick = viewModel::loginClicked,
                    errorMessage = state.error?.let { stringResource(it) }
                )
            }
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
        Button(
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
