package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.Transaccion
import kotlinx.coroutines.flow.Flow

interface TransaccionRepository {
    fun observeTransacciones(): Flow<List<Transaccion>>
    suspend fun getTransaccion(id: String?): Transaccion?
    suspend fun getTransaccionByRemoteId(remoteId: Int?): Transaccion?
    suspend fun upsert(transaccion: Transaccion): String
    suspend fun delete(transaccion: Transaccion)
    suspend fun deleteById(id: String)
    suspend fun deleteByRemoteId(remoteId: Int)
}
