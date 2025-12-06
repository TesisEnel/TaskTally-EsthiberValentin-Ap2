package edu.ucne.tasktally.data.remote.DTOs.gema.tarea

import com.squareup.moshi.Json

data class TareaUpdateResult(
    @Json(name = "tareaId") val tareaId: Int,
    @Json(name = "success") val success: Boolean,
    @Json(name = "mensaje") val mensaje: String,
    @Json(name = "nuevoEstado") val nuevoEstado: String?
)