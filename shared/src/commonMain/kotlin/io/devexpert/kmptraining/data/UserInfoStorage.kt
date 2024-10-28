package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.UserInfo
import io.github.xxfast.kstore.KStore

interface UserInfoStorage {
    suspend fun saveUserInfo(userInfo: UserInfo)
    suspend fun getUserInfo(): UserInfo?
    suspend fun clearUserInfo()
}

class KStoreUserInfoStorage(
    private val store: KStore<UserInfo>
) : UserInfoStorage {
    override suspend fun saveUserInfo(userInfo: UserInfo) {
        store.set(userInfo)
    }

    override suspend fun getUserInfo(): UserInfo? {
        return store.get()
    }

    override suspend fun clearUserInfo() {
        store.delete()
    }
}
