package edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa

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

