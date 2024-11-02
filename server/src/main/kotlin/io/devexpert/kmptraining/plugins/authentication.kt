package io.devexpert.kmptraining.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.devexpert.kmptraining.BuildConfig
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.configureAuthentication() {

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT.require(Algorithm.HMAC256(BuildConfig.JWT_SECRET)).build()
            )
            validate { credential ->
                JWTPrincipal(credential.payload)
            }
        }
    }
}

fun generateJwtToken(userId: Int): String =
    JWT.create()
        .withClaim("userId", userId)
        //.withExpiresAt(Date(System.currentTimeMillis() + 24*60*60*1000)) // 24 hours
        .sign(Algorithm.HMAC256(BuildConfig.JWT_SECRET))