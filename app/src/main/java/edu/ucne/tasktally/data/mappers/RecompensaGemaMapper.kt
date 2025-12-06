package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.RecompensaGemaEntity
import edu.ucne.tasktally.domain.models.RecompensaGema

fun RecompensaGemaEntity.toDomain() = RecompensaGema(
    recompensaId = recompensaId,
    remoteId = remoteId,
    titulo = titulo,
    descripcion = descripcion,
    precio = precio,
    nombreImgVector = nombreImgVector,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun RecompensaGema.toEntity() = RecompensaGemaEntity(
    recompensaId = recompensaId,
    remoteId = remoteId,
    titulo = titulo,
    descripcion = descripcion,
    precio = precio,
    nombreImgVector = nombreImgVector,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)
