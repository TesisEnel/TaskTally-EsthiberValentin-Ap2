package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.gemas.RecompensaGemaEntity
import edu.ucne.tasktally.data.remote.DTOs.gema.recompensa.RecompensasGemaResponse
import edu.ucne.tasktally.domain.models.RecompensaGema

fun RecompensaGemaEntity.toRecompensaGemaDomain() = RecompensaGema(
    recompensaId = recompensaId,
    remoteId = remoteId,
    transaccionId = transaccionId,
    canjeada = canjeada,
    titulo = titulo,
    descripcion = descripcion,
    precio = precio,
    nombreImgVector = nombreImgVector,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun RecompensaGema.toRecompensaGemaEntity() = RecompensaGemaEntity(
    recompensaId = recompensaId,
    remoteId = remoteId,
    transaccionId = transaccionId,
    canjeada = canjeada,
    titulo = titulo,
    descripcion = descripcion,
    precio = precio,
    nombreImgVector = nombreImgVector,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun RecompensasGemaResponse.toRecompensaGemaEntity() = RecompensaGemaEntity(
    remoteId = recompensaId,
    titulo = titulo,
    descripcion = descripcion,
    precio = precio,
    nombreImgVector = nombreImgVector,
    transaccionId = transaccionId,
    canjeada = canjeada,
    isPendingCreate = false,
    isPendingUpdate = false,
    isPendingDelete = false
)

