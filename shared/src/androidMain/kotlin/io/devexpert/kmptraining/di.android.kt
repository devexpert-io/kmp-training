package io.devexpert.kmptraining

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.devexpert.kmptraining.domain.User
import io.devexpert.kmptraining.sqldelight.Database
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val nativeModule: Module = module {
    single<SqlDriver> { AndroidSqliteDriver(Database.Schema, get(), "notes.db") }
    single(named(Named.SERVER_URL)) { "http://10.0.2.2:$SERVER_PORT" }
    single<KStore<User>> {
        val filesDir: String = get<Context>().filesDir.path
        storeOf<User>(Path("$filesDir/user.json"), null)
    }
}