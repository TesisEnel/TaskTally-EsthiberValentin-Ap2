package edu.ucne.tasktally.data.local.entidades.gemas

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

    val titulo: String,
    val descripcion: String,
    val estado: String,
    val puntos: Int,
    val dia: String? = null,
    val nombreImgVector: String? = null,
    val gemaCompletoNombre: String? = null,

    val isPendingCreate: Boolean = false,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)