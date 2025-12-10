package edu.ucne.tasktally.data.remote.DTOs.gema.tarea

data class TareasGemaResponse(
    val tareaId: Int = 0,
    val gemaId: Int = 0,
    val titulo: String = "",
    val estado: String = "",
    val puntos: Int = 0,
    val descripcion: String = "",
    val nombreImgVector: String = "",
    val dia: String ="",
    val gemaCompletoNombre: String? = null,
)
