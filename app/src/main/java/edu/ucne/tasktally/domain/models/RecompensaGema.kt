package edu.ucne.tasktally.domain.models

import java.util.UUID

data class RecompensaGema(
    val recompensaId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val titulo: String,
    val descripcion: String,
    val precio: Int,
    val nombreImgVector: String? = null,
    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)
