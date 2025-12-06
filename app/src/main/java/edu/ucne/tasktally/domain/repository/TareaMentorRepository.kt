package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.TareaMentor
import kotlinx.coroutines.flow.Flow

interface TareaMentorRepository {
    fun observeTareasMentor(): Flow<List<TareaMentor>>
    suspend fun getTareaMentor(id: String?): TareaMentor?
    suspend fun getTareaMentorByRemoteId(remoteId: Int): TareaMentor?
    suspend fun upsert(tarea: TareaMentor): String
    suspend fun delete(tarea: TareaMentor)
    suspend fun deleteById(id: String)
    suspend fun deleteByRemoteId(remoteId: Int)
}
