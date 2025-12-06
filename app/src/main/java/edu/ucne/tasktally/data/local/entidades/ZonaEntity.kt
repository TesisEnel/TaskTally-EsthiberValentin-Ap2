package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "zona",
    indices = [
        Index(value = ["remoteId"]),
        Index(value = ["isPendingCreate"]),
        Index(value = ["isPendingUpdate"]),
        Index(value = ["isPendingDelete"])
    ]
)
data class ZonaEntity(
    @PrimaryKey val zonaId: String = UUID.randomUUID().toString(),
    val remoteId: Int? = null,
    val joinCode: String,
    val mentorId: Int? = null,
    val zonaName: String,
    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)
