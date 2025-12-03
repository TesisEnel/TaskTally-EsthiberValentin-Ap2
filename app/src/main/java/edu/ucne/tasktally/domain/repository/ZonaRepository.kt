package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.Zona
import kotlinx.coroutines.flow.Flow

interface ZonaRepository {
    fun observeZonas(): Flow<List<Zona>>
    suspend fun getZona(id: Int?): Zona?
    suspend fun upsert(zona: Zona): String
    suspend fun delete(zona: Zona)
    suspend fun deleteById(id: Int)
}