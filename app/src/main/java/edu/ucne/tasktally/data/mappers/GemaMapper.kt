package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.GemaEntity
import edu.ucne.tasktally.domain.models.Gema

fun GemaEntity.toDomain() = Gema(
    id = id,
    remoteId = remoteId,
    nombre = nombre,
    apellido = apellido,
    fechaNacimiento = fechaNacimiento,
    puntosActuales = puntosActuales,
    isPendingPost = isPendingPost,
    isPendingUpdate = isPendingUpdate
)

fun Gema.toEntity() = GemaEntity(
    id = id,
    remoteId = remoteId,
    nombre = nombre,
    apellido = apellido,
    fechaNacimiento = fechaNacimiento,
    puntosActuales = puntosActuales,
    isPendingPost = isPendingPost,
    isPendingUpdate = isPendingUpdate
)