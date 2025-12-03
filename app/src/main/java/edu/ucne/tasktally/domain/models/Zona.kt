package edu.ucne.tasktally.domain.models

data class Zona(
    val id: String = "",
    val remoteId: Int? = null,
    val joinCode: String,
    val mentorId: Int,
    val zonaName: String,
    val isPendingPost: Boolean = true,
    val isPendingUpdate: Boolean = false
)
