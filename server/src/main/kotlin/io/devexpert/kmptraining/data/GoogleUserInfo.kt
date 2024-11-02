package io.devexpert.kmptraining.data

import kotlinx.serialization.Serializable

@Serializable
data class GoogleUserInfo(
    val id: Int = 0,
    val googleId: String,
    val email: String,
    val name: String,
    val picture: String
)