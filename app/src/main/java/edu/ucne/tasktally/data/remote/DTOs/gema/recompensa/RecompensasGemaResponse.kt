package edu.ucne.tasktally.data.remote.DTOs.gema.recompensa

data class RecompensasGemaResponse(
    val recompensaId: Int = 0,
    val titulo:String,
    val descripcion: String = "",
    val precio: Int = 0,
    val nombreImgVector: String = "",
    val canjeada: Boolean = false,
    val transaccionId: Int?
)