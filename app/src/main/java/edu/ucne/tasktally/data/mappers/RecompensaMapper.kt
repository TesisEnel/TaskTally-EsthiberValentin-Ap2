package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.RecompensaEntity
import edu.ucne.tasktally.domain.models.Recompensa

fun RecompensaEntity.toDomain(): Recompensa = Recompensa(
    id = id,
    remoteId = remoteId,
    titulo = titulo,
    descripcion = descripcion,
    precio = precio,
    imgVector = imgVector,
    isPendingPost = isPendingPost,
    isPendingUpdate = isPendingUpdate
)

fun Recompensa.toEntity(): RecompensaEntity = RecompensaEntity(
    id = id,
    remoteId = remoteId,
    titulo = titulo,
    descripcion = descripcion,
    precio = precio,
    imgVector = imgVector,
    isPendingPost = isPendingPost,
    isPendingUpdate = isPendingUpdate
)