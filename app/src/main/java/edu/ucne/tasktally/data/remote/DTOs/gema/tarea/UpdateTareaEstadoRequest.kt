package edu.ucne.tasktally.data.remote.DTOs.gema.tarea

import com.squareup.moshi.Json

data class UpdateTareaEstadoRequest(
    @Json(name = "tareaId") val tareaId: Int,
    @Json(name = "estado") val estado: String
)