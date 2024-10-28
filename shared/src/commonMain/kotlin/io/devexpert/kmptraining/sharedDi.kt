package io.devexpert.kmptraining

import io.devexpert.kmptraining.data.AuthRepository
import io.devexpert.kmptraining.data.KStoreUserInfoStorage
import io.devexpert.kmptraining.data.NotesLocalDataSource
import io.devexpert.kmptraining.data.NotesRemoteDataSource
import io.devexpert.kmptraining.data.NotesRepository
import io.devexpert.kmptraining.data.OAuthServer
import io.devexpert.kmptraining.data.UserInfoStorage
import io.devexpert.kmptraining.sqldelight.Database
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.plugin
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngineFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.bind
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
    single { buildHttpClient() }
    single { AuthRepository(get(named(Named.SERVER_URL)), get(), get()) }
    singleOf(::KStoreUserInfoStorage) bind UserInfoStorage::class
    singleOf(::OAuthServer)
    single<ApplicationEngineFactory<*, *>> { CIO }
}

private fun Scope.buildHttpClient(): HttpClient {
    val userInfoStorage: UserInfoStorage = get()
    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }
    }.also {
        it.plugin(HttpSend).intercept { request ->
            val token = userInfoStorage.getUserInfo()?.token
            if (token != null) {
                request.headers["Authorization"] = "Bearer $token"
            }
            execute(request)
        }
    }
}
