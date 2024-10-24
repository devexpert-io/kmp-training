package io.devexpert.kmptraining.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.oauth

private val applicationHttpClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

fun Application.configureAuthentication() {
    // Generated using: openssl rand -base64 64
    val jwtSecret = System.getenv("JWT_SECRET") ?: throw IllegalStateException("JWT_SECRET must be set")

    val googleClientId = System.getenv("GOOGLE_CLIENT_ID")
        ?: throw IllegalStateException("GOOGLE_CLIENT_ID must be set")
    val googleClientSecret = System.getenv("GOOGLE_CLIENT_SECRET")
        ?: throw IllegalStateException("GOOGLE_CLIENT_SECRET must be set")

    install(Authentication) {
        oauth("auth-oauth-google") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = googleClientId,
                    clientSecret = googleClientSecret,
                    defaultScopes = listOf(
                        "https://www.googleapis.com/auth/userinfo.profile",
                        "https://www.googleapis.com/auth/userinfo.email"
                    ),
                    extraAuthParameters = listOf("access_type" to "offline")
                )
            }
            client = applicationHttpClient
        }

        jwt("auth-jwt") {
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret)).build()
            )
            validate { credential ->
                JWTPrincipal(credential.payload)
            }
        }
    }
}

fun generateJwtToken(userId: String): String {
    val jwtSecret = System.getenv("JWT_SECRET")
        ?: throw IllegalStateException("JWT_SECRET must be set")

    return JWT.create()
        .withClaim("userId", userId)
        //.withExpiresAt(Date(System.currentTimeMillis() + 24*60*60*1000)) // 24 hours
        .sign(Algorithm.HMAC256(jwtSecret))
}