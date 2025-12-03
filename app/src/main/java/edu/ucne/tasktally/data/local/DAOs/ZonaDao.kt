package edu.ucne.tasktally.data.local.DAOs

import androidx.room.*
import edu.ucne.tasktally.data.local.entidades.ZonaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ZonaDao {
    @Query("SELECT * FROM zona ORDER BY id DESC")
    fun observeAll(): Flow<List<ZonaEntity>>

    @Query("SELECT * FROM zona WHERE id = :id")
    suspend fun getById(id: Int?): ZonaEntity?

    @Upsert
    suspend fun upsert(zona: ZonaEntity)

    @Delete
    suspend fun delete(zona: ZonaEntity)

    @Query("DELETE FROM zona WHERE id = :id")
    suspend fun deleteById(id: Int)
}