package edu.ucne.tasktally.data.remote.DTOs.mentor.tareas

import com.squareup.moshi.Json

data class UpdateTareaRequest(
    @Json(name = "tareaId") val tareaId: Int,
    @Json(name = "titulo") val titulo: String,
    @Json(name = "descripcion") val descripcion: String?,
    @Json(name = "puntos") val puntos: Int,
    @Json(name = "recurrente") val recurrente: Boolean,
    @Json(name = "dias") val dias: String?,
    @Json(name = "nombreImgVector") val nombreImgVector: String?,
    @Json(name = "asignada") val asignada: String
)