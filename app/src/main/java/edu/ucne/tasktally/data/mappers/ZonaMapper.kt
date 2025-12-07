package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.ZonaEntity
import edu.ucne.tasktally.domain.models.Zona

fun ZonaEntity.toDomain(): Zona {
    return Zona(
        zonaId = zonaId,
        nombre = nombre,
        joinCode = joinCode,
        mentorId = mentorId
    )
}

fun Zona.toEntity(): ZonaEntity {
    return ZonaEntity(
        zonaId = zonaId,
        nombre = nombre,
        joinCode = joinCode,
        mentorId = mentorId
    )
}
