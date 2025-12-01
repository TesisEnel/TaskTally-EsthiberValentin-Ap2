package edu.ucne.tasktally.domain.models

data class Progreso(
    val progresoId: Int = 0,
    val gemaId: Int,
    val tareaId: Int,
    val estadoId: Int?,
    val fechaAsignacion: String,
    val fechaCompletada: String?,
    val puntosGanados: Double
)

