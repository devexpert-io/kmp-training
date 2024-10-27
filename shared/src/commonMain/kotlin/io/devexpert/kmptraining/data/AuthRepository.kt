package io.devexpert.kmptraining.data

import kotlinx.coroutines.flow.first

class AuthRepository(
    private val remoteDataSource: AuthRemoteDataSource,
    private val userStorage: UserStorage
) {
    val user = userStorage.user

    suspend fun isUserValidated(): Boolean {
        return userStorage.user.first() != null
    }

    suspend fun validateToken(authorization: String) {
        val user = remoteDataSource.validateToken(authorization)
        userStorage.saveUser(user)
    }
}