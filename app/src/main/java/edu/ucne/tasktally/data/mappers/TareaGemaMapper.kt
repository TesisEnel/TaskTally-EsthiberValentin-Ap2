package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.TareaGemaEntity
import edu.ucne.tasktally.domain.models.TareaGema

fun TareaGemaEntity.toTareaGemaDomain() = TareaGema(
    tareaId = tareaId,
    remoteId = remoteId,
    gemaId = userInfoId,
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

fun TareaGema.toTareaGemaEntity() = TareaGemaEntity(
    tareaId = tareaId,
    remoteId = remoteId,
    userInfoId = gemaId,
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


