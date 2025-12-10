package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "zonas",
    indices = [
        Index(value = ["mentorId"]),
        Index(value = ["joinCode"], unique = true)
    ]
)
data class ZonaEntity(
    @PrimaryKey(autoGenerate = true)
    val zonaId: Int = 0,
    val nombre: String,
    val joinCode: String,
    val mentorId: String
)
