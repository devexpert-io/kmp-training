package io.devexpert.kmptraining

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import io.devexpert.kmptraining.domain.User
import io.devexpert.kmptraining.sqldelight.Database
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import net.harawata.appdirs.AppDirsFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

private const val PACKAGE_NAME = "io.devexpert.kmptraining"
private const val VERSION = "1.0"
private const val ORGANISATION = "devexpert"

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
    single<KStore<User>> {
        val filesDir = AppDirsFactory
            .getInstance()
            .getUserDataDir(PACKAGE_NAME, VERSION, ORGANISATION)
        val filesPath = Path(filesDir)
        with(SystemFileSystem) { if (!exists(filesPath)) createDirectories(filesPath) }
        storeOf<User>(Path("$filesDir/user.json"), null)
    }
}