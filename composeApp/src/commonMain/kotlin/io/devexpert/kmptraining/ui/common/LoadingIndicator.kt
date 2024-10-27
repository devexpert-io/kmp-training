package io.devexpert.kmptraining.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

const val LOADING_INDICATOR_TAG = "loading_indicator"

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier.testTag(LOADING_INDICATOR_TAG)) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}