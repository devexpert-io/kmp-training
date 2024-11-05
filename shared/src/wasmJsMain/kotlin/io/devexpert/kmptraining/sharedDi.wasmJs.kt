package io.devexpert.kmptraining

import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.User
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storage.storeOf
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val nativeModule: Module = module {
    single<KStore<User>>(named(Named.USER_STORE)) {
        storeOf(key = "user", null)
    }
    single<KStore<List<Note>>>(named(Named.NOTES_STORE)) {
        storeOf(key = "notes", null)
    }
    single(named(Named.SERVER_URL)) { "http://0.0.0.0:$SERVER_PORT" }
}