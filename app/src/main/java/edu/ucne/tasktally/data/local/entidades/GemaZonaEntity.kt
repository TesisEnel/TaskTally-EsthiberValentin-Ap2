package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "gemaZona",
    foreignKeys = [
        ForeignKey(
            entity = GemaEntity::class,
            parentColumns = ["gemaId"],
            childColumns = ["gemaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ZonaEntity::class,
            parentColumns = ["zonaId"],
            childColumns = ["zonaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GemaZonaEntity(
    @PrimaryKey(autoGenerate = true)
    val gemaZonaId: Int = 0,
    val gemaId: Int,
    val zonaId: Int,
    val fechaIngreso: String
)
