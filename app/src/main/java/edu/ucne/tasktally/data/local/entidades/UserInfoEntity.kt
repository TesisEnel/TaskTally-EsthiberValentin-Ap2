package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userInfo")
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val userInfoId: Int = 0,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String
)
