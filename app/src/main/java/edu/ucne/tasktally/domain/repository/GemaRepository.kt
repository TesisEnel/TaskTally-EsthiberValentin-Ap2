package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.Gema
import kotlinx.coroutines.flow.Flow

interface GemaRepository {
    fun observeGemas(): Flow<List<Gema>>
    suspend fun getGema(id: Int?): Gema?
    suspend fun upsert(gema: Gema): Int
    suspend fun delete(gema: Gema)
    suspend fun deleteById(id: Int)
}