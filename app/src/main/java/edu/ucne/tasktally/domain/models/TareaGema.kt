package edu.ucne.tasktally.domain.models

import java.util.UUID

data class TareaGema(
    val tareaId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,

    val titulo: String,
    val estado: String,
    val puntos: Int,
    val descripcion: String,
    val nombreImgVector: String? = null,
    val dia: String? = null,
    val gemaCompletoNombre: String? = null,

    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)

