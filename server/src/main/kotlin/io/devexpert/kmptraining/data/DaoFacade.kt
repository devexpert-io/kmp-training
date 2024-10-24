package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.domain.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

interface DaoFacade {
    suspend fun getAllNotes(userId: String): List<Note>
    suspend fun getNoteById(id: Int): Note?
    suspend fun addNote(note: Note, userId: String): Note
    suspend fun updateNote(note: Note): Boolean
    suspend fun deleteNoteById(id: Int): Boolean
    suspend fun getUserByGoogleId(googleId: String): User?
    suspend fun upsertUser(user: User): User
}

val dao = DaoFacadeImpl()

class DaoFacadeImpl : DaoFacade {
    override suspend fun getAllNotes(userId: String): List<Note> = dbQuery {
        Notes.selectAll().where { Notes.userId eq userId }.map(::resultRowToNote)
    }

    override suspend fun getNoteById(id: Int): Note? = dbQuery {
        Notes.selectAll().where { (Notes.id eq id) }
            .map(::resultRowToNote)
            .singleOrNull()
    }

    override suspend fun addNote(note: Note, userId: String): Note = dbQuery {
        val insertStatement = Notes.insert {
            it[this.userId] = userId
            it[title] = note.title
            it[content] = note.content
        }
        val resultRow = insertStatement.resultedValues?.first()
        resultRow?.let(::resultRowToNote) ?: throw IllegalStateException("Insert failed")
    }

    override suspend fun updateNote(note: Note): Boolean = dbQuery {
        Notes.update({ (Notes.id eq note.id) }) {
            it[title] = note.title
            it[content] = note.content
        } > 0
    }

    override suspend fun deleteNoteById(id: Int): Boolean = dbQuery {
        Notes.deleteWhere { (Notes.id eq id) } > 0
    }

    override suspend fun getUserByGoogleId(googleId: String): User? = dbQuery {
        Users.selectAll().where { Users.googleId eq googleId }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun upsertUser(user: User): User = dbQuery {
        val existingUser = Users.selectAll().where { Users.googleId eq user.googleId }.singleOrNull()
        if (existingUser != null) {
            Users.update({ Users.googleId eq user.googleId }) {
                it[email] = user.email
                it[name] = user.name
                it[pictureUrl] = user.pictureUrl
            }
            user.copy(id = existingUser[Users.id].value)
        } else {
            val insertStatement = Users.insert {
                it[googleId] = user.googleId
                it[email] = user.email
                it[name] = user.name
                it[pictureUrl] = user.pictureUrl
            }
            val resultRow = insertStatement.resultedValues?.first()
            resultRow?.let(::resultRowToUser) ?: throw IllegalStateException("Insert failed")
        }
    }

    private fun resultRowToNote(row: ResultRow) = Note(
        id = row[Notes.id].value,
        title = row[Notes.title],
        content = row[Notes.content]
    )

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id].value,
        googleId = row[Users.googleId],
        email = row[Users.email],
        name = row[Users.name],
        pictureUrl = row[Users.pictureUrl]
    )
}
