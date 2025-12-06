package edu.ucne.tasktally.domain.models

import java.util.UUID

data class Zona(
    val zonaId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val joinCode: String,
    val mentorId: Int? = null,
    val zonaName: String,
    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)
