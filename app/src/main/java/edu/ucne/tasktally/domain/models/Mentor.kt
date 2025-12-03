package edu.ucne.tasktally.domain.models

data class Mentor(
    val id: String = "",
    val remoteId: Int? = null,
    val userId: Int,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val isPendingPost: Boolean = true,
    val isPendingUpdate: Boolean = false
)

