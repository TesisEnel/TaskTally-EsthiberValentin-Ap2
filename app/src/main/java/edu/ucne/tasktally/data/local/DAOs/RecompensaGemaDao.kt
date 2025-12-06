package edu.ucne.tasktally.data.local.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tasktally.data.local.entidades.RecompensaGemaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecompensaGemaDao {
    @Query("SELECT * FROM recompensas_gema ORDER BY recompensaId DESC")
    fun observeAll(): Flow<List<RecompensaGemaEntity>>

    @Query("SELECT * FROM recompensas_gema WHERE recompensaId = :id")
    suspend fun getById(id: String?): RecompensaGemaEntity?

    @Query("SELECT * FROM recompensas_gema WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int?): RecompensaGemaEntity?

    @Query("SELECT * FROM recompensas_gema WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<RecompensaGemaEntity>

    @Query("SELECT * FROM recompensas_gema WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdate(): List<RecompensaGemaEntity>

    @Query("SELECT * FROM recompensas_gema WHERE isPendingDelete = 1")
    suspend fun getPendingDelete(): List<RecompensaGemaEntity>

    @Upsert
    suspend fun upsert(recompensa: RecompensaGemaEntity)

    @Delete
    suspend fun delete(recompensa: RecompensaGemaEntity)

    @Query("DELETE FROM recompensas_gema WHERE recompensaId = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE recompensas_gema SET isPendingCreate = 0, remoteId = :remoteId WHERE recompensaId = :localId")
    suspend fun markSynced(localId: String, remoteId: Int)

    @Query("UPDATE recompensas_gema SET isPendingUpdate = 0 WHERE recompensaId = :id")
    suspend fun clearUpdateFlag(id: String)
}
