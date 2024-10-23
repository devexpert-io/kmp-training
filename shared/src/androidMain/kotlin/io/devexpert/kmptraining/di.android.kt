package io.devexpert.kmptraining

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.devexpert.kmptraining.sqldelight.Database
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val nativeModule: Module = module {
    single<SqlDriver> { AndroidSqliteDriver(Database.Schema, get(), "notes.db") }
    single(named(Named.SERVER_URL)) { "http://10.0.2.2:8080" }
}