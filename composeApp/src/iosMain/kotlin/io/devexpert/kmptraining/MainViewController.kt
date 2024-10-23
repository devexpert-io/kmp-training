package io.devexpert.kmptraining

import androidx.compose.ui.window.ComposeUIViewController
import io.devexpert.kmptraining.data.DriverFactory

fun MainViewController() = ComposeUIViewController { App(DriverFactory()) }