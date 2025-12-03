package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.RecompensaEntity
import edu.ucne.tasktally.domain.models.Recompensa

fun RecompensaEntity.toDomain(): Recompensa {
    return Recompensa(
        id = id,
        remoteId = remoteId,
        titulo = titulo,
        descripcion = descripcion,
        precio = precio,
        isPendingPost = isPendingPost,
        isPendingUpdate = isPendingUpdate
    )
}

fun Recompensa.toEntity(): RecompensaEntity {
    return RecompensaEntity(
        id = id,
        remoteId = remoteId,
        titulo = titulo,
        descripcion = descripcion,
        precio = precio,
        isPendingPost = isPendingPost,
        isPendingUpdate = isPendingUpdate
    )
}