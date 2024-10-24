package io.devexpert.kmptraining.data

import kotlinx.serialization.Serializable

@Serializable
internal data class GoogleUserInfo(
    val id: String,
    val email: String,
    val name: String,
    val picture: String
)