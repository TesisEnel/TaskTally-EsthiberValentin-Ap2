package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaccionRecompensa",
    foreignKeys = [
        ForeignKey(
            entity = GemaEntity::class,
            parentColumns = ["gemaId"],
            childColumns = ["gemaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = RecompensaEntity::class,
            parentColumns = ["recompensaId"],
            childColumns = ["recompensaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransaccionRecompensaEntity(
    @PrimaryKey(autoGenerate = true)
    val transaccionRecompensaId: Int = 0,
    val gemaId: Int,
    val recompensaId: Int,
    val costoPuntos: Double,
    val fechaTransaccion: String
)
