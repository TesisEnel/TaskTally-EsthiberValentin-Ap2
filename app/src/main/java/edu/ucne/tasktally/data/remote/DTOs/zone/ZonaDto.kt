package edu.ucne.tasktally.data.remote.DTOs.zone

import com.squareup.moshi.Json

data class ZoneDto(
    @Json(name = "zoneId") val zoneId: Int,
    @Json(name = "mentorId") val mentorId: Int,
    @Json(name = "joinCode") val joinCode: String,
    @Json(name = "zoneName") val zoneName: String,
    @Json(name = "mentorName") val mentorName: String,
    @Json(name = "totalTareas") val totalTareas: Int,
    @Json(name = "totalMiembros") val totalMiembros: Int
)

data class UpdateZoneRequest(
    @Json(name = "zoneName") val zoneName: String
)

data class JoinZoneRequest(
    @Json(name = "gemaId") val gemaId: Int,
    @Json(name = "zoneCode") val zoneCode: String
)

data class LeaveZoneRequest(
    @Json(name = "gemaId") val gemaId: Int,
    @Json(name = "zoneCode") val zoneCode: String
)