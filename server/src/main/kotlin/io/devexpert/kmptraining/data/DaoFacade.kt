package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.Note
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface DaoFacade {
    suspend fun getAllNotes(): List<Note>
    suspend fun getNoteById(id: Int): Note?
    suspend fun addNote(note: Note): Note
    suspend fun updateNote(note: Note): Boolean
    suspend fun deleteNoteById(id: Int): Boolean
}

val dao = DaoFacadeImpl()

class DaoFacadeImpl : DaoFacade {
    override suspend fun getAllNotes(): List<Note> = dbQuery {
        Notes.selectAll().map(::noteFromResultRow)
    }

    override suspend fun getNoteById(id: Int): Note? = dbQuery {
        Notes.selectAll()
            .where { Notes.id eq id }
            .map(::noteFromResultRow)
            .singleOrNull()
    }

    override suspend fun addNote(note: Note): Note = dbQuery {
        val id = Notes.insert {
            it[title] = note.title
            it[content] = note.content
        } get Notes.id
        note.copy(id = id.value)
    }

    override suspend fun updateNote(note: Note): Boolean = dbQuery {
        Notes.update({ Notes.id eq note.id }) {
            it[title] = note.title
            it[content] = note.content
        } > 0
    }

    override suspend fun deleteNoteById(id: Int): Boolean = dbQuery {
        Notes.deleteWhere { Notes.id eq id } > 0
    }

    private fun noteFromResultRow(row: ResultRow) = Note(
        id = row[Notes.id].value,
        title = row[Notes.title],
        content = row[Notes.content]
    )
}