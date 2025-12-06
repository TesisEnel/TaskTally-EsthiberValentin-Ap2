package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.RecompensaGema
import kotlinx.coroutines.flow.Flow

interface RecompensaGemaRepository {
    fun observeRecompensasGema(): Flow<List<RecompensaGema>>
    suspend fun getRecompensaGema(id: String?): RecompensaGema?
    suspend fun getRecompensaGemaByRemoteId(remoteId: Int?): RecompensaGema?
    suspend fun upsert(recompensa: RecompensaGema): String
    suspend fun delete(recompensa: RecompensaGema)
    suspend fun deleteById(id: String)
    suspend fun deleteByRemoteId(remoteId: Int)
}
