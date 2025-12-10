package edu.ucne.tasktally.data.remote.DTOs.gema.zone

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class JoinZoneRequest(
    @field:Json(name = "gemaId") val gemaId: Int,
    @field:Json(name = "zoneCode") val zoneCode: String
)

data class JoinZoneResponse(
    @field:Json(name = "zoneId") val zoneId: Int? = null,
    @field:Json(name = "zoneName") val zoneName: String? = null
)