package edu.ucne.tasktally.data.remote.DTOs.gema.zone

import com.squareup.moshi.Json

data class JoinZoneRequest(
    @Json(name = "gemaId") val gemaId: Int,
    @Json(name = "zoneCode") val zoneCode: String
)

data class JoinZoneResponse(
    val zoneId:Int,
    val zoneName:String
)