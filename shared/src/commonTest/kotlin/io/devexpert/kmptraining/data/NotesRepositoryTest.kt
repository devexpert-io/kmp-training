package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NotesRepositoryTest {

    private val localNotes = mutableListOf(
        Note(1, "Local Note 1", "Content 1"),
        Note(2, "Local Note 2", "Content 2")
    )

    private val remoteNotes = mutableListOf(
        Note(3, "Remote Note 3", "Content 3"),
        Note(4, "Remote Note 4", "Content 4")
    )

    private fun buildRepository(
        localNotes: MutableList<Note> = mutableListOf(),
        remoteNotes: MutableList<Note> = mutableListOf()
    ): NotesRepository {
        val localDataSource = FakeNotesLocalDataSource(localNotes)
        val remoteDataSource = FakeNotesRemoteDataSource(remoteNotes)
        return NotesRepository(localDataSource, remoteDataSource)
    }

    @Test
    fun testGetNotesWhenLocalIsEmpty() = runBlocking {
        val repository = buildRepository(remoteNotes = remoteNotes)

        val result = repository.notes.first()

        assertEquals(remoteNotes, result)
    }

    @Test
    fun testGetNotesWhenLocalIsNotEmpty() = runBlocking {
        val repository = buildRepository(localNotes, remoteNotes)

        val result = repository.notes.first()

        assertEquals(localNotes, result)
    }

    @Test
    fun testGetNote() = runBlocking {
        val repository = buildRepository(localNotes, remoteNotes)
        val result = repository.getNote(1).first()

        assertEquals(localNotes[0], result)
    }

    @Test
    fun testCloneNote() = runBlocking {
        val originalNote = Note(1, "Original Note", "Original Content")
        val repository = buildRepository(localNotes = mutableListOf(originalNote))

        repository.cloneNote(originalNote)

        val clonedNote = repository.notes.first()[1]
        assertEquals("Copy of Original Note", clonedNote.title)
        assertEquals("Original Content", clonedNote.content)
    }

    @Test
    fun testDeleteNote() = runBlocking {
        val note = Note(1, "Note to Delete", "Delete this content")
        val repository = buildRepository(
            localNotes = mutableListOf(note),
            remoteNotes = mutableListOf(note)
        )

        repository.deleteNote(note)

        assertTrue(repository.notes.first().isEmpty())
    }

    @Test
    fun testSaveNewNote() = runBlocking {
        val repository = buildRepository()

        val newNote = Note(0, "New Note", "New Content")
        repository.saveNote(newNote)

        val notes = repository.notes.first()

        assertEquals(newNote.copy(id = 1), notes[0])
    }

    @Test
    fun testUpdateExistingNote() = runBlocking {
        val existingNote = Note(1, "Existing Note", "Existing Content")
        val repository = buildRepository(
            localNotes = mutableListOf(existingNote),
            remoteNotes = mutableListOf(existingNote)
        )

        val updatedNote = existingNote.copy(title = "Updated Note", content = "Updated Content")
        repository.saveNote(updatedNote)

        val notes = repository.notes.first()

        assertEquals(updatedNote, notes[0])
    }
}

class FakeNotesLocalDataSource(
    private var notes: MutableList<Note> = mutableListOf()
) : NotesLocalDataSource {

    override fun getAllNotes(): Flow<List<Note>> = flowOf(notes)

    override fun getNoteById(id: Int): Flow<Note?> = flowOf(notes.find { it.id == id })

    override fun insertNote(note: Note) {
        notes.add(note.copy(id = notes.size + 1))
    }

    override fun updateNote(note: Note) {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            notes[index] = note
        }
    }

    override fun deleteNote(note: Note) {
        notes.removeAll { it.id == note.id }
    }
}

class FakeNotesRemoteDataSource(
    private var notes: MutableList<Note> = mutableListOf()
) : NotesRemoteDataSource {

    override suspend fun getNotes(): List<Note> {
        return notes
    }

    override suspend fun saveNote(note: Note): Note {
        val savedNote = if (note.id <= 0) {
            note.copy(id = notes.size + 1)
        } else {
            note
        }
        val index = notes.indexOfFirst { it.id == savedNote.id }
        if (index != -1) {
            notes[index] = savedNote
        } else {
            notes.add(savedNote)
        }
        return savedNote
    }

    override suspend fun deleteNote(note: Note) {
        notes.removeAll { it.id == note.id }
    }
}
