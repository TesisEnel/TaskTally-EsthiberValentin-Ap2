package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "zona")
data class ZonaEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val joinCode: String,
    val mentorId: Int,
    val zonaName: String,

    val isPendingPost: Boolean = true,
    val isPendingUpdate: Boolean = false
)
