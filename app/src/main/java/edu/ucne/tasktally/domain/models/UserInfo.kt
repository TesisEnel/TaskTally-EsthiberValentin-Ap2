package edu.ucne.tasktally.domain.models

data class UserInfo(
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
