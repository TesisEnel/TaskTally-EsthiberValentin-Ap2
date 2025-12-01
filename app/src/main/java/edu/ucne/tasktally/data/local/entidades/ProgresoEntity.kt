package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "progreso",
    foreignKeys = [
        ForeignKey(
            entity = GemaEntity::class,
            parentColumns = ["gemaId"],
            childColumns = ["gemaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TareaEntity::class,
            parentColumns = ["tareaId"],
            childColumns = ["tareaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EstadoEntity::class,
            parentColumns = ["estadoId"],
            childColumns = ["estadoId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class ProgresoEntity(
    @PrimaryKey(autoGenerate = true)
    val progresoId: Int = 0,
    val gemaId: Int,
    val tareaId: Int,
    val estadoId: Int?,
    val fechaAsignacion: String,
    val fechaCompletada: String?,
    val puntosGanados: Double
)
