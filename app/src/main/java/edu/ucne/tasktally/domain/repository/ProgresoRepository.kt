package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.Progreso
import kotlinx.coroutines.flow.Flow

interface ProgresoRepository {
    fun observeProgresos(): Flow<List<Progreso>>
    suspend fun getProgreso(id: Int?): Progreso?
    suspend fun upsert(progreso: Progreso): Int
    suspend fun delete(progreso: Progreso)
    suspend fun deleteById(id: Int)
}