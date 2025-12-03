package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.TareaEntity
import edu.ucne.tasktally.domain.models.Tarea

fun TareaEntity.toDomain(): Tarea {
    return Tarea(
        id = id,
        remoteId = remoteId,
        estado = estado,
        titulo = titulo,
        descripcion = descripcion,
        puntos = puntos,
        diaAsignada = diaAsignada,
        imgVector = imgVector,
        isPendingPost = isPendingPost,
        isPendingUpdate = isPendingUpdate
    )
}

fun Tarea.toEntity(): TareaEntity {
    return TareaEntity(
        id = id,
        remoteId = remoteId,
        estado = estado,
        titulo = titulo,
        descripcion = descripcion,
        puntos = puntos,
        diaAsignada = diaAsignada,
        imgVector = imgVector,
        isPendingPost = isPendingPost,
        isPendingUpdate = isPendingUpdate
    )
}