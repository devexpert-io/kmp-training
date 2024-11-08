package io.devexpert.kmptraining.plugins

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.devexpert.kmptraining.BuildConfig
import io.devexpert.kmptraining.data.GoogleUserInfo
import io.devexpert.kmptraining.data.dao
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import java.util.Collections

fun Application.configureRouting() {
    routing {
        post("/login") {
            try {
                val authorization = call.request.headers["Authorization"]
                    ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        "No authorization header"
                    )

                call.respond(findUserInfo(authorization))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        route("/notes") {
            get {
                call.withValidUser { userId ->
                    val notes = dao.getAllNotes(userId)
                    call.respond(notes)
                }
            }

            post {
                call.withValidUser { userId ->
                    val note = call.receive<Note>()
                    val addedNote = dao.addNote(note, userId)
                    call.respond(HttpStatusCode.Created, addedNote)
                }
            }

            put {
                call.withValidUser {
                    val note = call.receive<Note>()
                    val success = dao.updateNote(note)
                    if (success) {
                        call.respond(HttpStatusCode.OK, note)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Note not found or update failed")
                    }
                }
            }

            route("/{id}") {
                get {
                    call.withValidUser {
                        val id = call.getIdParameter() ?: return@get
                        val note = dao.getNoteById(id)
                        if (note != null) {
                            call.respond(note)
                        } else {
                            call.respond(HttpStatusCode.NotFound, "Note not found")
                        }
                    }
                }

                delete {
                    call.withValidUser {
                        val id = call.getIdParameter() ?: return@delete
                        val success = dao.deleteNoteById(id)
                        if (success) {
                            call.respond(HttpStatusCode.NoContent)
                        } else {
                            call.respond(
                                HttpStatusCode.NotFound,
                                "Note not found or delete failed"
                            )
                        }
                    }
                }
            }
        }

    }
}

private val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
    .setAudience(Collections.singletonList(BuildConfig.GOOGLE_SERVER_ID))
    .build()

private suspend fun findUserInfo(idToken: String): User {
    val googleIdToken = verifier.verify(idToken) ?: throw IllegalStateException("Invalid token")

    val payload = googleIdToken.payload
    val googleId = payload.subject

    val savedUserInfo = dao.getUserByGoogleId(googleId) ?: GoogleUserInfo(
        googleId = googleId,
        email = payload.email,
        name = payload["name"] as? String ?: "",
        picture = payload["picture"] as? String ?: ""
    )

    val updatedUser = dao.upsertUser(savedUserInfo)
    val token = generateJwtToken(updatedUser.id)

    return User(
        id = updatedUser.id,
        token = token,
        email = updatedUser.email,
        name = updatedUser.name,
        pictureUrl = updatedUser.picture
    )
}

private suspend inline fun ApplicationCall.withValidUser(block: (Int) -> Unit) {
    block(1)
}

private suspend fun ApplicationCall.getIdParameter(): Int? {
    val id = parameters["id"]?.toIntOrNull()
    if (id == null) {
        respond(HttpStatusCode.BadRequest, "Invalid ID")
    }
    return id
}
