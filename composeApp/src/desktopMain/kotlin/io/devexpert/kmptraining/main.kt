package io.devexpert.kmptraining

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Dimension

fun main() = application {
    AppInitializer.onApplicationStart()
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "KMP Training",
    ) {
        window.minimumSize = Dimension(600,400)
        App()
    }
}