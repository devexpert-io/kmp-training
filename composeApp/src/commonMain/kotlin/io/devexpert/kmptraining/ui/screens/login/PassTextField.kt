package io.devexpert.kmptraining.ui.screens.login

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.hide_password
import kmptraining.composeapp.generated.resources.login_password
import kmptraining.composeapp.generated.resources.login_password_placeholder
import kmptraining.composeapp.generated.resources.reveal_password
import org.jetbrains.compose.resources.stringResource

@Composable
fun PassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(stringResource(Res.string.login_password)) },
        placeholder = { Text(stringResource(Res.string.login_password_placeholder)) },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        isError = isError,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions { onDone() },
        trailingIcon = { PasswordVisibilityIcon(
            isPasswordVisible = isPasswordVisible,
            onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible }
        ) }
    )
}

@Composable
private fun PasswordVisibilityIcon(isPasswordVisible: Boolean, onTogglePasswordVisibility: () -> Unit) {
    IconButton(onClick = { onTogglePasswordVisibility() }) {
        if (isPasswordVisible) {
            Icon(
                imageVector = Icons.Default.VisibilityOff,
                contentDescription = stringResource(Res.string.hide_password)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = stringResource(Res.string.reveal_password)
            )
        }
    }
}
