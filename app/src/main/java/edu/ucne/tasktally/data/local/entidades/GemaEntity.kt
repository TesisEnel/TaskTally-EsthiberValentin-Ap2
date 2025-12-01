package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "gema",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["remoteId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GemaEntity(
    @PrimaryKey(autoGenerate = true)
    val gemaId: Int = 0,
    val userId: Int,
    val userInfoId: Int?,
    val puntosActuales: Double,
    val puntosTotales: Double
)