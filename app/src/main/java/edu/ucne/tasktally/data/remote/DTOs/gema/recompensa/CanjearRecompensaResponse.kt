package edu.ucne.tasktally.data.remote.DTOs.gema.recompensa

import com.squareup.moshi.Json

data class CanjearRecompensaResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String,
    @Json(name = "transaccionId") val transaccionId: Int?,
    @Json(name = "puntosRestantes") val puntosRestantes: Int?
)