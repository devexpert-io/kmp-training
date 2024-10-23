package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.Note
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class NotesRemoteDataSource(
    private val httpClient: HttpClient,
    serverUrl: String
) {
    private val baseUrl = "$serverUrl/notes"

    suspend fun getNotes(): List<Note> =
        httpClient.get(baseUrl).body()

    suspend fun deleteNote(note: Note) {
        httpClient.delete("$baseUrl/${note.id}")
    }

    suspend fun saveNote(note: Note): Note = if (note.id <= 0) {
        httpClient.post(baseUrl) {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body<Note>()
    } else {
        httpClient.put("$baseUrl/${note.id}") {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body<Note>()
    }
}