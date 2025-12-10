package edu.ucne.tasktally.data.remote.interceptors

import edu.ucne.tasktally.data.local.preferences.AuthPreferencesManager
import edu.ucne.tasktally.data.remote.AuthApi
import edu.ucne.tasktally.data.remote.DTOs.auth.RefreshTokenRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val authPreferencesManager: AuthPreferencesManager,
    private val authApi: AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 401) {
            return runBlocking {
                try {
                    val refreshToken = authPreferencesManager.refreshToken.first()
                    if (refreshToken.isNullOrEmpty()) {
                        authPreferencesManager.clearAuthData()
                        return@runBlocking null
                    }

                    val refreshResponse = authApi.refreshToken(RefreshTokenRequest(refreshToken))
                    if (refreshResponse.isSuccessful && refreshResponse.body() != null) {
                        val refreshData = refreshResponse.body()!!
                        if (refreshData.success && refreshData.token != null) {
                            authPreferencesManager.updateTokens(
                                accessToken = refreshData.token,
                                refreshToken = refreshData.refreshToken ?: refreshToken,
                                expiresAt = refreshData.expiresAt
                            )

                            response.request.newBuilder()
                                .header("Authorization", "Bearer ${refreshData.token}")
                                .build()
                        } else {
                            authPreferencesManager.clearAuthData()
                            null
                        }
                    } else {
                        authPreferencesManager.clearAuthData()
                        null
                    }
                } catch (e: Exception) {
                    authPreferencesManager.clearAuthData()
                    null
                }
            }
        }
        return null
    }
}