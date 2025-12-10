package edu.ucne.tasktally.data.remote.DTOs.mentor.tareas

import com.squareup.moshi.Json

data class BulkTareasRequest(
    @Json(name = "mentorId") val mentorId: Int,

    @Json(name = "zoneId") val zoneId: Int,
    @Json(name = "tareas") val tareas: List<TareaOperationDto>
)

data class TareaOperationDto(
    @Json(name = "accion") val accion: String, // create, update, delete
    @Json(name = "tareasGroupId") val tareasGroupId: Int?,
    @Json(name = "titulo") val titulo: String?,
    @Json(name = "descripcion") val descripcion: String?,
    @Json(name = "puntos") val puntos: Int?,
    @Json(name = "dias") val dias: String?,
    @Json(name = "repetir") val repetir: Int?,
    @Json(name = "nombreImgVector") val nombreImgVector: String?,
)

data class BulkTareasResponse(
    @Json(name = "successful") val successful: Int,
    @Json(name = "failed") val failed: Int,
    @Json(name = "results") val results: List<TareaOperationResult>
)

data class TareaOperationResult(
    @Json(name = "accion") val accion: String,
    @Json(name = "tareasGroupId") val tareasGroupId: Int?,
    @Json(name="zoneId") val zoneId: Int?,
    @Json(name = "titulo") val titulo: String?,
    @Json(name = "success") val success: Boolean,
    @Json(name = "mensaje") val mensaje: String?
)