package io.devexpert.kmptraining.domain

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int,
    val title: String,
    val content: String
) {
    companion object {
        val Empty = Note(
            id = -1,
            title = "",
            content = ""
        )
    }
}