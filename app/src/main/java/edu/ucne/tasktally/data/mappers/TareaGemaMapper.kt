package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.gemas.TareaGemaEntity
import edu.ucne.tasktally.data.remote.DTOs.gema.tarea.TareasGemaResponse
import edu.ucne.tasktally.domain.models.TareaGema

fun TareaGemaEntity.toTareaGemaDomain() = TareaGema(
    tareaId = tareaId,
    remoteId = remoteId,
    titulo = titulo,
    estado = estado,
    puntos = puntos,
    descripcion = descripcion,
    nombreImgVector = nombreImgVector,
    dia = dia,
    gemaCompletoNombre = gemaCompletoNombre,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun TareaGema.toTareaGemaEntity() = TareaGemaEntity(
    tareaId = tareaId,
    remoteId = remoteId,
    titulo = titulo,
    descripcion = descripcion,
    estado = estado,
    puntos = puntos,
    dia = dia,
    gemaCompletoNombre = gemaCompletoNombre,
    nombreImgVector = nombreImgVector,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun TareasGemaResponse.toTareaGemaEntity() = TareaGemaEntity(
    remoteId = tareaId,
    titulo = titulo,
    descripcion = descripcion,
    estado = estado,
    puntos = puntos,
    dia = dia,
    nombreImgVector = nombreImgVector,
    gemaCompletoNombre = gemaCompletoNombre
)


