package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    fun observeUserInfos(): Flow<List<UserInfo>>
    suspend fun getUserInfo(id: Int?): UserInfo?
    suspend fun getUserInfoByUserId(userId: Int): UserInfo?
    suspend fun getUserInfoByUserName(userName: String): UserInfo?
    suspend fun getUserInfoByRemoteId(remoteId: Int?): UserInfo?

    fun observeByRole(role: String): Flow<List<UserInfo>>
    suspend fun getUsersByRole(role: String): List<UserInfo>

    fun observeGemas(): Flow<List<UserInfo>>
    suspend fun getGemas(): List<UserInfo>
    suspend fun getGemasByZona(zonaId: Int): List<UserInfo>

    fun observeMentors(): Flow<List<UserInfo>>
    suspend fun getMentors(): List<UserInfo>

    suspend fun getUsersByZona(zonaId: Int): List<UserInfo>

    suspend fun getPendingCreate(): List<UserInfo>
    suspend fun getPendingUpdate(): List<UserInfo>
    suspend fun getPendingDelete(): List<UserInfo>
    suspend fun markSynced(localId: Int, remoteId: Int)
    suspend fun clearUpdateFlag(id: Int)
    suspend fun markForDeletion(id: Int)

    suspend fun updatePuntos(id: Int, puntos: Int)
    suspend fun addPuntos(id: Int, puntos: Int)

    suspend fun upsert(userInfo: UserInfo): Int
    suspend fun delete(userInfo: UserInfo)
    suspend fun deleteById(id: Int)
    suspend fun deleteByRemoteId(remoteId: Int)
}
