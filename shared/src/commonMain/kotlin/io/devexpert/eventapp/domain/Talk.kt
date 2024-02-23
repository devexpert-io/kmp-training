package io.devexpert.eventapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class Talk(
    val id: Int,
    val title: String,
    val description: String,
    val time: String,
    val speaker: Speaker
)