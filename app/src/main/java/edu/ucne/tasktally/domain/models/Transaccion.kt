package edu.ucne.tasktally.domain.models

import java.util.UUID

data class Transaccion(
    val transaccionId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val gemaId: Int? = null,
    val recompensaId: Int? = null,
    val precio: Int,
    val fecha: String,
    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)
