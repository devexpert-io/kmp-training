package io.devexpert.kmptraining.ui.screens.login

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.login_username
import kmptraining.composeapp.generated.resources.login_username_placeholder
import org.jetbrains.compose.resources.stringResource

@Composable
fun UserTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(Res.string.login_username)) },
        placeholder = { Text(stringResource(Res.string.login_username_placeholder)) },
        isError = isError,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
}
