package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.MentorEntity
import edu.ucne.tasktally.domain.models.Mentor

fun MentorEntity.toDomain() = Mentor(
    id = id,
    remoteId = remoteId,
    userId = userId,
    nombre = nombre,
    apellido = apellido,
    fechaNacimiento = fechaNacimiento,
    isPendingPost = isPendingPost,
    isPendingUpdate = isPendingUpdate
)

fun Mentor.toEntity() = MentorEntity(
    id = id,
    remoteId = remoteId,
    userId = userId,
    nombre = nombre,
    apellido = apellido,
    fechaNacimiento = fechaNacimiento,
    isPendingPost = isPendingPost,
    isPendingUpdate = isPendingUpdate
)