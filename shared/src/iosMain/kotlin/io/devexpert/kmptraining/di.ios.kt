package io.devexpert.kmptraining

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.devexpert.kmptraining.sqldelight.Database
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val nativeModule: Module = module {
    single<SqlDriver> { NativeSqliteDriver(Database.Schema, "notes.db") }
    single(named(Named.SERVER_URL)) { "http://0.0.0.0:8080" }
}