package io.devexpert.kmptraining.data

import kotlinx.coroutines.flow.first

class AuthRepository(
    private val serverUrl: String,
    private val oAuthServer: OAuthServer,
    private val tokenStorage: TokenStorage,
) {
    fun initiateOAuth(): String {
        oAuthServer.start()
        return "$serverUrl/login"
    }

    suspend fun handleAuthCode() {
        val token = oAuthServer.authCode.first()
        tokenStorage.saveToken(token)
    }

    fun stop() {
        oAuthServer.stop()
    }
}