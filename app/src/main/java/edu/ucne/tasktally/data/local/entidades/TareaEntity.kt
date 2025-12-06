package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "tareas_mentor",
    indices = [
        Index(value = ["remoteId"]),
        Index(value = ["isPendingCreate"]),
        Index(value = ["isPendingUpdate"]),
        Index(value = ["isPendingDelete"])
    ]
)
data class TareaMentorEntity(
    @PrimaryKey val tareaId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val tareasGroupId: Int? = null,
    val mentorId: Int? = null,
    val titulo: String,
    val descripcion: String,
    val puntos: Int,
    val recurrente: Boolean = false,
    val dias: String? = null,
    val nombreImgVector: String? = null,
    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)

