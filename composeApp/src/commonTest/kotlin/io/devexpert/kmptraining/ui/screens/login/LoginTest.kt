package io.devexpert.kmptraining.ui.screens.login

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.devexpert.kmptraining.ui.common.LOADING_INDICATOR_TEST_TAG
import io.devexpert.kmptraining.ui.screens.login.Login
import io.devexpert.kmptraining.ui.screens.login.LoginViewModel
import kmptraining.composeapp.generated.resources.Res
import kmptraining.composeapp.generated.resources.login_button
import kmptraining.composeapp.generated.resources.wrong_username
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class LoginTest {

    @Test
    fun testInitialState() = runComposeUiTest {
        setContent {
            Login(
                state = LoginViewModel.UiState(),
                onLoginClick = {},
                onLoggedIn = {}
            )
        }

        runBlocking {
            onNodeWithText(getString(Res.string.login_button)).assertIsDisplayed()
        }
    }

    @Test
    fun testLoadingState() = runComposeUiTest {
        setContent {
            Login(
                state = LoginViewModel.UiState(loading = true),
                onLoginClick = {},
                onLoggedIn = {}
            )
        }

        onNodeWithTag(LOADING_INDICATOR_TEST_TAG).assertIsDisplayed()
    }

    @Test
    fun testErrorState() = runComposeUiTest {
        setContent {
            Login(
                state = LoginViewModel.UiState(error = Res.string.wrong_username),
                onLoginClick = {},
                onLoggedIn = {}
            )
        }

        runBlocking {
            onNodeWithText(getString(Res.string.wrong_username)).assertIsDisplayed()
        }
    }

    @Test
    fun testLoginButtonClick() = runComposeUiTest {
        var loginClicked = false

        setContent {
            Login(
                state = LoginViewModel.UiState(),
                onLoginClick = { loginClicked = true },
                onLoggedIn = {}
            )
        }

        runBlocking {
            onNodeWithText(getString(Res.string.login_button)).performClick()
            assertTrue(loginClicked)
        }
    }

    @Test
    fun testLoggedInCallback() = runComposeUiTest {
        var loggedInCalled = false

        setContent {
            Login(
                state = LoginViewModel.UiState(loggedIn = true),
                onLoginClick = {},
                onLoggedIn = { loggedInCalled = true }
            )
        }

        assertTrue(loggedInCalled)
    }
}
