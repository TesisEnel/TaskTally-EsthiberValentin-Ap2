package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.ZonaEntity
import edu.ucne.tasktally.domain.models.Zona

fun ZonaEntity.toDomain() = Zona(
    id = id,
    remoteId = remoteId,
    joinCode = joinCode,
    mentorId = mentorId,
    zonaName = zonaName,
    isPendingPost = isPendingPost,
    isPendingUpdate = isPendingUpdate
)

fun Zona.toEntity() = ZonaEntity(
    id = id,
    remoteId = remoteId,
    joinCode = joinCode,
    mentorId = mentorId,
    zonaName = zonaName,
    isPendingPost = isPendingPost,
    isPendingUpdate = isPendingUpdate
)