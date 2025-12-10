package edu.ucne.tasktally.data.remote

import edu.ucne.tasktally.data.remote.DTOs.auth.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>

    @POST("api/auth/logout")
    suspend fun logout(@Body request: LogoutRequest): Response<Unit>

    @GET("api/auth/me")
    suspend fun getCurrentUser(): Response<UserInfo>

    @POST("api/auth/assign-role")
    suspend fun assignRole(@Body request: AssignRoleRequest): Response<Unit>
}
