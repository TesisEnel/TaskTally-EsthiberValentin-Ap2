package edu.ucne.tasktally.data.mappers

import edu.ucne.tasktally.data.local.entidades.UserInfoEntity
import edu.ucne.tasktally.domain.models.UserInfo
import edu.ucne.tasktally.domain.models.Gema
import edu.ucne.tasktally.domain.models.Mentor
import java.util.UUID

fun UserInfoEntity.toUserInfoDomain(): UserInfo {
    return UserInfo(
        userInfoId = userInfoId,
        remoteId = remoteId,
        userId = userId,
        userName = userName,
        nombre = nombre,
        apellido = apellido,
        fechaNacimiento = fechaNacimiento,
        email = email,
        role = role,
        gemaId = gemaId,
        mentorId = mentorId,
        puntosActuales = puntosActuales,
        puntosTotales = puntosTotales,
        zonaId = zonaId,
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate,
        isPendingDelete = isPendingDelete
    )
}

fun UserInfo.toUserInfoEntity(): UserInfoEntity {
    return UserInfoEntity(
        userInfoId = userInfoId,
        remoteId = remoteId,
        userId = userId,
        userName = userName,
        nombre = nombre,
        apellido = apellido,
        fechaNacimiento = fechaNacimiento,
        email = email,
        role = role,
        gemaId = gemaId,
        mentorId = mentorId,
        puntosActuales = puntosActuales,
        puntosTotales = puntosTotales,
        zonaId = zonaId,
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate,
        isPendingDelete = isPendingDelete
    )
}

fun UserInfo.toGema(): Gema {
    return Gema(
        gemaId = gemaId?.toString() ?: UUID.randomUUID().toString(),
        remoteId = remoteId,
        nombre = nombre ?: "",
        apellido = apellido,
        fechaNacimiento = fechaNacimiento,
        puntosActuales = puntosActuales,
        zonaId = zonaId,
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate,
        isPendingDelete = isPendingDelete
    )
}

fun UserInfo.toMentor(): Mentor {
    return Mentor(
        mentorId = mentorId?.toString() ?: UUID.randomUUID().toString(),
        remoteId = remoteId,
        userId = userId,
        nombre = nombre ?: "",
        apellido = apellido ?: "",
        fechaNacimiento = fechaNacimiento ?: "",
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate,
        isPendingDelete = isPendingDelete
    )
}

fun Gema.toUserInfo(userId: Int, userName: String): UserInfo {
    return UserInfo(
        userInfoId = 0,
        remoteId = remoteId,
        userId = userId,
        userName = userName,
        nombre = nombre,
        apellido = apellido,
        fechaNacimiento = fechaNacimiento,
        email = null,
        role = "gema",
        gemaId = gemaId.toIntOrNull(),
        mentorId = null,
        puntosActuales = puntosActuales,
        puntosTotales = 0,
        zonaId = zonaId,
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate,
        isPendingDelete = isPendingDelete
    )
}

fun Mentor.toUserInfo(userName: String): UserInfo {
    return UserInfo(
        userInfoId = 0,
        remoteId = remoteId,
        userId = userId ?: 0,
        userName = userName,
        nombre = nombre,
        apellido = apellido,
        fechaNacimiento = fechaNacimiento,
        email = null,
        role = "mentor",
        gemaId = null,
        mentorId = mentorId.toIntOrNull(),
        puntosActuales = 0,
        puntosTotales = 0,
        zonaId = null,
        isPendingCreate = isPendingCreate,
        isPendingUpdate = isPendingUpdate,
        isPendingDelete = isPendingDelete
    )
}
