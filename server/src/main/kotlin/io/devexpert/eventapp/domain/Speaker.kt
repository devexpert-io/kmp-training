package io.devexpert.eventapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class Speaker(
    val id: Int,
    val name: String,
    val bio: String,
    val socialLinks: Map<SocialNetwork, String?> = emptyMap()
)