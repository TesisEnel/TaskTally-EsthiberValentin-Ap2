package edu.ucne.tasktally.domain.models

data class UserInfo(
    val userInfoId: Int = 0,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String
)
