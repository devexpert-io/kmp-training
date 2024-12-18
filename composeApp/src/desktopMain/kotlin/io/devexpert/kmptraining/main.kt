package io.devexpert.kmptraining

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    AppInitializer.onApplicationStart()
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMP Training",
    ) {
        App()
    }
}