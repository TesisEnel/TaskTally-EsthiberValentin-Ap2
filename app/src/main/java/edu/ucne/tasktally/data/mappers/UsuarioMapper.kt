package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.UsuarioEntity

fun UsuarioEntity.toDomain() = Usuario(
    userId = userId,
    userName = userName,
    password = password,
    email = email
)

fun Usuario.toEntity() = UsuarioEntity(
    userId = userId,
    userName = userName,
    password = password,
    email = email
)
