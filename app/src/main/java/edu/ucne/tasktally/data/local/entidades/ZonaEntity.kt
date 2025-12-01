package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "zona",
    foreignKeys = [
        ForeignKey(
            entity = MentorEntity::class,
            parentColumns = ["mentorId"],
            childColumns = ["mentorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ZonaEntity(
    @PrimaryKey(autoGenerate = true)
    val zonaId: Int = 0,
    val mentorId: Int,
    val zonaName: String
)
