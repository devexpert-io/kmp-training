package io.devexpert.kmptraining

import io.devexpert.kmptraining.data.NotesLocalDataSource
import io.devexpert.kmptraining.data.NotesRemoteDataSource
import io.devexpert.kmptraining.data.NotesRepository
import io.devexpert.kmptraining.sqldelight.Database
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect val nativeModule: Module

internal enum class Named {
    SERVER_URL
}

val sharedModule: Module = module {
    single { NotesRemoteDataSource(get(), get(named(Named.SERVER_URL))) }
    singleOf(::NotesLocalDataSource)
    singleOf(::NotesRepository)
    single<CoroutineDispatcher> { Dispatchers.IO }
    single { Database(get()).notesQueries }
    single {
        HttpClient {
            install(ContentNegotiation){
                json(Json {
                    prettyPrint = true
                })
            }
        }
    }
}