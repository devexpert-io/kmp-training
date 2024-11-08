package io.devexpert.kmptraining

import io.devexpert.kmptraining.data.DatabaseSingleton
import io.devexpert.kmptraining.plugins.configureCORS
import io.devexpert.kmptraining.plugins.configureRouting
import io.devexpert.kmptraining.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(
        factory = Netty,
        port = SERVER_PORT,
        host = "0.0.0.0",
        module = Application::module
    )
        .start(wait = true)
}

fun Application.module() {
    DatabaseSingleton.init()
    configureSerialization()
    configureRouting()
    configureCORS()
}