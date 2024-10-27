package io.devexpert.kmptraining.data

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.devexpert.kmptraining.domain.User
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthRepositoryTest {
    private lateinit var userStorage: UserStorage
    private lateinit var remoteDataSource: AuthRemoteDataSource
    private lateinit var authRepository: AuthRepository

    @BeforeTest
    fun setup() {
        userStorage = mock(MockMode.autofill)
        remoteDataSource = mock(MockMode.autofill)
        authRepository = AuthRepository(
            remoteDataSource = remoteDataSource,
            userStorage = userStorage
        )
    }

    @Test
    fun testIsUserValidated_whenUserExists() = runBlocking {
        val testUser = testUser()
        every { userStorage.user } returns flowOf(testUser)

        val result = authRepository.isUserValidated()

        assertTrue(result)
    }

    @Test
    fun testIsUserValidated_whenUserDoesNotExist() = runBlocking {
        every { userStorage.user } returns flowOf(null)

        val result = authRepository.isUserValidated()

        assertFalse(result)
    }

    @Test
    fun testValidateToken() = runBlocking {
        val testUser = testUser()
        val testToken = "oauth_token"
        
        everySuspend { remoteDataSource.validateToken(testToken) } returns testUser

        authRepository.validateToken(testToken)

        verifySuspend { userStorage.saveUser(testUser) }
    }
}

private fun testUser(
    id: Int = 1,
    token: String = "test_token",
    email: String = "test@example.com",
    name: String = "Test User",
    pictureUrl: String = "https://example.com/picture.jpg"
) = User(
    id = id,
    token = token,
    email = email,
    name = name,
    pictureUrl = pictureUrl
)