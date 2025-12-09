package edu.ucne.tasktally.data.local.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tasktally.data.local.entidades.UserInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDao {
    @Query("SELECT * FROM userInfo WHERE isPendingDelete = 0 ORDER BY userInfoId DESC")
    fun observeAll(): Flow<List<UserInfoEntity>>

    @Query("SELECT * FROM userInfo WHERE userInfoId = :id AND isPendingDelete = 0")
    suspend fun getById(id: Int?): UserInfoEntity?

    @Query("SELECT * FROM userInfo WHERE userId = :userId AND isPendingDelete = 0")
    suspend fun getByUserId(userId: Int): UserInfoEntity?

    @Query("SELECT * FROM userInfo WHERE userName = :userName AND isPendingDelete = 0")
    suspend fun getByUserName(userName: String): UserInfoEntity?

    @Query("SELECT * FROM userInfo WHERE remoteId = :remoteId AND isPendingDelete = 0")
    suspend fun getByRemoteId(remoteId: Int?): UserInfoEntity?

    @Query("SELECT * FROM userInfo WHERE role = :role AND isPendingDelete = 0")
    fun observeByRole(role: String): Flow<List<UserInfoEntity>>

    @Query("SELECT * FROM userInfo WHERE role = :role AND isPendingDelete = 0")
    suspend fun getByRole(role: String): List<UserInfoEntity>

    @Query("SELECT * FROM userInfo WHERE zonaId = :zonaId AND isPendingDelete = 0")
    suspend fun getByZonaId(zonaId: Int): List<UserInfoEntity>

    @Query("SELECT * FROM userInfo WHERE zonaId = :zonaId AND role = :role AND isPendingDelete = 0")
    suspend fun getByZonaIdAndRole(zonaId: Int, role: String): List<UserInfoEntity>

    @Query("SELECT * FROM userInfo WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<UserInfoEntity>

    @Query("SELECT * FROM userInfo WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdate(): List<UserInfoEntity>

    @Query("SELECT * FROM userInfo WHERE isPendingDelete = 1")
    suspend fun getPendingDelete(): List<UserInfoEntity>

    @Query("UPDATE userInfo SET isPendingCreate = 0, remoteId = :remoteId WHERE userInfoId = :localId")
    suspend fun markSynced(localId: Int, remoteId: Int)

    @Query("UPDATE userInfo SET isPendingUpdate = 0 WHERE userInfoId = :id")
    suspend fun clearUpdateFlag(id: Int)

    @Query("UPDATE userInfo SET isPendingDelete = 1 WHERE userInfoId = :id")
    suspend fun markForDeletion(id: Int)

    @Query("UPDATE userInfo SET puntosActuales = :puntos, isPendingUpdate = 1 WHERE userInfoId = :id")
    suspend fun updatePuntos(id: Int, puntos: Int)

    @Query("UPDATE userInfo SET puntosActuales = puntosActuales + :puntos, isPendingUpdate = 1 WHERE userInfoId = :id")
    suspend fun addPuntos(id: Int, puntos: Int)

    @Upsert
    suspend fun upsert(userInfo: UserInfoEntity)

    @Delete
    suspend fun delete(userInfo: UserInfoEntity)

    @Query("DELETE FROM userInfo WHERE userInfoId = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM userInfo WHERE remoteId = :remoteId")
    suspend fun deleteByRemoteId(remoteId: Int)
}
