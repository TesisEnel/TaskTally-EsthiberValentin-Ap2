package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.RecompensaMentor
import kotlinx.coroutines.flow.Flow

interface RecompensaMentorRepository {
    fun observeRecompensasMentor(): Flow<List<RecompensaMentor>>
    suspend fun getRecompensaMentor(id: String?): RecompensaMentor?
    suspend fun getRecompensaMentorByRemoteId(remoteId: Int?): RecompensaMentor?
    suspend fun upsert(recompensa: RecompensaMentor): String
    suspend fun delete(recompensa: RecompensaMentor)
    suspend fun deleteById(id: String)
    suspend fun deleteByRemoteId(remoteId: Int)
}
