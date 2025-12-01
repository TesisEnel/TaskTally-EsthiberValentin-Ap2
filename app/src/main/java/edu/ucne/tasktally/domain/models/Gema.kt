package edu.ucne.tasktally.domain.models

data class Gema(
    val gemaId: Int = 0,
    val userId: Int,
    val userInfoId: Int?,
    val puntosActuales: Double,
    val puntosTotales: Double
)
