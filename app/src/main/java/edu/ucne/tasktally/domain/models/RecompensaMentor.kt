package edu.ucne.tasktally.domain.models

import java.util.UUID

data class RecompensaMentor(
    val recompensaId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val createdBy: Int? = null,
    val titulo: String,
    val descripcion: String,
    val precio: Int,
    val isDisponible: Boolean = true,
    val fechaCreacion: String? = null,
    val createdByName: String? = null,
    val totalCanjes: Int = 0,
    val nombreImgVector: String? = null,
    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)
