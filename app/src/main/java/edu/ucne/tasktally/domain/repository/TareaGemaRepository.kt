package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.TareaGema
import kotlinx.coroutines.flow.Flow

interface TareaGemaRepository {
    fun observeTareasGema(): Flow<List<TareaGema>>
    suspend fun getTareaGema(id: String?): TareaGema?
    suspend fun getTareaGemaByRemoteId(remoteId: Int): TareaGema?
    suspend fun upsert(tarea: TareaGema): String
    suspend fun delete(tarea: TareaGema)
    suspend fun deleteById(id: String)
    suspend fun deleteByRemoteId(remoteId: Int)
}
