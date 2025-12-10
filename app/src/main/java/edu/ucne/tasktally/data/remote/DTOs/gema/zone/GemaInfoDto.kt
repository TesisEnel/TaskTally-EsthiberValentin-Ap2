package edu.ucne.tasktally.data.remote.DTOs.gema.zone

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GemaInfoDto(
    val gemaId: Int,
    val nombre: String = "",
    val puntosActuales: Int = 0
)
