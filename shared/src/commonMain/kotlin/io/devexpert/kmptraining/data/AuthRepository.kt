package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import kotlinx.coroutines.flow.first

class AuthRepository(
    private val serverUrl: String,
    private val userStorage: UserStorage,
    private val httpClient: HttpClient
) {
    val user = userStorage.user

    suspend fun isUserValidated(): Boolean {
        return userStorage.user.first() != null
    }

    suspend fun validateToken(authorization: String) {
        val user = httpClient.post("$serverUrl/login") {
            header("Authorization", authorization)
        }.body<User>()

        userStorage.saveUser(user)
    }
}