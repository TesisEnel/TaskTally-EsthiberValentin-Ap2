package edu.ucne.tasktally.data.remote.DTOs.mentor.tareas

import com.squareup.moshi.Json

data class CreateTareaRequest(
    @Json(name = "titulo") val titulo: String,
    @Json(name = "descripcion") val descripcion: String?,
    @Json(name = "puntos") val puntos: Int,
    @Json(name = "recurrente") val recurrente: Boolean,
    @Json(name = "dias") val dias: String?,
    @Json(name = "nombreImgVector") val nombreImgVector: String?,
    @Json(name = "asignada") val asignada: String // ID de gemas separado por coma, 0 si todas las de la zona.
)