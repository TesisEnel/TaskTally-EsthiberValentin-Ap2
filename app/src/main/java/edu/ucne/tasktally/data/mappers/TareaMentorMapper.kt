package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.TareaMentorEntity
import edu.ucne.tasktally.domain.models.TareaMentor
import edu.ucne.tasktally.data.remote.DTOs.mentor.TareaDto

fun TareaMentorEntity.toTareaMentorDomain() = TareaMentor(
    tareaId = tareaId,
    remoteId = remoteId,
    tareasGroupId = tareasGroupId,
    mentorId = userInfoId,
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

fun TareaMentor.toTareaMentorEntity() = TareaMentorEntity(
    tareaId = tareaId,
    remoteId = remoteId,
    tareasGroupId = tareasGroupId,
    userInfoId = mentorId,
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

fun TareaMentor.toTareaDto() = TareaDto(
    tareasGroupId = tareasGroupId ?: 0,
    mentorId = mentorId ?: 0,
    titulo = titulo,
    descripcion = descripcion,
    puntos = puntos,
    recurrente = recurrente,
    dias = dias ?: "",
    nombreImgVector = nombreImgVector ?: "",
    gemas = emptyList()
)

