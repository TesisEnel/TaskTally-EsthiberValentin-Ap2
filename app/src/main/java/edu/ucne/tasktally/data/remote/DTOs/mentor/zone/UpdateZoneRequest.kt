package edu.ucne.tasktally.data.remote.DTOs.mentor.zone

import com.squareup.moshi.Json

data class UpdateZoneRequest(
    @Json(name = "zoneName") val zoneName: String
)