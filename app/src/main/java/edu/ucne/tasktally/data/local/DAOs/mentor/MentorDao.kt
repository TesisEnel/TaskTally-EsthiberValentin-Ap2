package edu.ucne.tasktally.data.local.DAOs.mentor

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tasktally.data.local.entidades.mentor.TareaMentorEntity
import edu.ucne.tasktally.data.local.entidades.mentor.RecompensaMentorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MentorDao {
    //region Tareas
    @Query("SELECT * FROM tareas_mentor WHERE isPendingDelete = 0 ORDER BY tareaId DESC")
    fun observeTodasLasTareasMentor(): Flow<List<TareaMentorEntity>>

    @Query("SELECT * FROM tareas_mentor ORDER BY tareaId DESC")
    fun observeAllTareasMentor(): Flow<List<TareaMentorEntity>> // TODO ELIMINAR ES REDUNDANTE

    @Query("SELECT * FROM tareas_mentor WHERE tareaId = :id AND isPendingDelete = 0")
    suspend fun obtenerTareaMentorPorId(id: String?): TareaMentorEntity?

    @Upsert
    suspend fun upsertTarea(tarea: TareaMentorEntity)

    @Delete
    suspend fun eliminarTarea(tarea: TareaMentorEntity)

    @Query("DELETE FROM tareas_mentor WHERE tareaId = :id")
    suspend fun eliminarTareaPorId(id: String)

    @Query("DELETE FROM tareas_mentor")
    fun eliminarTodasLasTareas()

    @Query("SELECT * FROM tareas_mentor WHERE isPendingCreate = 1")
    suspend fun obtenerTareasPendientesDeCrear(): List<TareaMentorEntity>

    @Query("SELECT * FROM tareas_mentor WHERE isPendingUpdate = 1")
    suspend fun obtenerTareasPendientesDeActualizar(): List<TareaMentorEntity>

    @Query("SELECT * FROM tareas_mentor WHERE isPendingDelete = 1")
    suspend fun obtenerTareasPendientesDeEliminar(): List<TareaMentorEntity>

    //endregion

    //region Recompensas
    @Query("SELECT * FROM recompensas_mentor WHERE isPendingDelete = 0 ORDER BY recompensaId DESC")
    fun observeTodasLasRecompensasMentor(): Flow<List<RecompensaMentorEntity>>

    @Query("SELECT * FROM recompensas_mentor WHERE recompensaId = :id AND isPendingDelete = 0")
    suspend fun obtenerRecompensaMentorPorId(id: String?): RecompensaMentorEntity?

    @Query("SELECT * FROM recompensas_mentor WHERE isPendingDelete = 0")
    fun observeRecompensasDelMentor(): Flow<List<RecompensaMentorEntity>>

    @Query("SELECT * FROM recompensas_mentor WHERE isPendingCreate = 1")
    suspend fun obtenerRecompensasPendientesDeCrear(): List<RecompensaMentorEntity>

    @Query("SELECT * FROM recompensas_mentor WHERE isPendingUpdate = 1")
    suspend fun obtenerRecompensasPendientesDeActualizar(): List<RecompensaMentorEntity>

    @Query("SELECT * FROM recompensas_mentor WHERE isPendingDelete = 1")
    suspend fun obtenerRecompensasPendientesDeEliminar(): List<RecompensaMentorEntity>

    @Upsert
    suspend fun upsertRecompensa(recompensa: RecompensaMentorEntity)

    @Delete
    suspend fun eliminarRecompensaMentor(recompensa: RecompensaMentorEntity)

    @Query("DELETE FROM recompensas_mentor WHERE recompensaId = :id")
    suspend fun eliminarRecompensaMentorPorId(id: String)

    @Query("DELETE FROM recompensas_mentor")
    fun eliminarTodasLasRecompensas()

    //endregion
}