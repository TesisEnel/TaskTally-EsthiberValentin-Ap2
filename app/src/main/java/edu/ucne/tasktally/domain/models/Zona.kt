package edu.ucne.tasktally.domain.models

data class Zona(
    val zonaId: Int = 0,
    val nombre: String,
    val joinCode: String,
    val mentorId: String,
    val gemas: List<Gema> = emptyList()
)
