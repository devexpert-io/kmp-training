package io.devexpert.kmptraining.plugins

import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.mockedNotes
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

private val notes = mockedNotes.toMutableList()

fun Application.configureRouting() {
    routing {
        route("/notes") {
            post {
                val note = try {
                    call.receive<Note>()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request format")
                    return@post
                }

                notes.add(note)
                call.respond(HttpStatusCode.Created)
            }

            get {
                call.respond(notes)
            }

            put {
                val updatedNote = try {
                    call.receive<Note>()
                } catch (e: Exception) {
                    return@put call.respond(
                        HttpStatusCode.BadRequest,
                        "Invalid request format"
                    )
                }

                val index = notes.indexOfFirst { it.id == updatedNote.id }
                if (index != -1) {
                    notes[index] = updatedNote
                    call.respond(HttpStatusCode.OK, notes[index])
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            route("/{id}") {
                get {
                    val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                        HttpStatusCode.BadRequest
                    )
                    val note = notes.find { it.id == id }
                        ?: return@get call.respond(HttpStatusCode.NotFound)
                    call.respond(note)
                }

                delete {
                    val id = call.parameters["id"]?.toIntOrNull()
                        ?: return@delete call.respond(HttpStatusCode.BadRequest)

                    val removed = notes.removeIf { it.id == id }
                    call.respond(if (removed) HttpStatusCode.NoContent else HttpStatusCode.NotFound)
                }
            }
        }
    }
}