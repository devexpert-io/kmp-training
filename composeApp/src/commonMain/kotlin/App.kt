import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import io.devexpert.eventapp.data.TalksRepository
import io.devexpert.eventapp.domain.Talk
import ui.screens.talks.Talks

@Composable
fun App() {
    MaterialTheme {
        val talks by produceState(initialValue = emptyList<Talk>()) {
            value = TalksRepository().getTalks()
        }

        Surface(modifier = Modifier.fillMaxSize()) {
            Talks(talks)
        }
    }
}