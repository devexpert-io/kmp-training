package io.devexpert.kmptraining.plugins

import io.devexpert.kmptraining.data.Notes.id
import io.devexpert.kmptraining.data.dao
import io.devexpert.kmptraining.domain.Note
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

                val addedNote = dao.addNote(note)
                call.respond(HttpStatusCode.Created, addedNote)
            }

            get {
                val notes = dao.getAllNotes()
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

                val success = dao.updateNote(updatedNote)
                if (success) {
                    call.respond(HttpStatusCode.OK, updatedNote)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            route("/{id}") {
                get {
                    val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(
                        HttpStatusCode.BadRequest
                    )
                    val note = dao.getNoteById(id)
                        ?: return@get call.respond(HttpStatusCode.NotFound)
                    call.respond(note)
                }

                put {
                    val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(
                        HttpStatusCode.BadRequest
                    )
                    val updatedNote = try {
                        call.receive<Note>()
                    } catch (e: Exception) {
                        return@put call.respond(
                            HttpStatusCode.BadRequest,
                            "Invalid request format"
                        )
                    }

                    val success = dao.updateNote(updatedNote.copy(id = id))
                    if (success) {
                        call.respond(HttpStatusCode.OK, updatedNote.copy(id = id))
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }

                delete {
                    val id = call.parameters["id"]?.toIntOrNull()
                        ?: return@delete call.respond(HttpStatusCode.BadRequest)

                    val removed = dao.deleteNoteById(id)
                    call.respond(if (removed) HttpStatusCode.NoContent else HttpStatusCode.NotFound)
                }
            }
        }
    }
}