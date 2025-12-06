package edu.ucne.tasktally.data.local.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tasktally.data.local.entidades.TareaGemaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaGemaDao {
    @Query("SELECT * FROM tareas_gema ORDER BY tareaId DESC")
    fun observeAll(): Flow<List<TareaGemaEntity>>

    @Query("SELECT * FROM tareas_gema WHERE tareaId = :id")
    suspend fun getById(id: String?): TareaGemaEntity?

    @Query("SELECT * FROM tareas_gema WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int): TareaGemaEntity?

    @Query("SELECT * FROM tareas_gema WHERE estado = :estado")
    fun observeByEstado(estado: String): Flow<List<TareaGemaEntity>>

    @Query("SELECT * FROM tareas_gema WHERE gemaId = :gemaId")
    fun observeByGema(gemaId: Int): Flow<List<TareaGemaEntity>>

    @Query("SELECT * FROM tareas_gema WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<TareaGemaEntity>

    @Query("SELECT * FROM tareas_gema WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdate(): List<TareaGemaEntity>

    @Query("SELECT * FROM tareas_gema WHERE isPendingDelete = 1")
    suspend fun getPendingDelete(): List<TareaGemaEntity>

    @Upsert
    suspend fun upsert(tarea: TareaGemaEntity)

    @Delete
    suspend fun delete(tarea: TareaGemaEntity)

    @Query("DELETE FROM tareas_gema WHERE tareaId = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE tareas_gema SET isPendingCreate = 0, remoteId = :remoteId WHERE tareaId = :localId")
    suspend fun markSynced(localId: String, remoteId: Int)

    @Query("UPDATE tareas_gema SET isPendingUpdate = 0 WHERE tareaId = :id")
    suspend fun clearUpdateFlag(id: String)

    @Query("UPDATE tareas_gema SET estado = :estado, isPendingUpdate = 1 WHERE tareaId = :id")
    suspend fun updateEstado(id: String, estado: String)
}
