package edu.ucne.tasktally.data.local.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tasktally.data.local.entidades.TareaMentorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {
    @Query("SELECT * FROM tareas_mentor ORDER BY tareaId DESC")
    fun observeAll(): Flow<List<TareaMentorEntity>>

    @Query("SELECT * FROM tareas_mentor WHERE tareaId = :id")
    suspend fun getById(id: String?): TareaMentorEntity?

    @Query("SELECT * FROM tareas_mentor WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int): TareaMentorEntity?

    @Query("SELECT * FROM tareas_mentor WHERE tareasGroupId = :groupId")
    fun observeByGroup(groupId: Int): Flow<List<TareaMentorEntity>>

    @Query("SELECT * FROM tareas_mentor WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<TareaMentorEntity>

    @Query("SELECT * FROM tareas_mentor WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdate(): List<TareaMentorEntity>

    @Query("SELECT * FROM tareas_mentor WHERE isPendingDelete = 1")
    suspend fun getPendingDelete(): List<TareaMentorEntity>

    @Upsert
    suspend fun upsert(tarea: TareaMentorEntity)

    @Delete
    suspend fun delete(tarea: TareaMentorEntity)

    @Query("DELETE FROM tareas_mentor WHERE tareaId = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE tareas_mentor SET isPendingCreate = 0, remoteId = :remoteId WHERE tareaId = :localId")
    suspend fun markSynced(localId: String, remoteId: Int)

    @Query("UPDATE tareas_mentor SET isPendingUpdate = 0 WHERE tareaId = :id")
    suspend fun clearUpdateFlag(id: String)

    @Query("UPDATE tareas_mentor SET isPendingDelete = 1 WHERE tareaId = :id")
    suspend fun markForDeletion(id: String)
}