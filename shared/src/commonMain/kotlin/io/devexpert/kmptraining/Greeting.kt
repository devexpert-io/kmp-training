package io.devexpert.kmptraining

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class Greeting {
    private val client = HttpClient()

    suspend fun greet(): String {
        val response = client.get("http://10.0.2.2:8080/notes")
        //val response = client.get("http://0.0.0.0:8080/notes")
        return response.bodyAsText()
    }
}