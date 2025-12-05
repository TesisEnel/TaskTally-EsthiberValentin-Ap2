package edu.ucne.tasktally.data.remote.DTOs.auth

import com.squareup.moshi.Json


data class RegisterRequest(
    @Json(name = "userName") val userName: String,
    @Json(name = "password") val password: String,
    @Json(name = "email") val email: String?,
    @Json(name = "role") val role: String // "mentor" o "gema"
)

data class RegisterResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String?,
    @Json(name = "userId") val userId: Int?,
    @Json(name = "role") val role: String?,
    @Json(name = "zoneName") val zoneName: String?,
    @Json(name = "joinCode") val joinCode: String?
)

data class AssignRoleRequest(
    @Json(name = "userId") val userId: Int,
    @Json(name = "role") val role: String // "mentor" o "gema"
)
