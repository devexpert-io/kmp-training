package io.devexpert.kmptraining

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        MyButton("Click Me")
    }
}

@Composable
fun MyButton(text: String) {
    Button(onClick = { println("Button Clicked") }) {
        Text(text = text)
    }
}
