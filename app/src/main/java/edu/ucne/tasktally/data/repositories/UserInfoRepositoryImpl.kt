package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.UserInfoDao
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.domain.models.UserInfo
import edu.ucne.tasktally.domain.repository.UserInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val dao: UserInfoDao
) : UserInfoRepository {

    override fun observeUsersInfo(): Flow<List<UserInfo>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getUserInfo(id: Int?): UserInfo? =
        dao.getById(id)?.toDomain()

    override suspend fun upsert(userInfo: UserInfo): Int {
        dao.upsert(userInfo.toEntity())
        return userInfo.userInfoId
    }

    override suspend fun delete(userInfo: UserInfo) {
        dao.delete(userInfo.toEntity())
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }
}