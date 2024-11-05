package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.User
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.flow.Flow

interface UserStorage {
    val user: Flow<User?>
    suspend fun saveUser(user: User)
    suspend fun clearUser()
}

class KStoreUserStorage(
    private val store: KStore<User>
) : UserStorage {

    override val user: Flow<User?> = store.updates

    override suspend fun saveUser(user: User) {
        store.set(user)
    }

    override suspend fun clearUser() {
        store.delete()
    }
}