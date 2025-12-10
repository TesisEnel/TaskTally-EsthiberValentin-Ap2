package edu.ucne.tasktally.data.remote.DTOs.mentor.zone

import com.squareup.moshi.JsonClass
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.GemaInfoDto

@JsonClass(generateAdapter = true)
data class ZoneInfoMentorResponse(
    val zoneId: Int,
    val zoneName: String = "",
    val joinCode: String = "",
    val gemas: List<GemaInfoDto> = emptyList()
)
