package edu.ucne.tasktally.repositories_test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.tasktally.data.local.preferences.AuthPreferencesManager
import edu.ucne.tasktally.data.remote.AuthApi
import edu.ucne.tasktally.data.remote.DTOs.auth.AssignRoleRequest
import edu.ucne.tasktally.data.remote.DTOs.auth.LoginResponse
import edu.ucne.tasktally.data.remote.DTOs.auth.RefreshTokenResponse
import edu.ucne.tasktally.data.remote.DTOs.auth.RegisterRequest
import edu.ucne.tasktally.data.remote.DTOs.auth.RegisterResponse
import edu.ucne.tasktally.data.remote.DTOs.auth.UserInfo
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.repositories.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import retrofit2.Response
import kotlin.test.Test
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*

import java.io.IOException

@ExperimentalCoroutinesApi
class AuthRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: AuthRepository
    private lateinit var authApi: AuthApi
    private lateinit var authPreferencesManager: AuthPreferencesManager

    @Before
    fun setup() {
        authApi = mockk(relaxed = true)
        authPreferencesManager = mockk(relaxed = true)
        repository = AuthRepository(authApi, authPreferencesManager)
    }

    // region Login Tests
    @Test
    fun `login retorna Success cuando API responde correctamente`() = runTest {
        // Given
        val username = "testUser"
        val password = "testPassword"
        val userInfo = UserInfo(
            userId = 1,
            userName = username,
            email = "test@test.com",
            role = "mentor",
            mentorId = 1,
            gemaId = null,
            zoneId = 1,
            puntosDisponibles = 100,
            puntosGastados = 50
        )
        val loginResponse = LoginResponse(
            success = true,
            message = "Login successful",
            token = "access_token_123",
            refreshToken = "refresh_token_123",
            expiresAt = "2024-12-31T23:59:59",
            user = userInfo
        )

        coEvery { authApi.login(any()) } returns Response.success(loginResponse)
        coEvery {
            authPreferencesManager.saveAuthData(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        } just Runs

        // When
        val result = repository.login(username, password)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(loginResponse, (result as Resource.Success).data)
        coVerify {
            authPreferencesManager.saveAuthData(
                accessToken = "access_token_123",
                refreshToken = "refresh_token_123",
                userId = 1,
                username = username,
                email = "test@test.com",
                expiresAt = "2024-12-31T23:59:59",
                role = "mentor",
                mentorId = 1,
                gemaId = null,
                zoneId = 1,
                puntosDisponibles = 100,
                puntosGastados = 50
            )
        }
    }

    @Test
    fun `login retorna Error cuando success es false`() = runTest {
        // Given
        val loginResponse = LoginResponse(
            success = false,
            message = "Invalid credentials",
            token = null,
            refreshToken = null,
            expiresAt = null,
            user = null
        )

        coEvery { authApi.login(any()) } returns Response.success(loginResponse)

        // When
        val result = repository.login("user", "wrongPassword")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Invalid credentials", (result as Resource.Error).message)
        coVerify(exactly = 0) {
            authPreferencesManager.saveAuthData(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()
            )
        }
    }

    @Test
    fun `login retorna Error cuando API falla`() = runTest {
        // Given
        coEvery { authApi.login(any()) } returns Response.error(
            401,
            "Unauthorized".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.login("user", "password")

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Network error"))
    }

    @Test
    fun `login retorna Error cuando hay excepcion`() = runTest {
        // Given
        coEvery { authApi.login(any()) } throws IOException("Network unavailable")

        // When
        val result = repository.login("user", "password")

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Unexpected error"))
    }
    // endregion

    // region Register Tests
    @Test
    fun `register retorna Success cuando API responde correctamente`() = runTest {
        // Given
        val registerResponse = RegisterResponse(
            success = true,
            message = "Registration successful",
            userId = 1,
            role = "mentor",
            zoneId = 1,
            zoneName = "Mi Zona",
            joinCode = "ABC123"
        )

        coEvery { authApi.register(any()) } returns Response.success(registerResponse)

        // When
        val result = repository.register("newUser", "password123", "email@test.com", "mentor")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(registerResponse, (result as Resource.Success).data)
        assertEquals(1, result.data?.userId)
        assertEquals("mentor", result.data?.role)
        assertEquals(1, result.data?.zoneId)
        assertEquals("Mi Zona", result.data?.zoneName)
        assertEquals("ABC123", result.data?.joinCode)
    }

    @Test
    fun `register retorna Success para gema sin zona`() = runTest {
        // Given
        val registerResponse = RegisterResponse(
            success = true,
            message = "Registration successful",
            userId = 2,
            role = "gema",
            zoneId = null,
            zoneName = null,
            joinCode = null
        )

        coEvery { authApi.register(any()) } returns Response.success(registerResponse)

        // When
        val result = repository.register("gemaUser", "password123", "gema@test.com", "gema")

        // Then
        assertTrue(result is Resource.Success)
        assertEquals("gema", (result as Resource.Success).data?.role)
        assertNull(result.data?.zoneId)
        assertNull(result.data?.zoneName)
        assertNull(result.data?.joinCode)
    }

    @Test
    fun `register retorna Error cuando success es false`() = runTest {
        // Given
        val registerResponse = RegisterResponse(
            success = false,
            message = "Username already exists",
            userId = null,
            role = null,
            zoneId = null,
            zoneName = null,
            joinCode = null
        )

        coEvery { authApi.register(any()) } returns Response.success(registerResponse)

        // When
        val result = repository.register("existingUser", "password", null, "mentor")

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Username already exists", (result as Resource.Error).message)
    }

    @Test
    fun `register retorna Error cuando API falla`() = runTest {
        // Given
        coEvery { authApi.register(any()) } returns Response.error(
            500,
            "Server Error".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.register("user", "password", "email@test.com", "mentor")

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Network error"))
    }

    @Test
    fun `register retorna Error cuando hay excepcion`() = runTest {
        // Given
        coEvery { authApi.register(any()) } throws IOException("Network unavailable")

        // When
        val result = repository.register("user", "password", null, "gema")

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Unexpected error"))
    }

    @Test
    fun `register envia request con datos correctos`() = runTest {
        // Given
        val requestSlot = slot<RegisterRequest>()
        val registerResponse = RegisterResponse(
            success = true,
            message = "OK",
            userId = 1,
            role = "mentor",
            zoneId = 1,
            zoneName = "Zona",
            joinCode = "CODE"
        )

        coEvery { authApi.register(capture(requestSlot)) } returns Response.success(registerResponse)

        // When
        repository.register("testUser", "testPass", "test@email.com", "mentor")

        // Then
        assertEquals("testUser", requestSlot.captured.userName)
        assertEquals("testPass", requestSlot.captured.password)
        assertEquals("test@email.com", requestSlot.captured.email)
        assertEquals("mentor", requestSlot.captured.role)
    }
    // endregion

    // region RefreshToken Tests
    @Test
    fun `refreshToken retorna Success cuando API responde correctamente`() = runTest {
        // Given
        val refreshResponse = RefreshTokenResponse(
            success = true,
            token = "new_access_token",
            refreshToken = "new_refresh_token",
            expiresAt = "2024-12-31T23:59:59"
        )

        coEvery { authPreferencesManager.refreshToken } returns flowOf("old_refresh_token")
        coEvery { authApi.refreshToken(any()) } returns Response.success(refreshResponse)
        coEvery { authPreferencesManager.updateTokens(any(), any(), any()) } just Runs

        // When
        val result = repository.refreshToken()

        // Then
        assertTrue(result is Resource.Success)
        coVerify {
            authPreferencesManager.updateTokens(
                accessToken = "new_access_token",
                refreshToken = "new_refresh_token",
                expiresAt = "2024-12-31T23:59:59"
            )
        }
    }

    @Test
    fun `refreshToken retorna Error cuando no hay refresh token disponible`() = runTest {
        // Given
        coEvery { authPreferencesManager.refreshToken } returns flowOf(null)

        // When
        val result = repository.refreshToken()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("No refresh token available", (result as Resource.Error).message)
    }

    @Test
    fun `refreshToken retorna Error cuando refresh token esta vacio`() = runTest {
        // Given
        coEvery { authPreferencesManager.refreshToken } returns flowOf("")

        // When
        val result = repository.refreshToken()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("No refresh token available", (result as Resource.Error).message)
    }

    @Test
    fun `refreshToken retorna Error cuando API falla`() = runTest {
        // Given
        coEvery { authPreferencesManager.refreshToken } returns flowOf("valid_token")
        coEvery { authApi.refreshToken(any()) } returns Response.error(
            401,
            "Token expired".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.refreshToken()

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Network error"))
    }

    @Test
    fun `refreshToken retorna Error cuando success es false`() = runTest {
        // Given
        val refreshResponse = RefreshTokenResponse(
            success = false,
            token = null,
            refreshToken = null,
            expiresAt = null
        )

        coEvery { authPreferencesManager.refreshToken } returns flowOf("old_token")
        coEvery { authApi.refreshToken(any()) } returns Response.success(refreshResponse)

        // When
        val result = repository.refreshToken()

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Token refresh failed", (result as Resource.Error).message)
    }
    // endregion

    // region Logout Tests
    @Test
    fun `logout limpia datos de autenticacion exitosamente`() = runTest {
        // Given
        coEvery { authPreferencesManager.refreshToken } returns flowOf("refresh_token")
        coEvery { authApi.logout(any()) } returns Response.success(Unit)
        coEvery { authPreferencesManager.clearAuthData() } just Runs

        // When
        val result = repository.logout()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { authPreferencesManager.clearAuthData() }
    }

    @Test
    fun `logout limpia datos incluso cuando API falla`() = runTest {
        // Given
        coEvery { authPreferencesManager.refreshToken } returns flowOf("refresh_token")
        coEvery { authApi.logout(any()) } throws IOException("Network error")
        coEvery { authPreferencesManager.clearAuthData() } just Runs

        // When
        val result = repository.logout()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { authPreferencesManager.clearAuthData() }
    }

    @Test
    fun `logout funciona cuando no hay refresh token`() = runTest {
        // Given
        coEvery { authPreferencesManager.refreshToken } returns flowOf(null)
        coEvery { authPreferencesManager.clearAuthData() } just Runs

        // When
        val result = repository.logout()

        // Then
        assertTrue(result is Resource.Success)
        coVerify { authPreferencesManager.clearAuthData() }
        coVerify(exactly = 0) { authApi.logout(any()) }
    }
    // endregion

    // region GetCurrentUser Tests
    @Test
    fun `getCurrentUserFromApi retorna Success cuando API responde correctamente`() = runTest {
        // Given
        val userInfo = UserInfo(
            userId = 1,
            userName = "testUser",
            email = "test@test.com",
            role = "mentor",
            mentorId = 1,
            gemaId = null,
            zoneId = 1,
            puntosDisponibles = 100,
            puntosGastados = 50
        )

        coEvery { authApi.getCurrentUser() } returns Response.success(userInfo)

        // When
        val result = repository.getCurrentUserFromApi()

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(userInfo, (result as Resource.Success).data)
    }

    @Test
    fun `getCurrentUserFromApi retorna Error cuando API falla`() = runTest {
        // Given
        coEvery { authApi.getCurrentUser() } returns Response.error(
            401,
            "Unauthorized".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.getCurrentUserFromApi()

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Failed to get user info"))
    }

    @Test
    fun `getCurrentUserFromApi retorna Error cuando hay excepcion`() = runTest {
        // Given
        coEvery { authApi.getCurrentUser() } throws IOException("Network error")

        // When
        val result = repository.getCurrentUserFromApi()

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Unexpected error"))
    }
    // endregion

    // region AssignRole Tests
    @Test
    fun `assignRole retorna Success cuando API responde correctamente`() = runTest {
        // Given
        coEvery { authApi.assignRole(any()) } returns Response.success(Unit)

        // When
        val result = repository.assignRole(1, "mentor")

        // Then
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `assignRole retorna Error cuando API falla`() = runTest {
        // Given
        coEvery { authApi.assignRole(any()) } returns Response.error(
            400,
            "Bad Request".toResponseBody("text/plain".toMediaTypeOrNull())
        )

        // When
        val result = repository.assignRole(1, "invalid_role")

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Failed to assign role"))
    }

    @Test
    fun `assignRole retorna Error cuando hay excepcion`() = runTest {
        // Given
        coEvery { authApi.assignRole(any()) } throws IOException("Network error")

        // When
        val result = repository.assignRole(1, "mentor")

        // Then
        assertTrue(result is Resource.Error)
        assertTrue((result as Resource.Error).message!!.contains("Unexpected error"))
    }

    @Test
    fun `assignRole envia request con datos correctos`() = runTest {
        // Given
        val requestSlot = slot<AssignRoleRequest>()
        coEvery { authApi.assignRole(capture(requestSlot)) } returns Response.success(Unit)

        // When
        repository.assignRole(5, "gema")

        // Then
        assertEquals(5, requestSlot.captured.userId)
        assertEquals("gema", requestSlot.captured.role)
    }
    // endregion

    @Test
    fun `isLoggedIn retorna flow de authPreferencesManager`() = runTest {
        // Given
        coEvery { authPreferencesManager.isLoggedIn } returns flowOf(true)

        // When
        val result = repository.isLoggedIn()

        // Then
        assertEquals(true, result.first())
    }

    @Test
    fun `getAccessToken retorna flow de authPreferencesManager`() = runTest {
        // Given
        val expectedToken = "access_token_123"
        coEvery { authPreferencesManager.accessToken } returns flowOf(expectedToken)

        // When
        val result = repository.getAccessToken()

        // Then
        assertEquals(expectedToken, result.first())
    }
    // endregion
}