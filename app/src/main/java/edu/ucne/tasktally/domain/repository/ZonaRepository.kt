package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.Zona
import kotlinx.coroutines.flow.Flow

interface ZonaRepository {
    fun observeZonas(): Flow<List<Zona>>
    suspend fun getZona(id: String?): Zona?
    suspend fun getZonaByRemoteId(remoteId: Int?): Zona?
    suspend fun upsert(zona: Zona): String
    suspend fun delete(zona: Zona)
    suspend fun deleteById(id: String)
    suspend fun deleteByRemoteId(remoteId: Int)
}