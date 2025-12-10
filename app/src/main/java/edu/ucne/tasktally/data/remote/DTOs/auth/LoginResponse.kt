package edu.ucne.tasktally.data.remote.DTOs.auth

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String?,
    @Json(name = "token") val token: String?,
    @Json(name = "refreshToken") val refreshToken: String?,
    @Json(name = "user") val user: UserInfo?,
    @Json(name = "expiresAt") val expiresAt: String?
)

data class UserInfo(
    @Json(name = "userId") val userId: Int,
    @Json(name = "userName") val userName: String,
    @Json(name = "email") val email: String?,
    @Json(name = "role") val role: String? = null,
    @Json(name = "mentorId") val mentorId: Int? = null,
    @Json(name = "gemaId") val gemaId: Int? = null,
    @Json(name = "zoneId") val zoneId: Int? = null,

    @Json(name = "puntosTotales") val puntosTotales: Int? = null,
    @Json(name = "puntosDisponibles") val puntosDisponibles: Int? = null,
    @Json(name = "puntosGastados") val puntosGastados: Int? = null
)
