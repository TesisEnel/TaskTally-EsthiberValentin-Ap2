package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "recompensa")
data class RecompensaEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val titulo: String,
    val descripcion: String,
    val precio: Double,

    val isPendingPost: Boolean = true,
    val isPendingUpdate: Boolean = false
)