package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.MentorEntity
import edu.ucne.tasktally.domain.models.Mentor

fun MentorEntity.toDomain() = Mentor(
    mentorId = mentorId,
    remoteId = remoteId,
    userId = userId,
    nombre = nombre,
    apellido = apellido,
    fechaNacimiento = fechaNacimiento,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun Mentor.toEntity() = MentorEntity(
    mentorId = mentorId,
    remoteId = remoteId,
    userId = userId,
    nombre = nombre,
    apellido = apellido,
    fechaNacimiento = fechaNacimiento,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)