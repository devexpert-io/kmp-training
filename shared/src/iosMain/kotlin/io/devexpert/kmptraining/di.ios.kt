package io.devexpert.kmptraining

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.devexpert.kmptraining.domain.User
import io.devexpert.kmptraining.sqldelight.Database
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
    single<SqlDriver> { NativeSqliteDriver(Database.Schema, "notes.db") }
    single(named(Named.SERVER_URL)) { "http://0.0.0.0:$SERVER_PORT" }
    single<KStore<User>> {
        val fileManager: NSFileManager = NSFileManager.defaultManager
        val documentsUrl: NSURL = fileManager.URLForDirectory(
            directory = NSDocumentDirectory,
            appropriateForURL = null,
            create = false,
            inDomain = NSUserDomainMask,
            error = null
        )!!
        storeOf<User>(Path("${documentsUrl.path}/token.json"), null)
    }
}