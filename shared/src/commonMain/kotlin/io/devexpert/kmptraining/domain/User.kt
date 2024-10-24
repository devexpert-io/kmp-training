package io.devexpert.kmptraining.domain

data class User(
    val id: Int = 0,
    val googleId: String,
    val email: String,
    val name: String,
    val pictureUrl: String
)