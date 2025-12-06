package edu.ucne.tasktally.data.local.DAOs

import androidx.room.*
import edu.ucne.tasktally.data.local.entidades.GemaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GemaDao {
    @Query("SELECT * FROM gema ORDER BY gemaId DESC")
    fun observeAll(): Flow<List<GemaEntity>>

    @Query("SELECT * FROM gema WHERE gemaId = :id")
    suspend fun getById(id: String?): GemaEntity?

    @Query("SELECT * FROM gema WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int?): GemaEntity?

    @Query("SELECT * FROM gema WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<GemaEntity>

    @Query("SELECT * FROM gema WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdate(): List<GemaEntity>

    @Query("SELECT * FROM gema WHERE isPendingDelete = 1")
    suspend fun getPendingDelete(): List<GemaEntity>

    @Upsert
    suspend fun upsert(gema: GemaEntity)

    @Delete
    suspend fun delete(gema: GemaEntity)

    @Query("DELETE FROM gema WHERE gemaId = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM gema WHERE remoteId = :remoteId")
    suspend fun deleteByRemoteId(remoteId: Int)

    @Query("UPDATE gema SET isPendingCreate = 0, remoteId = :remoteId WHERE gemaId = :localId")
    suspend fun markSynced(localId: String, remoteId: Int)

    @Query("UPDATE gema SET isPendingUpdate = 0 WHERE gemaId = :id")
    suspend fun clearUpdateFlag(id: String)

    @Query("UPDATE gema SET puntosActuales = :puntos, isPendingUpdate = 1 WHERE gemaId = :id")
    suspend fun updatePuntos(id: String, puntos: Int)
}