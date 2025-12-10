package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.mentor.TareaMentorEntity
import edu.ucne.tasktally.domain.models.TareaMentor
import edu.ucne.tasktally.data.remote.DTOs.mentor.TareaDto

fun TareaMentorEntity.toTareaMentorDomain() = TareaMentor(
    tareaId = tareaId,
    remoteId = remoteId,

    titulo = titulo,
    descripcion = descripcion,
    puntos = puntos,
    repetir = repetir,
    dias = dias,
    nombreImgVector = nombreImgVector,

    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun TareaMentor.toTareaMentorEntity() = TareaMentorEntity(
    tareaId = tareaId,
    remoteId = remoteId,

    titulo = titulo,
    descripcion = descripcion,
    puntos = puntos,
    repetir = repetir,
    dias = dias,

    nombreImgVector = nombreImgVector,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun TareaMentor.toTareaDto() = TareaDto(
    titulo = titulo,
    descripcion = descripcion,
    puntos = puntos,
    repetir = repetir,
    dias = dias ?: "",
    nombreImgVector = nombreImgVector ?: "",
)

