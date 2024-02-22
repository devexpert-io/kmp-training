package io.devexpert.eventapp

import SERVER_PORT
import io.devexpert.eventapp.domain.Talk
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

private val talks = mutableListOf<Talk>()

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json { prettyPrint = true })
    }

    routing {
        route("/talks") {
            // Create
            post {
                val talk = call.receive<Talk>()
                talks.add(talk)
                call.respond(HttpStatusCode.Created)
            }

            // Read
            get {
                call.respond(talks)
            }

            // Update
            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val updatedTalk = call.receive<Talk>()
                val existingTalk = talks.firstOrNull { it.id == id }

                existingTalk?.let {
                    talks[talks.indexOf(it)] = updatedTalk
                    call.respond(HttpStatusCode.OK)
                } ?: call.respond(HttpStatusCode.NotFound)
            }

            // Delete
            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val isRemoved = talks.removeIf { it.id == id }

                if (isRemoved) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}