package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.User

interface UserStorage {
    fun saveUser(user: User)
    fun getUser(): User?
    fun clearUser()
}

class InMemoryUserStorage : UserStorage {
    private var user: User? = null

    override fun saveUser(user: User) {
        this.user = user
    }

    override fun getUser(): User? {
        return user
    }

    override fun clearUser() {
        user = null
    }
}