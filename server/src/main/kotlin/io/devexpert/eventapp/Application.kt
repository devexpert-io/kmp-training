package io.devexpert.eventapp

import io.devexpert.eventapp.data.DatabaseSingleton
import io.devexpert.eventapp.plugins.configureRouting
import io.devexpert.eventapp.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseSingleton.init()
    configureSerialization()
    configureRouting()
}