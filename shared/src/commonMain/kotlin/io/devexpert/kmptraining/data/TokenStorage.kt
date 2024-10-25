package io.devexpert.kmptraining.data

interface TokenStorage {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}

class InMemoryTokenStorage : TokenStorage {
    private var token: String? = null

    override fun saveToken(token: String) {
        this.token = token
    }

    override fun getToken(): String? {
        return token
    }

    override fun clearToken() {
        token = null
    }
}
