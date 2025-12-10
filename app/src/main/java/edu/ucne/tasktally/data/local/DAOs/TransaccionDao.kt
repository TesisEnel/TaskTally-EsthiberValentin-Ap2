package edu.ucne.tasktally.data.local.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tasktally.data.local.entidades.TransaccionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaccionDao {
    @Query("SELECT * FROM transaccion WHERE isPendingDelete = 0 ORDER BY transaccionId DESC")
    fun observeAll(): Flow<List<TransaccionEntity>>

    @Query("SELECT * FROM transaccion WHERE transaccionId = :id AND isPendingDelete = 0")
    suspend fun getById(id: String?): TransaccionEntity?

    @Query("SELECT * FROM transaccion WHERE remoteId = :remoteId AND isPendingDelete = 0")
    suspend fun getByRemoteId(remoteId: Int?): TransaccionEntity?

    @Query("SELECT * FROM transaccion WHERE gemaId = :gemaId AND isPendingDelete = 0")
    fun observeByGema(gemaId: Int): Flow<List<TransaccionEntity>>

    @Query("SELECT * FROM transaccion WHERE isPendingCreate = 1")
    suspend fun getPendingCreate(): List<TransaccionEntity>

    @Query("SELECT * FROM transaccion WHERE isPendingUpdate = 1")
    suspend fun getPendingUpdate(): List<TransaccionEntity>

    @Query("SELECT * FROM transaccion WHERE isPendingDelete = 1")
    suspend fun getPendingDelete(): List<TransaccionEntity>

    @Upsert
    suspend fun upsert(transaccion: TransaccionEntity)

    @Delete
    suspend fun delete(transaccion: TransaccionEntity)

    @Query("DELETE FROM transaccion WHERE transaccionId = :id")
    suspend fun deleteById(id: String)

    @Query("UPDATE transaccion SET isPendingCreate = 0, remoteId = :remoteId WHERE transaccionId = :localId")
    suspend fun markSynced(localId: String, remoteId: Int)

    @Query("UPDATE transaccion SET isPendingUpdate = 0 WHERE transaccionId = :id")
    suspend fun clearUpdateFlag(id: String)
}
