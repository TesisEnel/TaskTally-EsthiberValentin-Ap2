package edu.ucne.tasktally.domain.models

import java.util.UUID

data class Gema(
    val gemaId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombre: String,
    val apellido: String? = null,
    val fechaNacimiento: String? = null,
    val puntosActuales: Int = 0,
    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)
