package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "tareas_gema",
    indices = [
        Index(value = ["remoteId"]),
        Index(value = ["isPendingCreate"]),
        Index(value = ["isPendingUpdate"]),
        Index(value = ["isPendingDelete"])
    ]
)
data class TareaGemaEntity(
    @PrimaryKey val tareaId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val gemaId: Int? = null,
    val titulo: String,
    val estado: String,
    val puntos: Int,
    val descripcion: String,
    val nombreImgVector: String? = null,
    val dia: String? = null,
    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)
