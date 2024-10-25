package io.devexpert.kmptraining.data

import io.devexpert.kmptraining.domain.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
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
        val repository = buildRepository(
            localNotes = mutableListOf(originalNote),
            remoteNotes = mutableListOf(originalNote)
        )

        repository.cloneNote(originalNote)

        val notes = repository.notes.first()
        val clonedNote = notes[1]
        assertEquals("Copy of Original Note", clonedNote.title)
        assertEquals("Original Content", clonedNote.content)
        assertEquals(2, notes.size)
    }

    @Test
    fun testDeleteNote() = runBlocking {
        val note = Note(1, "Note to Delete", "Delete this content")
        val repository = buildRepository(
            localNotes = mutableListOf(note),
            remoteNotes = mutableListOf(note)
        )

        repository.deleteNote(note)
        val notes = repository.notes.first()
        assertTrue(notes.isEmpty())
    }

    @Test
    fun testSaveNewNote() = runBlocking {
        val repository = buildRepository()
        val newNote = Note(0, "New Note", "New Content")
        
        repository.saveNote(newNote)
        val notes = repository.notes.first()

        assertEquals(1, notes.size)
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
        assertEquals(1, notes.size)
        assertEquals(updatedNote, notes[0])
    }
}

class FakeNotesLocalDataSource(
    initialNotes: MutableList<Note> = mutableListOf()
) : NotesLocalDataSource {
    private val _notes = MutableStateFlow(initialNotes.toList())
    
    override fun getAllNotes(): Flow<List<Note>> = _notes.asStateFlow()

    override fun getNoteById(id: Int): Flow<Note?> = _notes.map { notes ->
        notes.find { it.id == id }
    }

    override fun insertNote(note: Note) {
        _notes.update { currentNotes ->
            currentNotes + note
        }
    }

    override fun deleteAllNotes() {
        _notes.update { emptyList() }
    }
}

class FakeNotesRemoteDataSource(
    private var notes: MutableList<Note> = mutableListOf()
) : NotesRemoteDataSource {

    override suspend fun getNotes(): List<Note> = notes

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
