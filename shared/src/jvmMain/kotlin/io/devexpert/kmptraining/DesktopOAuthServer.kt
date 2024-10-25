package io.devexpert.kmptraining

import io.devexpert.kmptraining.data.OAuthServer
import io.ktor.http.HttpStatusCode
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class DesktopOAuthServer : OAuthServer {
    private var server:
            EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>? = null
    private val _authCode = Channel<String>()
    override val authCode: Flow<String> = _authCode.receiveAsFlow()

    override fun start() {
        server = embeddedServer(Netty, port = 3000) {
            routing {
                get("/callback") {
                    val token = call.parameters["token"]
                    if (token != null) {
                        _authCode.send(token)
                        call.respondText("Authentication successful. You can close this window.")
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Code not received")
                    }
                }
            }
        }.start()
    }

    override fun stop() {
        server?.stop(1000, 2000)
        _authCode.close()
    }
}