package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.UserInfo
import io.ktor.http.HttpStatusCode
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class OAuthServer(
    applicationEngineFactory: ApplicationEngineFactory<*, *>
) {
    private val _userInfo = Channel<UserInfo>()
    val userInfo: Flow<UserInfo> = _userInfo.receiveAsFlow()

    private val server = embeddedServer(
        factory = applicationEngineFactory,
        host = "0.0.0.0",
        port = 3000
    ) {
        routing {
            get("/callback") {
                val token = call.parameters["token"]
                val name = call.parameters["name"]
                val picture = call.parameters["picture"]
                
                if (token != null && name != null && picture != null) {
                    _userInfo.send(UserInfo(token, name, picture))
                    call.respondText("Authentication successful. You can close this window.")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Required parameters not received")
                }
                stop()
            }
        }
    }

    fun start() {
        server.start()
    }

    fun stop() {
        server.stop(1000, 2000)
        _userInfo.close()
    }
}
