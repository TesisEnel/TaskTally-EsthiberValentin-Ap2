package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "transaccion",
    indices = [
        Index(value = ["remoteId"]),
        Index(value = ["isPendingCreate"]),
        Index(value = ["isPendingUpdate"]),
        Index(value = ["isPendingDelete"])
    ]
)
data class TransaccionEntity(
    @PrimaryKey val transaccionId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,

    val gemaId: Int? = null,
    val recompensaId: Int? = null,
    val precio: Int,
    val fecha: String,

    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)
