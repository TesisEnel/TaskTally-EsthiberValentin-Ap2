package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.TareaMentorEntity
import edu.ucne.tasktally.domain.models.TareaMentor

fun TareaMentorEntity.toDomain() = TareaMentor(
    tareaId = tareaId,
    remoteId = remoteId,
    tareasGroupId = tareasGroupId,
    mentorId = mentorId,
    titulo = titulo,
    descripcion = descripcion,
    puntos = puntos,
    recurrente = recurrente,
    dias = dias,
    nombreImgVector = nombreImgVector,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun TareaMentor.toEntity() = TareaMentorEntity(
    tareaId = tareaId,
    remoteId = remoteId,
    tareasGroupId = tareasGroupId,
    mentorId = mentorId,
    titulo = titulo,
    descripcion = descripcion,
    puntos = puntos,
    recurrente = recurrente,
    dias = dias,
    nombreImgVector = nombreImgVector,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)
