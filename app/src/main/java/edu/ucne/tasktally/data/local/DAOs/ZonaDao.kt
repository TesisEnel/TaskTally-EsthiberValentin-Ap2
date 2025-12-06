package edu.ucne.tasktally.data.local.DAOs

import androidx.room.*
import edu.ucne.tasktally.data.local.entidades.ZonaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ZonaDao {
    @Query("SELECT * FROM zona ORDER BY zonaId DESC")
    fun observeAll(): Flow<List<ZonaEntity>>

    @Query("SELECT * FROM zona WHERE zonaId = :id")
    suspend fun getById(id: String?): ZonaEntity?

    @Query("SELECT * FROM zona WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int?): ZonaEntity?

    @Query("SELECT * FROM zona WHERE joinCode = :joinCode")
    suspend fun getByJoinCode(joinCode: String): ZonaEntity?

    @Query("SELECT * FROM zona WHERE mentorId = :mentorId")
    fun observeByMentor(mentorId: Int): Flow<List<ZonaEntity>>

    @Query("SELECT * FROM zona WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<ZonaEntity>

    @Query("SELECT * FROM zona WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdate(): List<ZonaEntity>

    @Query("SELECT * FROM zona WHERE isPendingDelete = 1")
    suspend fun getPendingDelete(): List<ZonaEntity>

    @Upsert
    suspend fun upsert(zona: ZonaEntity)

    @Delete
    suspend fun delete(zona: ZonaEntity)

    @Query("DELETE FROM zona WHERE zonaId = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM zona WHERE remoteId = :remoteId")
    suspend fun deleteByRemoteId(remoteId: Int)

    @Query("UPDATE zona SET isPendingCreate = 0, remoteId = :remoteId WHERE zonaId = :localId")
    suspend fun markSynced(localId: String, remoteId: Int)

    @Query("UPDATE zona SET isPendingUpdate = 0 WHERE zonaId = :id")
    suspend fun clearUpdateFlag(id: String)

    @Query("UPDATE zona SET zonaName = :name, isPendingUpdate = 1 WHERE zonaId = :id")
    suspend fun updateName(id: String, name: String)
}