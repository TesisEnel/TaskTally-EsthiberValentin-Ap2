package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.Racha
import kotlinx.coroutines.flow.Flow

interface RachaRepository {
    fun observeRachas(): Flow<List<Racha>>
    suspend fun getRacha(id: Int?): Racha?
    suspend fun upsert(racha: Racha): Int
    suspend fun delete(racha: Racha)
    suspend fun deleteById(id: Int)
}