package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.mentor.RecompensaMentorEntity
import edu.ucne.tasktally.domain.models.RecompensaMentor

fun RecompensaMentorEntity.toRecompensaMentorDomain() = RecompensaMentor(
    recompensaId = recompensaId,
    remoteId = remoteId,

    titulo = titulo,
    descripcion = descripcion,
    precio = precio,
    isDisponible = isDisponible,
    nombreImgVector = nombreImgVector,

    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun RecompensaMentor.toRecompensaMentorEntity() = RecompensaMentorEntity(
    recompensaId = recompensaId,
    remoteId = remoteId,

    titulo = titulo,
    descripcion = descripcion,
    precio = precio,
    isDisponible = isDisponible,
    nombreImgVector = nombreImgVector,

    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)
