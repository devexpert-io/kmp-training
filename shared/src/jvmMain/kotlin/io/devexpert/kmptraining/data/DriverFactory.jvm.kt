package io.devexpert.kmptraining.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.devexpert.kmptraining.sqldelight.Database
import java.io.File

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:app.db")
        if (!File("app.db").exists()) {
            Database.Schema.create(driver)
        }
        return driver
    }
}