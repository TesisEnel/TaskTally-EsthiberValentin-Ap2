package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.remote.DTOs.gema.zone.GemaInfoDto
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.ZoneInfoGemaResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.ZoneInfoMentorResponse
import edu.ucne.tasktally.domain.models.Gema
import edu.ucne.tasktally.domain.models.Zona

fun ZoneInfoMentorResponse.toZonaDomain(): Zona {
    return Zona(
        zonaId = zoneId,
        nombre = zoneName,
        joinCode = joinCode,
        mentorId = "",
        gemas = gemas.map { it.toGemaDomain() }
    )
}

fun ZoneInfoGemaResponse.toZonaDomain(): Zona {
    return Zona(
        zonaId = zoneId,
        nombre = zoneName,
        joinCode = "",
        mentorId = "",
        gemas = gemas.map { it.toGemaDomain() }
    )
}

fun GemaInfoDto.toGemaDomain(): Gema {
    return Gema(
        gemaId = gemaId.toString(),
        remoteId = gemaId,
        nombre = gemaNombre,
        apellido = null,
        fechaNacimiento = null,
        puntosActuales = 0,
        zonaId = null,
        isPendingCreate = false,
        isPendingUpdate = false,
        isPendingDelete = false
    )
}
