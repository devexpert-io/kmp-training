package io.devexpert.kmptraining

import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.User
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val nativeModule: Module = module {
    single<KStore<User>>(named(Named.USER_STORE)) {
        val fileManager = NSFileManager.defaultManager
        val documentsUrl: NSURL = fileManager.URLForDirectory(
            directory = NSDocumentDirectory,
            appropriateForURL = null,
            create = false,
            inDomain = NSUserDomainMask,
            error = null
        )!!
        storeOf(Path("${documentsUrl.path}/user.json"), null)
    }
    single<KStore<List<Note>>>(named(Named.NOTES_STORE)) {
        val fileManager = NSFileManager.defaultManager
        val documentsUrl: NSURL = fileManager.URLForDirectory(
            directory = NSDocumentDirectory,
            appropriateForURL = null,
            create = false,
            inDomain = NSUserDomainMask,
            error = null
        )!!
        storeOf(Path("${documentsUrl.path}/notes.json"), null)
    }
    single(named(Named.SERVER_URL)) { "http://0.0.0.0:$SERVER_PORT" }
}