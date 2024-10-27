package io.devexpert.kmptraining.data

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthRepositoryTest {
    private lateinit var oAuthServer: OAuthServer
    private lateinit var tokenStorage: TokenStorage
    private lateinit var authRepository: AuthRepository

    @BeforeTest
    fun setup() {
        oAuthServer = mock(MockMode.autofill)
        tokenStorage = mock(MockMode.autofill)
        authRepository = AuthRepository(
            serverUrl = "https://example.com",
            oAuthServer = oAuthServer,
            tokenStorage = tokenStorage
        )
    }

    @Test
    fun testInitiateOAuth() {
        val result = authRepository.initiateOAuth()

        assertEquals("https://example.com/login", result)
        verify { oAuthServer.start() }
    }

    @Test
    fun testHandleAuthCode() = runBlocking {
        val testToken = "test_token"
        every { oAuthServer.authCode } returns flowOf(testToken)

        authRepository.handleAuthCode()

        verify { tokenStorage.saveToken(testToken) }
    }

    @Test
    fun testStop() {
        authRepository.stop()

        verify { oAuthServer.stop() }
    }
}
