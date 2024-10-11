package io.devexpert.kmptraining.domain

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int,
    val title: String,
    val content: String
)

val mockedNotes = listOf(
    Note(
        id = 1,
        title = "Kotlin Basics",
        content = "Learn the basics of Kotlin, including syntax, data types, and control structures."
    ),
    Note(
        id = 2,
        title = "Advanced Kotlin",
        content = "Dive deeper into Kotlin with advanced topics such as coroutines, DSLs, and type-safe builders."
    ),
    Note(
        id = 3,
        title = "Ktor Framework",
        content = "Explore the Ktor framework for building asynchronous servers and clients in Kotlin."
    ),
    Note(
        id = 4,
        title = "Jetpack Compose",
        content = "Discover Jetpack Compose, the modern Android UI toolkit for building native apps in Kotlin."
    ),
    Note(
        id = 5,
        title = "Multiplatform Development",
        content = "Learn how to share code between Android, iOS, and the web using Kotlin Multiplatform."
    ),
    Note(
        id = 6,
        title = "Kotlin/JS",
        content = "Build interactive web applications with Kotlin/JS, a lightweight, cross-platform programming language."
    )
)