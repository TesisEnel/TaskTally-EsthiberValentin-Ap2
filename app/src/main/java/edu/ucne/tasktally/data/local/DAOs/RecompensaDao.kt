package edu.ucne.tasktally.data.local.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tasktally.data.local.entidades.RecompensaMentorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecompensaDao {
    @Query("SELECT * FROM recompensas_mentor ORDER BY recompensaId DESC")
    fun observeAll(): Flow<List<RecompensaMentorEntity>>

    @Query("SELECT * FROM recompensas_mentor WHERE recompensaId = :id")
    suspend fun getById(id: String?): RecompensaMentorEntity?

    @Query("SELECT * FROM recompensas_mentor WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int?): RecompensaMentorEntity?

    @Query("SELECT * FROM recompensas_mentor WHERE createdBy = :mentorId")
    fun observeByMentor(mentorId: Int): Flow<List<RecompensaMentorEntity>>

    @Query("SELECT * FROM recompensas_mentor WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<RecompensaMentorEntity>

    @Query("SELECT * FROM recompensas_mentor WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdate(): List<RecompensaMentorEntity>

    @Query("SELECT * FROM recompensas_mentor WHERE isPendingDelete = 1")
    suspend fun getPendingDelete(): List<RecompensaMentorEntity>

    @Upsert
    suspend fun upsert(recompensa: RecompensaMentorEntity)

    @Delete
    suspend fun delete(recompensa: RecompensaMentorEntity)

    @Query("DELETE FROM recompensas_mentor WHERE recompensaId = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM recompensas_mentor WHERE remoteId = :remoteId")
    suspend fun deleteByRemoteId(remoteId: Int)

    @Query("UPDATE recompensas_mentor SET isPendingCreate = 0, remoteId = :remoteId WHERE recompensaId = :localId")
    suspend fun markSynced(localId: String, remoteId: Int)

    @Query("UPDATE recompensas_mentor SET isPendingUpdate = 0 WHERE recompensaId = :id")
    suspend fun clearUpdateFlag(id: String)

    @Query("UPDATE recompensas_mentor SET isPendingDelete = 1 WHERE recompensaId = :id")
    suspend fun markForDeletion(id: String)
}