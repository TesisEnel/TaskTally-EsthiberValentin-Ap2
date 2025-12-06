package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.TareaGemaEntity
import edu.ucne.tasktally.domain.models.TareaGema

fun TareaGemaEntity.toDomain() = TareaGema(
    tareaId = tareaId,
    remoteId = remoteId,
    gemaId = gemaId,
    titulo = titulo,
    estado = estado,
    puntos = puntos,
    descripcion = descripcion,
    nombreImgVector = nombreImgVector,
    dia = dia,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun TareaGema.toEntity() = TareaGemaEntity(
    tareaId = tareaId,
    remoteId = remoteId,
    gemaId = gemaId,
    titulo = titulo,
    estado = estado,
    puntos = puntos,
    descripcion = descripcion,
    nombreImgVector = nombreImgVector,
    dia = dia,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)


