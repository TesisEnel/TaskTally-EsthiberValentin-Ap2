package edu.ucne.tasktally.data.remote.DTOs.recompensa

import com.squareup.moshi.Json

data class RecompensaDto(
    @Json(name = "recompensaId") val recompensaId: Int,
    @Json(name = "createdBy") val createdBy: Int,
    @Json(name = "titulo") val titulo: String,
    @Json(name = "descripcion") val descripcion: String,
    @Json(name = "precio") val precio: Int,
    @Json(name = "isDisponible") val isDisponible: Boolean,
    @Json(name = "fechaCreacion") val fechaCreacion: String,
    @Json(name = "createdByName") val createdByName: String?,
    @Json(name = "totalCanjes") val totalCanjes: Int
)

data class CreateRecompensaRequest(
    @Json(name = "titulo") val titulo: String,
    @Json(name = "descripcion") val descripcion: String,
    @Json(name = "precio") val precio: Int,
    @Json(name = "isDisponible") val isDisponible: Boolean = true
)

data class UpdateRecompensaRequest(
    @Json(name = "titulo") val titulo: String,
    @Json(name = "descripcion") val descripcion: String,
    @Json(name = "precio") val precio: Int,
    @Json(name = "isDisponible") val isDisponible: Boolean
)

data class CanjearRecompensaRequest(
    @Json(name = "gemaId") val gemaId: Int,
    @Json(name = "recompensaId") val recompensaId: Int
)

data class CanjearRecompensaResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "message") val message: String,
    @Json(name = "transaccionId") val transaccionId: Int?,
    @Json(name = "puntosRestantes") val puntosRestantes: Int?
)
