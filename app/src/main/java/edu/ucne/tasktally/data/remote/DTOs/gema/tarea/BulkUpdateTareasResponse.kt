package edu.ucne.tasktally.data.remote.DTOs.gema.tarea

import com.squareup.moshi.Json

data class BulkUpdateTareasResponse(
    @Json(name = "successful") val successful: Int,
    @Json(name = "failed") val failed: Int,
    @Json(name = "results") val results: List<TareaUpdateResult>
)