package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.TransaccionEntity
import edu.ucne.tasktally.domain.models.Transaccion

fun TransaccionEntity.toDomain() = Transaccion(
    transaccionId = transaccionId,
    remoteId = remoteId,
    gemaId = gemaId,
    recompensaId = recompensaId,
    precio = precio,
    fecha = fecha,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun Transaccion.toEntity() = TransaccionEntity(
    transaccionId = transaccionId,
    remoteId = remoteId,
    gemaId = gemaId,
    recompensaId = recompensaId,
    precio = precio,
    fecha = fecha,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)
