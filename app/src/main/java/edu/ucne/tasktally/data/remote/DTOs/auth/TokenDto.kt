package edu.ucne.tasktally.data.remote.DTOs.auth

import com.squareup.moshi.Json

data class RefreshTokenRequest(
    @Json(name = "refreshToken") val refreshToken: String
)

data class RefreshTokenResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "token") val token: String?,
    @Json(name = "refreshToken") val refreshToken: String?,
    @Json(name = "expiresAt") val expiresAt: String?
)

data class LogoutRequest(
    @Json(name = "refreshToken") val refreshToken: String
)

data class AssignRoleRequest(
    @Json(name = "userId") val userId: Int,
    @Json(name = "role") val role: String
)

