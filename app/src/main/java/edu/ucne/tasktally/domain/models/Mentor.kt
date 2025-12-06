package edu.ucne.tasktally.domain.models

import java.util.UUID

data class Mentor(
    val mentorId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val userId: Int? = null,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)

