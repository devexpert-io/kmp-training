package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.Note
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface NotesLocalDataSource {
    fun getAllNotes(): Flow<List<Note>>
    fun getNoteById(id: Int): Flow<Note?>
    suspend fun insertNote(note: Note)
    suspend fun deleteAllNotes()
}

class NotesLocalDataSourceImpl(private val store: KStore<List<Note>>) : NotesLocalDataSource {
    override fun getAllNotes(): Flow<List<Note>> =
        store.updates.map { it ?: emptyList() }

    override fun getNoteById(id: Int): Flow<Note?> =
        store.updates.map { notes -> notes?.find { it.id == id } }

    override suspend fun insertNote(note: Note) {
        val currentNotes = store.get() ?: emptyList()
        store.set(currentNotes + note)
    }

    override suspend fun deleteAllNotes() {
        store.set(emptyList())
    }
}
