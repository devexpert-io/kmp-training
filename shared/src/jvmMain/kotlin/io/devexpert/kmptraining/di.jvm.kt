package io.devexpert.kmptraining

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.devexpert.kmptraining.sqldelight.Database
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

actual val nativeModule: Module = module {
    single<SqlDriver> {
        JdbcSqliteDriver("jdbc:sqlite:app.db")
            .also {
                if (!File("app.db").exists()) {
                    Database.Schema.create(it)
                }
            }
    }
    single(named(Named.SERVER_URL)) { "http://0.0.0.0:$SERVER_PORT" }
}