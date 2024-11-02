package io.devexpert.kmptraining.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int = 0,
    val token: String,
    val email: String,
    val name: String,
    val pictureUrl: String
)