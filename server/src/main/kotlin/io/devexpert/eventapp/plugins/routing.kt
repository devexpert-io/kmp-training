package io.devexpert.eventapp.plugins

import io.devexpert.eventapp.data.dao
import io.devexpert.eventapp.domain.Talk
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
        route("/talks") {

            post {
                val talk = call.receive<Talk>()
                dao.createTalk(talk)
                call.respond(HttpStatusCode.Created)
            }

            get {
                call.respond(dao.getAllTalks())
            }

            put {
                val updatedTalk = call.receive<Talk>()
                if (dao.updateTalk(updatedTalk)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val isRemoved = dao.deleteTalk(id ?: 0)

                if (isRemoved) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}