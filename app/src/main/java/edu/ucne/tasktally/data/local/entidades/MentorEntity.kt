package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "mentor")
data class MentorEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val userId: Int,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,

    val isPendingPost: Boolean = true,
    val isPendingUpdate: Boolean = false
)
