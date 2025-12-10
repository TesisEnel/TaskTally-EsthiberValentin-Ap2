package edu.ucne.tasktally.data.local.DAOs.gema

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tasktally.data.local.entidades.gemas.RecompensaGemaEntity
import edu.ucne.tasktally.data.local.entidades.gemas.TareaGemaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GemaDao {
    //region TareaGema
    @Query("SELECT * FROM tareas_gema WHERE isPendingDelete = 0 ORDER BY tareaId DESC")
    fun observeTodasLasTareas(): Flow<List<TareaGemaEntity>>

    @Query("SELECT * FROM tareas_gema WHERE tareaId = :id AND isPendingDelete = 0")
    suspend fun obtenerTareaPorId(id: String): TareaGemaEntity?

    @Query("UPDATE tareas_gema SET isPendingUpdate = 1, estado = 'iniciada' WHERE tareaId = :id AND isPendingDelete = 0")
    suspend fun iniciarTarea(id: String)

    @Query("UPDATE tareas_gema SET isPendingUpdate = 1, estado = 'completada' WHERE tareaId = :id AND isPendingDelete = 0")
    suspend fun completarTarea(id: String)

    @Query("UPDATE tareas_gema SET puntos = puntos + :puntos WHERE tareaId = :id")
    suspend fun darPuntos(id: String, puntos: Int)

    @Upsert
    suspend fun upsertTarea(tarea: TareaGemaEntity)

    @Delete
    suspend fun eliminarTarea(tarea: TareaGemaEntity)

    @Query("DELETE FROM tareas_gema")
    suspend fun eliminarTodasLasTareas()

    @Query("SELECT * FROM tareas_gema WHERE isPendingCreate = 1")
    suspend fun getPendingCreateTareas(): List<TareaGemaEntity>

    @Query("SELECT * FROM tareas_gema WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdateTareas(): List<TareaGemaEntity>

    @Query("SELECT * FROM tareas_gema WHERE isPendingDelete = 1")
    suspend fun getPendingDeleteTareas(): List<TareaGemaEntity>
    //endregion

    //region RecompensaGema
    @Query("SELECT * FROM recompensas_gema WHERE isPendingDelete = 0 ORDER BY recompensaId DESC")
    fun observeTodasLasRecompensas(): Flow<List<RecompensaGemaEntity>>

    @Query("SELECT * FROM recompensas_gema WHERE recompensaId = :id AND isPendingDelete = 0")
    suspend fun obtenerRecompensaPorId(id: String): RecompensaGemaEntity?

    @Upsert
    suspend fun upsertRecompensa(recompensa: RecompensaGemaEntity)

    @Delete
    suspend fun eliminarRecompensa(recompensa: RecompensaGemaEntity)

    @Query("DELETE FROM recompensas_gema")
    suspend fun eliminarTodasLasRecompensas()

    @Query("SELECT * FROM recompensas_gema WHERE isPendingCreate = 1")
    suspend fun getPendingCreateRecompensas(): List<RecompensaGemaEntity>

    @Query("SELECT * FROM recompensas_gema WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdateRecompensas(): List<RecompensaGemaEntity>

    @Query("SELECT * FROM recompensas_gema WHERE isPendingDelete = 1")
    suspend fun getPendingDeleteRecompensas(): List<RecompensaGemaEntity>
    //endregion
}