package edu.ucne.tasktally.domain.models

data class Usuario(
    val userId: Int = 0,
    val userName: String,
    val password: String,
    val email: String
)
