package edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa

import com.squareup.moshi.Json

data class UpdateRecompensaRequest(
    @Json(name = "titulo") val titulo: String,
    @Json(name = "descripcion") val descripcion: String,
    @Json(name = "precio") val precio: Int,
    @Json(name = "isDisponible") val isDisponible: Boolean,
    val nombreImgVector: String
)