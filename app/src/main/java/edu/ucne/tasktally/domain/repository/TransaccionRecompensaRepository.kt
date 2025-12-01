package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.TransaccionRecompensa
import kotlinx.coroutines.flow.Flow

interface TransaccionRecompensaRepository {
    fun observeTransacciones(): Flow<List<TransaccionRecompensa>>
    suspend fun getTransaccion(id: Int?): TransaccionRecompensa?
    suspend fun upsert(transaccion: TransaccionRecompensa): Int
    suspend fun delete(transaccion: TransaccionRecompensa)
    suspend fun deleteById(id: Int)
}