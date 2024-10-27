package io.devexpert.kmptraining.ui.screens.notes

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.ui.common.LOADING_INDICATOR_TAG
import io.devexpert.kmptraining.ui.domain.Action
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.clone
import kmptraining.composeapp.generated.resources.delete
import kmptraining.composeapp.generated.resources.more_actions
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class NotesTest {

    @Test
    fun testInitialState() = runComposeUiTest {
        setContent {
            Notes(
                state = NotesViewModel.UiState(),
                onAction = { _, _ -> },
                onNoteClick = {},
                onMessageRemoved = {}
            )
        }
        onNode(hasScrollToIndexAction()).onChildren().assertCountEquals(0)
    }

    @Test
    fun testLoadingState() = runComposeUiTest {
        setContent {
            Notes(
                state = NotesViewModel.UiState(isLoading = true),
                onAction = { _, _ -> },
                onNoteClick = {},
                onMessageRemoved = {}
            )
        }

        onNodeWithTag(LOADING_INDICATOR_TAG).assertIsDisplayed()
    }

    @Test
    fun testNotesListDisplay() = runComposeUiTest {
        val testNotes = listOf(
            Note(1, "Test Note 1", "Content 1"),
            Note(2, "Test Note 2", "Content 2")
        )

        setContent {
            Notes(
                state = NotesViewModel.UiState(notes = testNotes),
                onAction = { _, _ -> },
                onNoteClick = {},
                onMessageRemoved = {}
            )
        }

        onNodeWithText("Test Note 1").assertIsDisplayed()
        onNodeWithText("Test Note 2").assertIsDisplayed()
    }

    @Test
    fun testNoteClick() = runComposeUiTest {
        var clickedNote: Note? = null
        val testNotes = listOf(
            Note(1, "Test Note 1", "Content 1")
        )

        setContent {
            Notes(
                state = NotesViewModel.UiState(notes = testNotes),
                onAction = { _, _ -> },
                onNoteClick = { clickedNote = it },
                onMessageRemoved = {}
            )
        }

        onNodeWithText("Test Note 1").performClick()
        assertTrue(clickedNote?.id == 1)
    }

    @Test
    fun testAddNoteAction() = runComposeUiTest {
        var clickedNote: Note? = null
        setContent {
            Notes(
                state = NotesViewModel.UiState(),
                onAction = { _, _ -> },
                onNoteClick = { clickedNote = it },
                onMessageRemoved = {}
            )
        }

        onNodeWithContentDescription("Add note").performClick()
        assertEquals(Note.Empty, clickedNote)
    }

    @Test
    fun testErrorState() = runComposeUiTest {
        setContent {
            Notes(
                state = NotesViewModel.UiState(error = "Error loading notes"),
                onAction = { _, _ -> },
                onNoteClick = {},
                onMessageRemoved = {}
            )
        }

        onNodeWithText("Error loading notes").assertIsDisplayed()
    }

    @Test
    fun testCloneNoteFromMoreActionsMenu() = runComposeUiTest {
        val initialNote = Note(id = 1, title = "Test Note", content = "Test Content")
        val notes = mutableListOf(initialNote)
        var lastAction: Action? = null
        var lastActionedNote: Note? = null

        setContent {
            Notes(
                state = NotesViewModel.UiState(notes = notes),
                onAction = { a, n -> lastAction = a; lastActionedNote = n },
                onNoteClick = {},
                onMessageRemoved = {}
            )
        }

        runBlocking {
            onNodeWithContentDescription(getString(Res.string.more_actions)).performClick()
            onNodeWithText(getString(Res.string.clone)).performClick()

            assertEquals(Action.CLONE, lastAction)
            assertEquals(initialNote, lastActionedNote)
        }
    }

    @Test
    fun testDeleteNoteFromMoreActionsMenu() = runComposeUiTest {
        val initialNote = Note(id = 1, title = "Test Note", content = "Test Content")
        val notes = mutableListOf(initialNote)
        var lastAction: Action? = null
        var lastActionedNote: Note? = null

        setContent {
            Notes(
                state = NotesViewModel.UiState(notes = notes),
                onAction = { a, n -> lastAction = a; lastActionedNote = n },
                onNoteClick = {},
                onMessageRemoved = {}
            )
        }

        runBlocking {
            onNodeWithContentDescription(getString(Res.string.more_actions)).performClick()
            onNodeWithText(getString(Res.string.delete)).performClick()

            assertEquals(Action.DELETE, lastAction)
            assertEquals(initialNote, lastActionedNote)
        }
    }
}
