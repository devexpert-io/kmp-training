package io.devexpert.kmptraining

import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.User
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import net.harawata.appdirs.AppDirsFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val PACKAGE_NAME = "io.devexpert.kmptraining"
private const val VERSION = "1.0"
private const val ORGANISATION = "devexpert"

actual val nativeModule: Module = module {
    single<KStore<User>>(named(Named.USER_STORE)) {
        val filesDir = AppDirsFactory
            .getInstance()
            .getUserDataDir(PACKAGE_NAME, VERSION, ORGANISATION)
        val filesPath = Path(filesDir)
        with(SystemFileSystem) { if (!exists(filesPath)) createDirectories(filesPath) }
        storeOf(Path("$filesDir/user.json"), null)
    }
    single<KStore<List<Note>>>(named(Named.NOTES_STORE)) {
        val filesDir = AppDirsFactory
            .getInstance()
            .getUserDataDir(PACKAGE_NAME, VERSION, ORGANISATION)
        val filesPath = Path(filesDir)
        with(SystemFileSystem) { if (!exists(filesPath)) createDirectories(filesPath) }
        storeOf(Path("$filesDir/notes.json"), null)
    }
    single(named(Named.SERVER_URL)) { "http://0.0.0.0:$SERVER_PORT" }
}