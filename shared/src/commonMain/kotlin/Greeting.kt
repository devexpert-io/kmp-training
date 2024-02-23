import io.devexpert.eventapp.domain.Talk
import io.devexpert.eventapp.serverUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class Greeting {
    private val client = HttpClient {
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
            })
        }
    }

    suspend fun greet(): List<Talk> {
        val response = client.get(serverUrl)
        return response.body()
    }
}