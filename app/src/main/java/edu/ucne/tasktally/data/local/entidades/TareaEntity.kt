package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tareas")
data class TareaEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,

    val estado: String,
    val titulo: String,
    val descripcion: String,
    val puntos: Double,

    val diaAsignada: String?,
    val imgVector: String?,

    val isPendingPost: Boolean = true,
    val isPendingUpdate: Boolean = false
)
