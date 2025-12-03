package edu.ucne.tasktally.domain.models

data class Recompensa(
    val id: String = "",
    val remoteId: Int? = null,
    val titulo: String,
    val descripcion: String,
    val precio: Double,
    val isPendingPost: Boolean = true,
    val isPendingUpdate: Boolean = false
)

