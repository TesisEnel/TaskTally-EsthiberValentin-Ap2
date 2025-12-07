package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "userInfo",
    indices = [
        Index(value = ["userId"], unique = true),
        Index(value = ["userName"]),
        Index(value = ["remoteId"]),
        Index(value = ["role"]),
        Index(value = ["zonaId"]),
        Index(value = ["isPendingCreate"]),
        Index(value = ["isPendingUpdate"]),
        Index(value = ["isPendingDelete"])
    ]
)
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val userInfoId: Int = 0,
    val remoteId: Int? = null,
    val userId: Int,

    val userName: String,
    val nombre: String?,
    val apellido: String?,
    val fechaNacimiento: String?,
    val email: String?,

    val role: String? = null,
    val gemaId: Int? = null,
    val mentorId: Int? = null,

    val puntosActuales: Int = 0,
    val puntosTotales: Int = 0,

    val zonaId: Int? = null,

    val isPendingCreate: Boolean = true,
    val isPendingUpdate: Boolean = false,
    val isPendingDelete: Boolean = false
)
