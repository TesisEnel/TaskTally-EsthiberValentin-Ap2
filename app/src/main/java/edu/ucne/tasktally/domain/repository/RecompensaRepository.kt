package edu.ucne.tasktally.domain.repository

import kotlinx.coroutines.flow.Flow

interface RecompensaRepository {
    fun observeRecompensas(): Flow<List<Recompensa>>
    suspend fun getRecompensa(id: String?): Recompensa?
    suspend fun getRecompensaByRemoteId(remoteId: Int?): Recompensa?
    suspend fun upsert(recompensa: Recompensa): String
    suspend fun delete(recompensa: Recompensa)
    suspend fun deleteById(id: String)
    suspend fun deleteByRemoteId(remoteId: Int)
}