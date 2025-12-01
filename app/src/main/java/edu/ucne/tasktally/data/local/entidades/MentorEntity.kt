package edu.ucne.tasktally.data.local.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "mentor",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["remoteId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserInfoEntity::class,
            parentColumns = ["userInfoId"],
            childColumns = ["userInfoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MentorEntity(
    @PrimaryKey(autoGenerate = true)
    val mentorId: Int = 0,
    val userId: Int,
    val userInfoId: Int
)
