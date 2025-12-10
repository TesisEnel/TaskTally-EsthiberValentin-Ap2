package edu.ucne.tasktally.data.remote.DTOs.gema.recompensa

import com.squareup.moshi.Json

data class CanjearRecompensaRequest(
    @Json(name = "gemaId") val gemaId: Int,
    @Json(name = "recompensaId") val recompensaId: Int
)