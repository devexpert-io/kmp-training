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

interface NotesRemoteDataSource {
    suspend fun getNotes(): List<Note>
    suspend fun deleteNote(note: Note)
    suspend fun saveNote(note: Note): Note
}

class NotesRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    serverUrl: String
) : NotesRemoteDataSource {
    private val baseUrl = "$serverUrl/notes"

    override suspend fun getNotes(): List<Note> =
        httpClient.get(baseUrl).body()

    override suspend fun deleteNote(note: Note) {
        httpClient.delete("$baseUrl/${note.id}")
    }

    override suspend fun saveNote(note: Note): Note = if (note.id <= 0) {
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