package io.devexpert.kmptraining

import io.devexpert.kmptraining.data.AuthRemoteDataSource
import io.devexpert.kmptraining.data.AuthRemoteDataSourceImpl
import io.devexpert.kmptraining.data.KStoreUserStorage
import io.devexpert.kmptraining.data.NotesLocalDataSource
import io.devexpert.kmptraining.data.NotesLocalDataSourceImpl
import io.devexpert.kmptraining.data.NotesRemoteDataSource
import io.devexpert.kmptraining.data.NotesRemoteDataSourceImpl
import io.devexpert.kmptraining.data.NotesRepository
import io.devexpert.kmptraining.data.UserStorage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect val nativeModule: Module

internal enum class Named {
    SERVER_URL, NOTES_STORE, USER_STORE
}

val sharedModule: Module = module {
    single<NotesRemoteDataSource> { NotesRemoteDataSourceImpl(get(), get(named(Named.SERVER_URL))) }
    single<NotesLocalDataSource> { NotesLocalDataSourceImpl(get(named(Named.NOTES_STORE))) }
    singleOf(::NotesRepository)
    single { buildHttpClient() }
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get(named(Named.SERVER_URL)), get()) }
    single<UserStorage> { KStoreUserStorage(get(named(Named.USER_STORE))) }
}

private fun buildHttpClient(): HttpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { prettyPrint = true })
    }
}
