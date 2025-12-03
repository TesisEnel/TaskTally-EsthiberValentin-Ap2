package edu.ucne.tasktally.domain.models

data class Gema(
    val id: String = "",
    val remoteId: Int? = null,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val puntosActuales: Double,
    val isPendingPost: Boolean = true,
    val isPendingUpdate: Boolean = false
)
