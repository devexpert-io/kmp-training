package io.devexpert.kmptraining.plugins

import io.devexpert.kmptraining.data.GoogleUserInfo
import io.devexpert.kmptraining.data.dao
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

private val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond("Welcome to the Note Taking API")
        }

        authenticate("auth-oauth-google") {
            get("/login") {
                // Redirects to Google's login page
            }

            get("/callback") {
                call.handleOAuthCallback(httpClient)
            }
        }

        authenticate("auth-jwt") {
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
}

private suspend fun ApplicationCall.handleOAuthCallback(httpClient: HttpClient) {
    val principal: OAuthAccessTokenResponse.OAuth2? = principal()
    principal?.let { oauthResponse ->

        val response = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
            header("Authorization", "Bearer ${oauthResponse.accessToken}")
        }

        val userInfo: GoogleUserInfo = response.body()

        val user: User = dao.getUserByGoogleId(userInfo.id) ?: User(
            googleId = userInfo.id,
            email = userInfo.email,
            name = userInfo.name,
            pictureUrl = userInfo.picture
        )
        val updatedUser = dao.upsertUser(user)

        val token = generateJwtToken(updatedUser.id.toString())

        respondRedirect("http://localhost:3000/callback?token=$token")

    } ?: respond(HttpStatusCode.Unauthorized, "OAuth authentication failed")
}

private suspend inline fun ApplicationCall.withValidUser(block: (String) -> Unit) {
    val userId = principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString()
    if (userId != null) {
        block(userId)
    } else {
        respond(HttpStatusCode.Unauthorized)
    }
}

private suspend fun ApplicationCall.getIdParameter(): Int? {
    val id = parameters["id"]?.toIntOrNull()
    if (id == null) {
        respond(HttpStatusCode.BadRequest, "Invalid ID")
    }
    return id
}
