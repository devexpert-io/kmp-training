package io.devexpert.kmptraining.data

import kotlinx.coroutines.flow.Flow

interface OAuthServer {
    val authCode: Flow<String>
    fun start()
    fun stop()
}