package edu.ucne.tasktally.data.local.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tasktally.data.local.entidades.RecompensaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecompensaDao {
    @Query("SELECT * FROM recompensa ORDER BY id DESC")
    fun observeAll(): Flow<List<RecompensaEntity>>

    @Query("SELECT * FROM recompensa WHERE id = :id")
    suspend fun getById(id: Int?): RecompensaEntity?

    @Upsert
    suspend fun upsert(recompensa: RecompensaEntity)

    @Delete
    suspend fun delete(recompensa: RecompensaEntity)

    @Query("DELETE FROM recompensa WHERE id = :id")
    suspend fun deleteById(id: Int)
}