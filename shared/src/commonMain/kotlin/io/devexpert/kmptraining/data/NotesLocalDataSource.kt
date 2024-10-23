package io.devexpert.kmptraining.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.devexpert.kmptraining.domain.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesLocalDataSource(
    private val queries: NotesQueries,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    fun getAllNotes(): Flow<List<Note>> = queries.selectAll().asFlow().mapToList(dispatcher).map { notes -> notes.map { it.toNote() } }

    fun getNoteById(id: Int): Flow<Note?> = queries.selectById(id.toLong()).asFlow().mapToList(dispatcher).map { it.firstOrNull()?.toNote() }

    fun insertNote(note: Note): Unit = queries.insert(note.id.toLong(), note.title, note.content)

    fun deleteAllNotes(): Unit = queries.deleteAll()
}

private fun Notes.toNote() = Note(
    id = id.toInt(),
    title = title,
    content = content
)