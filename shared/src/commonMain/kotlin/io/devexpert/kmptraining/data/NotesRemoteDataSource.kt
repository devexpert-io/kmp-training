package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.serverUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class NotesRemoteDataSource {
    companion object {
        private val BASE_URL = "$serverUrl/notes"
    }

    private val httpClient: HttpClient = RemoteClient.instance

    suspend fun getNotes(): List<Note> =
        httpClient.get(BASE_URL).body()

    suspend fun deleteNote(note: Note) {
        httpClient.delete("$BASE_URL/${note.id}")
    }

    suspend fun saveNote(note: Note): Note = if (note.id <= 0) {
        httpClient.post(BASE_URL) {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body<Note>()
    } else {
        httpClient.put("$BASE_URL/${note.id}") {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body<Note>()
    }
}