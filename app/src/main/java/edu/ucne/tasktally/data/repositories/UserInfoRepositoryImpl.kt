package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.UserInfoDao
import edu.ucne.tasktally.data.mappers.toUserInfoDomain
import edu.ucne.tasktally.data.mappers.toUserInfoEntity
import edu.ucne.tasktally.domain.models.UserInfo
import edu.ucne.tasktally.domain.repository.UserInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserInfoRepositoryImpl @Inject constructor(
    private val dao: UserInfoDao
) : UserInfoRepository {

    override fun observeUserInfos(): Flow<List<UserInfo>> =
        dao.observeAll().map { list -> list.map { it.toUserInfoDomain() } }

    override suspend fun getUserInfo(id: Int?): UserInfo? =
        dao.getById(id)?.toUserInfoDomain()

    override suspend fun getUserInfoByUserId(userId: Int): UserInfo? =
        dao.getByUserId(userId)?.toUserInfoDomain()

    override suspend fun getUserInfoByUserName(userName: String): UserInfo? =
        dao.getByUserName(userName)?.toUserInfoDomain()

    override suspend fun getUserInfoByRemoteId(remoteId: Int?): UserInfo? =
        dao.getByRemoteId(remoteId)?.toUserInfoDomain()

    override fun observeByRole(role: String): Flow<List<UserInfo>> =
        dao.observeByRole(role).map { list -> list.map { it.toUserInfoDomain() } }

    override suspend fun getUsersByRole(role: String): List<UserInfo> =
        dao.getByRole(role).map { it.toUserInfoDomain() }

    // Gema-specific operations
    override fun observeGemas(): Flow<List<UserInfo>> = observeByRole("gema")

    override suspend fun getGemas(): List<UserInfo> = getUsersByRole("gema")

    override suspend fun getGemasByZona(zonaId: Int): List<UserInfo> =
        dao.getByZonaIdAndRole(zonaId, "gema").map { it.toUserInfoDomain() }

    override fun observeMentors(): Flow<List<UserInfo>> = observeByRole("mentor")

    override suspend fun getMentors(): List<UserInfo> = getUsersByRole("mentor")

    override suspend fun getUsersByZona(zonaId: Int): List<UserInfo> =
        dao.getByZonaId(zonaId).map { it.toUserInfoDomain() }

    override suspend fun getPendingCreate(): List<UserInfo> =
        dao.getPendingCreate().map { it.toUserInfoDomain() }

    override suspend fun getPendingUpdate(): List<UserInfo> =
        dao.getPendingUpdate().map { it.toUserInfoDomain() }

    override suspend fun getPendingDelete(): List<UserInfo> =
        dao.getPendingDelete().map { it.toUserInfoDomain() }

    override suspend fun markSynced(localId: Int, remoteId: Int) =
        dao.markSynced(localId, remoteId)

    override suspend fun clearUpdateFlag(id: Int) =
        dao.clearUpdateFlag(id)

    override suspend fun markForDeletion(id: Int) =
        dao.markForDeletion(id)

    override suspend fun updatePuntos(id: Int, puntos: Int) =
        dao.updatePuntos(id, puntos)

    override suspend fun addPuntos(id: Int, puntos: Int) =
        dao.addPuntos(id, puntos)

    override suspend fun upsert(userInfo: UserInfo): Int {
        dao.upsert(userInfo.toUserInfoEntity())
        return userInfo.userInfoId
    }

    override suspend fun delete(userInfo: UserInfo) {
        dao.delete(userInfo.toUserInfoEntity())
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }

    override suspend fun deleteByRemoteId(remoteId: Int) =
        dao.deleteByRemoteId(remoteId)
}
