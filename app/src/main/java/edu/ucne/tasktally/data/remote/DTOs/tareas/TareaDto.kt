package edu.ucne.tasktally.data.remote.DTOs.tareas


import com.squareup.moshi.Json

data class TareaDto(
    @Json(name = "tareasId") val tareasId: Int,
    @Json(name = "titulo") val titulo: String,
    @Json(name = "descripcion") val descripcion: String?,
    @Json(name = "puntos") val puntos: Int,
    @Json(name = "estado") val estado: String
)

data class TareaGemaRequest(
    @Json(name = "gemaId") val gemaId: Int
)

data class TareaGemaResponse(
    @Json(name = "tareaId") val tareaId: Int,
    @Json(name = "gemaId") val gemaId: Int,
    @Json(name = "titulo") val titulo: String,
    @Json(name = "estado") val estado: String,
    @Json(name = "puntos") val puntos: Int,
    @Json(name = "descripcion") val descripcion: String,
    @Json(name = "nombreImgVector") val nombreImgVector: String
)
