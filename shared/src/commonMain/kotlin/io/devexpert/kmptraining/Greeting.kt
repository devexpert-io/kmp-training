package io.devexpert.kmptraining

import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.serverUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class Greeting {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }
    }

    suspend fun greet(): List<Note> {
        val response = client.get("$serverUrl/notes")
        return response.body()
    }
}