package io.devexpert.kmptraining.ui.screens.notedetail

import androidx.compose.ui.test.*
import io.devexpert.kmptraining.domain.Note
import io.devexpert.kmptraining.ui.common.LOADING_INDICATOR_TAG
import kmptraining.composeapp.generated.resources.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class NoteDetailTest {

    @Test
    fun testLoadingState() = runComposeUiTest {
        setContent {
            NoteDetail(
                state = NoteDetailViewModel.UiState(isLoading = true),
                onSave = {},
                onBack = {}
            )
        }

        onNodeWithTag(LOADING_INDICATOR_TAG).assertIsDisplayed()
    }

    @Test
    fun testErrorState() = runComposeUiTest {
        val errorMessage = "Error loading note"
        setContent {
            NoteDetail(
                state = NoteDetailViewModel.UiState(error = errorMessage),
                onSave = {},
                onBack = {}
            )
        }

        onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun testNoteDisplayAndEdit() = runComposeUiTest {
        val initialNote = Note(id = 1, title = "Test Title", content = "Test Content")
        var updatedNote: Note? = null

        setContent {
            NoteDetail(
                state = NoteDetailViewModel.UiState(note = initialNote),
                onSave = { updatedNote = it },
                onBack = {}
            )
        }

        runBlocking {
            onNodeWithText("Test Title").assertIsDisplayed()
            onNodeWithText("Test Content").assertIsDisplayed()

            onNodeWithText("Test Title").performTextReplacement("Updated Title")
            onNodeWithText("Test Content").performTextReplacement("Updated Content")

            onNodeWithContentDescription(getString(Res.string.save)).performClick()

            assertEquals("Updated Title", updatedNote?.title)
            assertEquals("Updated Content", updatedNote?.content)
        }
    }

    @Test
    fun testBackButton() = runComposeUiTest {
        var backPressed = false

        setContent {
            NoteDetail(
                state = NoteDetailViewModel.UiState(note = Note.Empty),
                onSave = {},
                onBack = { backPressed = true }
            )
        }

        runBlocking {
            onNodeWithContentDescription(getString(Res.string.back)).performClick()
            assertTrue(backPressed)
        }
    }

    @Test
    fun testSaveButton() = runComposeUiTest {
        var savePressed = false

        setContent {
            NoteDetail(
                state = NoteDetailViewModel.UiState(note = Note.Empty),
                onSave = { savePressed = true },
                onBack = {}
            )
        }

        runBlocking {
            onNodeWithContentDescription(getString(Res.string.save)).performClick()
            assertTrue(savePressed)
        }
    }
}
