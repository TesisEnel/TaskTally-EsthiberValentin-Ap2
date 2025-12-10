package edu.ucne.tasktally.data.remote.DTOs.auth

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name = "userName") val userName: String,
    @Json(name = "password") val password: String
)
