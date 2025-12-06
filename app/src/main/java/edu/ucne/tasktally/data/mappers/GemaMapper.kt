package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.GemaEntity
import edu.ucne.tasktally.domain.models.Gema

fun GemaEntity.toDomain() = Gema(
    gemaId = gemaId,
    remoteId = remoteId,
    nombre = nombre,
    apellido = apellido,
    fechaNacimiento = fechaNacimiento,
    puntosActuales = puntosActuales,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)

fun Gema.toEntity() = GemaEntity(
    gemaId = gemaId,
    remoteId = remoteId,
    nombre = nombre,
    apellido = apellido,
    fechaNacimiento = fechaNacimiento,
    puntosActuales = puntosActuales,
    isPendingCreate = isPendingCreate,
    isPendingUpdate = isPendingUpdate,
    isPendingDelete = isPendingDelete
)