import io.devexpert.eventapp.serverUrl
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class Greeting {
    private val client = HttpClient()

    suspend fun greet(): String {
        val response = client.get(serverUrl)
        return response.bodyAsText()
    }
}