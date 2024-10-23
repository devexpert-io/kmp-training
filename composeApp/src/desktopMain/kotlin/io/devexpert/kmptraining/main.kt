package io.devexpert.kmptraining

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.devexpert.kmptraining.data.DriverFactory

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMP Training",
    ) {
        App(DriverFactory())
    }
}