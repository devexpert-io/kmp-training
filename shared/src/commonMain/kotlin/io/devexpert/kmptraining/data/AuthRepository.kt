package io.devexpert.kmptraining.data

import kotlinx.coroutines.flow.first

class AuthRepository(
    private val serverUrl: String,
    private val oAuthServer: OAuthServer,
    private val userInfoStorage: UserInfoStorage,
) {
    suspend fun shouldInitiateOAuth(): Boolean {
        return userInfoStorage.getUserInfo() == null
    }

    fun initiateOAuth(): String {
        oAuthServer.start()
        return "$serverUrl/login"
    }

    suspend fun handleAuthCode() {
        val userInfo = oAuthServer.userInfo.first()
        userInfoStorage.saveUserInfo(userInfo)
    }

    fun stop() {
        oAuthServer.stop()
    }
}
