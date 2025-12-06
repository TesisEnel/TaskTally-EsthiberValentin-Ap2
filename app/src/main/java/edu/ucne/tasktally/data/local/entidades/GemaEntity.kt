package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "gema",
    indices = [
        Index(value = ["remoteId"]),
        Index(value = ["isPendingCreate"]),
        Index(value = ["isPendingUpdate"]),
        Index(value = ["isPendingDelete"])
    ]
)
data class GemaEntity(
    @PrimaryKey val gemaId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val nombre: String,
    val apellido: String? = null,
    val fechaNacimiento: String? = null,
    val puntosActuales: Int = 0,
    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)