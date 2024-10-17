package io.devexpert.kmptraining

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import io.devexpert.kmptraining.ui.screens.notes.Notes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Notes()
    }
}