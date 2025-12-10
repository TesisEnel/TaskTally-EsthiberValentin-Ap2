package edu.ucne.tasktally.domain.models

import java.util.UUID

data class TareaMentor(
    val tareaId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,

    val titulo: String,
    val descripcion: String,
    val puntos: Int,
    val repetir: Int,
    val dias: String? = null,
    val nombreImgVector: String? = null,

    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)
