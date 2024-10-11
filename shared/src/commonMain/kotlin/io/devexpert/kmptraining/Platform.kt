package io.devexpert.kmptraining

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform