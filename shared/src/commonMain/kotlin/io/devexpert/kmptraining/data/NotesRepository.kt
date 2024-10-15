package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.serverUrl
import io.ktor.client.call.body
import io.ktor.client.request.get

class NotesRepository {

    suspend fun getNotes(): List<Note> =
        RemoteClient.instance.get("$serverUrl/notes").body()
}