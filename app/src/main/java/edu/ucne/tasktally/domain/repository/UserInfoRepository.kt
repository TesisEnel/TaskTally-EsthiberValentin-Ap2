package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    fun observeUsersInfo(): Flow<List<UserInfo>>
    suspend fun getUserInfo(id: Int?): UserInfo?
    suspend fun upsert(userInfo: UserInfo): Int
    suspend fun delete(userInfo: UserInfo)
    suspend fun deleteById(id: Int)
}