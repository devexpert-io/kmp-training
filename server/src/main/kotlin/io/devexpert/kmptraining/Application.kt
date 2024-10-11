package io.devexpert.kmptraining

import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.mockedNotes
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
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

private val notes = mockedNotes.toMutableList()

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json { prettyPrint = true })
    }

    routing {

        // Create
        post("/notes") {
            val note = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request format")
                return@post
            }

            notes.add(note)
            call.respond(HttpStatusCode.Created)
        }

        // Read
        get("/notes") {
            call.respond(notes)
        }

        // Read by id
        get("/notes/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest)
            val note =
                notes.find { it.id == id } ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(note)
        }

        // Update
        put("/notes") {
            val updatedNote = try {
                call.receive<Note>()
            } catch (e: Exception) {
                return@put call.respond(HttpStatusCode.BadRequest, "Invalid request format")
            }

            val index = notes.indexOfFirst { it.id == updatedNote.id }
            if (index != -1) {
                notes[index] = updatedNote
                call.respond(HttpStatusCode.OK, notes[index])
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Delete
        delete("/notes/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest)

            val removed = notes.removeIf { it.id == id }
            call.respond(if (removed) HttpStatusCode.NoContent else HttpStatusCode.NotFound)
        }
    }
}