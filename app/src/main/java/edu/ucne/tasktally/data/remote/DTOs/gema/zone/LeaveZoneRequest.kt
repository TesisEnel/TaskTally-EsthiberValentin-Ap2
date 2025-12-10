package edu.ucne.tasktally.data.remote.DTOs.gema.zone

import com.squareup.moshi.Json

data class LeaveZoneRequest(
    @Json(name = "gemaId") val gemaId: Int
)