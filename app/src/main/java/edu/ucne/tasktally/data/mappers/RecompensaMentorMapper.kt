package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.RecompensaMentorEntity
import edu.ucne.tasktally.domain.models.RecompensaMentor

fun RecompensaMentorEntity.toDomain() = RecompensaMentor(
    recompensaId = recompensaId,
    remoteId = remoteId,
    createdBy = createdBy,
    titulo = titulo,
    descripcion = descripcion,
    precio = precio,
    isDisponible = isDisponible,
    fechaCreacion = fechaCreacion,
    createdByName = createdByName,
    totalCanjes = totalCanjes,
    nombreImgVector = nombreImgVector,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun RecompensaMentor.toEntity() = RecompensaMentorEntity(
    recompensaId = recompensaId,
    remoteId = remoteId,
    createdBy = createdBy,
    titulo = titulo,
    descripcion = descripcion,
    precio = precio,
    isDisponible = isDisponible,
    fechaCreacion = fechaCreacion,
    createdByName = createdByName,
    totalCanjes = totalCanjes,
    nombreImgVector = nombreImgVector,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)
