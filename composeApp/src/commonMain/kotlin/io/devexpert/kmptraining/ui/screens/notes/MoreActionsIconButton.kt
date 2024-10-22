package io.devexpert.kmptraining.ui.screens.notes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.devexpert.kmptraining.ui.domain.Action
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.clone
import kmptraining.composeapp.generated.resources.delete
import kmptraining.composeapp.generated.resources.more_actions
import org.jetbrains.compose.resources.stringResource

@Composable
fun MoreActionsIconButton(
    onAction: (Action) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(
        onClick = { expanded = true },
        modifier = modifier
    ) {
        Icon(
            Icons.Default.MoreVert,
            contentDescription = stringResource(Res.string.more_actions)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.clone)) },
                onClick = { onAction(Action.CLONE); expanded = false }
            )
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.delete)) },
                onClick = { onAction(Action.DELETE); expanded = false }
            )
        }
    }
}
