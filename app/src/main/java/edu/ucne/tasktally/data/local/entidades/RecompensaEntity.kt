package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "recompensas_mentor",
    indices = [
        Index(value = ["remoteId"]),
        Index(value = ["isPendingCreate"]),
        Index(value = ["isPendingUpdate"]),
        Index(value = ["isPendingDelete"])
    ]
)
data class RecompensaMentorEntity(
    @PrimaryKey val recompensaId: String = UUID.randomUUID().toString(),
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