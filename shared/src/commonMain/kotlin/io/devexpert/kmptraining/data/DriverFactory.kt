package io.devexpert.kmptraining.data

import app.cash.sqldelight.db.SqlDriver
import io.devexpert.kmptraining.sqldelight.Database

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): Database {
    val driver = driverFactory.createDriver()
    return Database(driver)
}