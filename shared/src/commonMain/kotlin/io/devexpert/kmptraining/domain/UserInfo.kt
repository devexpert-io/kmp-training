package io.devexpert.kmptraining.domain

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val token: String,
    val name: String,
    val picture: String
)