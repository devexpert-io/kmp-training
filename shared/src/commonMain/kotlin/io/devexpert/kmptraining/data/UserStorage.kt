package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.User
import io.github.xxfast.kstore.KStore

interface UserStorage {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User?
    suspend fun clearUser()
}

class KStoreUserStorage(
    private val store: KStore<User>
) : UserStorage {

    override suspend fun saveUser(user: User) {
        store.set(user)
    }

    override suspend fun getUser(): User? {
        return store.get()
    }

    override suspend fun clearUser() {
        store.delete()
    }
}