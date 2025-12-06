package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "mentor",
    indices = [
        Index(value = ["remoteId"]),
        Index(value = ["isPendingCreate"]),
        Index(value = ["isPendingUpdate"]),
        Index(value = ["isPendingDelete"])
    ]
)
data class MentorEntity(
    @PrimaryKey val mentorId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val userId: Int? = null,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)
