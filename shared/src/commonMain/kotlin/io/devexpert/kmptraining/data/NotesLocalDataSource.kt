package io.devexpert.kmptraining.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.sqldelight.Notes
import io.devexpert.kmptraining.sqldelight.NotesQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface NotesLocalDataSource {
    fun getAllNotes(): Flow<List<Note>>
    fun getNoteById(id: Int): Flow<Note?>
    fun insertNote(note: Note)
    fun deleteAllNotes()
}

class NotesLocalDataSourceImpl(
    private val queries: NotesQueries,
    private val dispatcher: CoroutineDispatcher,
) : NotesLocalDataSource {
    override fun getAllNotes(): Flow<List<Note>> = queries.selectAll().asFlow().mapToList(dispatcher)
        .map { notes -> notes.map { it.toNote() } }

    override fun getNoteById(id: Int): Flow<Note?> =
        queries.selectById(id.toLong()).asFlow().mapToList(dispatcher)
            .map { it.firstOrNull()?.toNote() }

    override fun insertNote(note: Note): Unit = queries.insert(note.id.toLong(), note.title, note.content)

    override fun deleteAllNotes(): Unit = queries.deleteAll()
}

private fun Notes.toNote() = Note(
    id = id.toInt(),
    title = title,
    content = content
)
