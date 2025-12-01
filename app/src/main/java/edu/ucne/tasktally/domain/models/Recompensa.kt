package edu.ucne.tasktally.domain.models

data class Recompensa(
    val recompensaId: Int = 0,
    val createdBy: Int,
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val isDisponible: Boolean,
    val fechaCreacion: String
)

