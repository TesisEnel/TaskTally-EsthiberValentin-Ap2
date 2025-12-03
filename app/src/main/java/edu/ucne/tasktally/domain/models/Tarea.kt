package edu.ucne.tasktally.domain.models

data class Tarea(
    val id: String = "",
    val remoteId: Int? = null,
    val estado: String,
    val titulo: String,
    val descripcion: String,
    val puntos: Double,
    val diaAsignada: String?,
    val imgVector: String?,
    val isPendingPost: Boolean = true,
    val isPendingUpdate: Boolean = false
)
