package ui.screens.talks

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import io.devexpert.eventapp.domain.Talk

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Talks(talks: List<Talk>) {
    LazyColumn {
        items(talks) { talk ->
            ListItem(
                text = { Text(talk.title) },
                secondaryText = { Text(talk.speaker.name) }
            )
        }
    }
}