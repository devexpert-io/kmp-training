package io.devexpert.eventapp.domain

import kotlinx.serialization.Serializable

@Serializable
enum class SocialNetwork {
    LINKEDIN,
    INSTAGRAM,
    GITHUB,
    X,
    BLOG
}