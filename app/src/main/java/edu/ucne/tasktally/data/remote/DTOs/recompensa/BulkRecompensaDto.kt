package edu.ucne.tasktally.data.remote.DTOs.recompensa

import com.squareup.moshi.Json

data class BulkRecompensasRequest(
    @Json(name = "mentorId") val mentorId: Int,
    @Json(name = "recompensas") val recompensas: List<RecompensaOperationDto>
)

data class RecompensaOperationDto(
    @Json(name = "accion") val accion: String, // "create", "update", "delete"
    @Json(name = "recompensaId") val recompensaId: Int?,
    @Json(name = "titulo") val titulo: String?,
    @Json(name = "descripcion") val descripcion: String?,
    @Json(name = "precio") val precio: Int?,
    @Json(name = "isDisponible") val isDisponible: Boolean?
)

data class BulkRecompensasResponse(
    @Json(name = "successful") val successful: Int,
    @Json(name = "failed") val failed: Int,
    @Json(name = "results") val results: List<RecompensaOperationResult>
)

data class RecompensaOperationResult(
    @Json(name = "accion") val accion: String,
    @Json(name = "recompensaId") val recompensaId: Int?,
    @Json(name = "success") val success: Boolean,
    @Json(name = "mensaje") val mensaje: String?
)