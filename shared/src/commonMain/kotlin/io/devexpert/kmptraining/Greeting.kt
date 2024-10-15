package io.devexpert.kmptraining

import io.devexpert.kmptraining.domain.serverUrl
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class Greeting {
    private val client = HttpClient()

    suspend fun greet(): String {
        val response = client.get("$serverUrl/notes")
        return response.bodyAsText()
    }
}