package edu.ucne.tasktally.data.local.DAOs

import androidx.room.*
import edu.ucne.tasktally.data.local.entidades.MentorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MentorDao {
    @Query("SELECT * FROM mentor ORDER BY mentorId DESC")
    fun observeAll(): Flow<List<MentorEntity>>

    @Query("SELECT * FROM mentor WHERE mentorId = :id")
    suspend fun getById(id: String?): MentorEntity?

    @Query("SELECT * FROM mentor WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int?): MentorEntity?

    @Query("SELECT * FROM mentor WHERE userId = :userId")
    suspend fun getByUserId(userId: Int): MentorEntity?

    @Query("SELECT * FROM mentor WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<MentorEntity>

    @Query("SELECT * FROM mentor WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdate(): List<MentorEntity>

    @Query("SELECT * FROM mentor WHERE isPendingDelete = 1")
    suspend fun getPendingDelete(): List<MentorEntity>

    @Upsert
    suspend fun upsert(mentor: MentorEntity)

    @Delete
    suspend fun delete(mentor: MentorEntity)

    @Query("DELETE FROM mentor WHERE mentorId = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE mentor SET isPendingCreate = 0, remoteId = :remoteId WHERE mentorId = :localId")
    suspend fun markSynced(localId: String, remoteId: Int)

    @Query("UPDATE mentor SET isPendingUpdate = 0 WHERE mentorId = :id")
    suspend fun clearUpdateFlag(id: String)

    @Query("UPDATE mentor SET isPendingDelete = 1 WHERE mentorId = :id")
    suspend fun markForDeletion(id: String)
}