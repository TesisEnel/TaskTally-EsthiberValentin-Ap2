package edu.ucne.tasktally.data.remote.interceptors

import edu.ucne.tasktally.data.local.preferences.AuthPreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authPreferencesManager: AuthPreferencesManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.url.pathSegments.contains("auth")) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking {
            authPreferencesManager.accessToken.first()
        }

        val authenticatedRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        return chain.proceed(authenticatedRequest)
    }
}