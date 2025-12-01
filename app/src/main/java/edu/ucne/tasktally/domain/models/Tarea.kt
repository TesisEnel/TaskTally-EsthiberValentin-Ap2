package edu.ucne.tasktally.domain.models

data class Tarea(
    val tareaId: Int = 0,
    val createdBy: Int,
    val zonaId: Int,
    val estadoId: Int?,
    val titulo: String,
    val descripcion: String,
    val puntos: Double,
    val diaAsignada: String?,
    val recurrente: String?,
    val imgVector: String?,
    val fechaCreacion: String
)
