package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.serverUrl
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class NotesRepository {

    companion object {
        private val BASE_URL = "$serverUrl/notes"
    }

    suspend fun getNotes(): List<Note> =
        RemoteClient.instance.get(BASE_URL).body()

    suspend fun getNote(noteId: Int): Note =
        RemoteClient.instance.get("$BASE_URL/$noteId").body()

    suspend fun cloneNote(note: Note): Note {
        val clonedNote = note.copy(id = 0, title = "Copy of ${note.title}")

        return RemoteClient.instance.post(BASE_URL) {
            contentType(ContentType.Application.Json)
            setBody(clonedNote)
        }.body<Note>()
    }

    suspend fun deleteNote(note: Note) {
        RemoteClient.instance.delete("$BASE_URL/${note.id}")
    }

    suspend fun saveNote(note: Note): Note = if (note.id <= 0) {
        RemoteClient.instance.post(BASE_URL) {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body<Note>()
    } else {
        RemoteClient.instance.put("$BASE_URL/${note.id}") {
            contentType(ContentType.Application.Json)
            setBody(note)
        }.body<Note>()
    }
}