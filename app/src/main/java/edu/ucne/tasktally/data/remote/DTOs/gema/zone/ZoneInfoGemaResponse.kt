package edu.ucne.tasktally.data.remote.DTOs.gema.zone

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ZoneInfoGemaResponse(
    val zoneId: Int,
    val zoneName: String = "",
    val gemas: List<GemaInfoDto> = emptyList()
)
