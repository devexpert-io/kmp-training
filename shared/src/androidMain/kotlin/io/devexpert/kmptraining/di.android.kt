package io.devexpert.kmptraining

import android.content.Context
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.User
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val nativeModule: Module = module {
    single<KStore<User>>(named(Named.USER_STORE)) {
        val filesDir: String = get<Context>().filesDir.path
        storeOf(Path("$filesDir/user.json"), null)
    }
    single<KStore<List<Note>>>(named(Named.NOTES_STORE)) {
        val filesDir: String = get<Context>().filesDir.path
        storeOf(Path("$filesDir/notes.json"), null)
    }
    single(named(Named.SERVER_URL)) { "http://10.0.2.2:$SERVER_PORT" }
}