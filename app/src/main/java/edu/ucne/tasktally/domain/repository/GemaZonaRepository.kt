package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.GemaZona
import kotlinx.coroutines.flow.Flow

interface GemaZonaRepository {
    fun observeGemaZonas(): Flow<List<GemaZona>>
    suspend fun getGemaZona(id: Int?): GemaZona?
    suspend fun upsert(gemaZona: GemaZona): Int
    suspend fun delete(gemaZona: GemaZona)
    suspend fun deleteById(id: Int)
}