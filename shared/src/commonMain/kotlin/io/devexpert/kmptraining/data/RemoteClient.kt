package io.devexpert.kmptraining.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object RemoteClient {
    val instance = HttpClient {
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
            })
        }
    }
}