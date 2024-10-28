package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.UserInfo
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.flow.Flow

interface UserInfoStorage {
    val userInfo: Flow<UserInfo?>
    suspend fun saveUserInfo(userInfo: UserInfo)
    suspend fun clearUserInfo()
}

class KStoreUserInfoStorage(
    private val store: KStore<UserInfo>
) : UserInfoStorage {

    override val userInfo = store.updates

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        store.set(userInfo)
    }

    override suspend fun clearUserInfo() {
        store.delete()
    }
}
