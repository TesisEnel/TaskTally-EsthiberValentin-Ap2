package edu.ucne.tasktally.data.local.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tasktally.data.local.entidades.TareaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {
    @Query("SELECT * FROM tareas ORDER BY id DESC")
    fun observeAll(): Flow<List<TareaEntity>>

    @Query("SELECT * FROM tareas WHERE id = :id")
    suspend fun getById(id: String?): TareaEntity?

    @Query("SELECT * FROM tareas WHERE estado = :estado")
    fun observeByEstado(estado: String): Flow<List<TareaEntity>>

    @Upsert
    suspend fun upsert(tarea: TareaEntity)

    @Delete
    suspend fun delete(tarea: TareaEntity)

    @Query("DELETE FROM tareas WHERE id = :id")
    suspend fun deleteById(id: String)
}