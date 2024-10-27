package io.devexpert.kmptraining.data

import io.github.xxfast.kstore.KStore

interface TokenStorage {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}

typealias Token = String

class KStoreTokenStorage(
    private val store: KStore<Token>
) : TokenStorage {
    override suspend fun saveToken(token: Token) {
        store.set(token)
    }

    override suspend fun getToken(): Token? {
        return store.get()
    }

    override suspend fun clearToken() {
        store.delete()
    }
}
