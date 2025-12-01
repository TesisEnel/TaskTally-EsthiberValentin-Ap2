package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.TransaccionRecompensaDao
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.domain.models.TransaccionRecompensa
import edu.ucne.tasktally.domain.repository.TransaccionRecompensaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransaccionRecompensaRepositoryImpl @Inject constructor(
    private val dao: TransaccionRecompensaDao
) : TransaccionRecompensaRepository {

    override fun observeTransacciones(): Flow<List<TransaccionRecompensa>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getTransaccion(id: Int?): TransaccionRecompensa? =
        dao.getById(id)?.toDomain()

    override suspend fun upsert(transaccion: TransaccionRecompensa): Int {
        dao.upsert(transaccion.toEntity())
        return transaccion.transaccionRecompensaId
    }

    override suspend fun delete(transaccion: TransaccionRecompensa) {
        dao.delete(transaccion.toEntity())
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }
}