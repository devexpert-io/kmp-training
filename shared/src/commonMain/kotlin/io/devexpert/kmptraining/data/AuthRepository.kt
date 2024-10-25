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
        try {
            val token = oAuthServer.authCode.first()
            tokenStorage.saveToken(token)
        } finally {
            stop()
        }
    }

    suspend fun stop() {
        oAuthServer.stop()
    }
}