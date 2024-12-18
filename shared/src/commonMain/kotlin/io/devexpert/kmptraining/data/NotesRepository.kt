package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class NotesRepository(
    private val localDataSource: NotesLocalDataSource,
    private val remoteDataSource: NotesRemoteDataSource,
) {
    val notes: Flow<List<Note>> = localDataSource.getAllNotes().map { localNotes ->
        localNotes.ifEmpty { refreshCache() }
    }

    private suspend fun refreshCache(): List<Note> {
        val remoteNotes = remoteDataSource.getNotes()
        localDataSource.deleteAllNotes()
        remoteNotes.forEach(localDataSource::insertNote)
        return remoteNotes
    }

    fun getNote(noteId: Int): Flow<Note?> = localDataSource.getNoteById(noteId)

    suspend fun cloneNote(note: Note) {
        val clonedNote = note.copy(id = 0, title = "Copy of ${note.title}")
        saveNote(clonedNote)
    }

    suspend fun deleteNote(note: Note) {
        remoteDataSource.deleteNote(note)
        refreshCache()
    }

    suspend fun saveNote(note: Note): Note {
        val updatedNote = remoteDataSource.saveNote(note)
        refreshCache()
        return updatedNote
    }
}
