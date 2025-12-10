package edu.ucne.tasktally.data.local.entidades.gemas

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "recompensas_gema",
    indices = [
        Index(value = ["remoteId"]),
        Index(value = ["isPendingCreate"]),
        Index(value = ["isPendingUpdate"]),
        Index(value = ["isPendingDelete"])
    ]
)
data class RecompensaGemaEntity(
    @PrimaryKey val recompensaId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,

    val titulo: String,
    val descripcion: String,
    val precio: Int,
    val nombreImgVector: String? = null,
    val transaccionId: Int? = null,
    val canjeada: Boolean = false,

    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)