package edu.ucne.tasktally.data.local.DAOs

import androidx.room.*
import edu.ucne.tasktally.data.local.entidades.GemaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GemaDao {
    @Query("SELECT * FROM gema ORDER BY id DESC")
    fun observeAll(): Flow<List<GemaEntity>>

    @Query("SELECT * FROM gema WHERE id = :id")
    suspend fun getById(id: String?): GemaEntity?

    @Query("SELECT * FROM gema WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: Int?): GemaEntity?

    @Upsert
    suspend fun upsert(gema: GemaEntity)

    @Delete
    suspend fun delete(gema: GemaEntity)

    @Query("DELETE FROM gema WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM gema WHERE remoteId = :remoteId")
    suspend fun deleteByRemoteId(remoteId: Int)
}