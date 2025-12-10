package edu.ucne.tasktally.data.remote.DTOs.mentor.zone

import com.squareup.moshi.Json

data class UpdateZoneResponse(
    @Json(name = "zoneName")  val zoneName: String
)
