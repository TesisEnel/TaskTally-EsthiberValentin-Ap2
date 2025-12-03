package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.Gema
import kotlinx.coroutines.flow.Flow

interface GemaRepository {
    fun observeGemas(): Flow<List<Gema>>
    suspend fun getGema(id: String?): Gema?
    suspend fun getGemaByRemoteId(remoteId: Int?): Gema?
    suspend fun upsert(gema: Gema): String
    suspend fun delete(gema: Gema)
    suspend fun deleteById(id: String)
    suspend fun deleteByRemoteId(remoteId: Int)
}