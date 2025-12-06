package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.ZonaEntity
import edu.ucne.tasktally.domain.models.Zona

fun ZonaEntity.toDomain() = Zona(
    zonaId = zonaId,
    remoteId = remoteId,
    joinCode = joinCode,
    mentorId = mentorId,
    zonaName = zonaName,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun Zona.toEntity() = ZonaEntity(
    zonaId = zonaId,
    remoteId = remoteId,
    joinCode = joinCode,
    mentorId = mentorId,
    zonaName = zonaName,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)