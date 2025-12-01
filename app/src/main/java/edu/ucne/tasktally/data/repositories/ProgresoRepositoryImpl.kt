package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.ProgresoDao
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.domain.models.Progreso
import edu.ucne.tasktally.domain.repository.ProgresoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProgresoRepositoryImpl @Inject constructor(
    private val dao: ProgresoDao
) : ProgresoRepository {

    override fun observeProgresos(): Flow<List<Progreso>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getProgreso(id: Int?): Progreso? =
        dao.getById(id)?.toDomain()

    override suspend fun upsert(progreso: Progreso): Int {
        dao.upsert(progreso.toEntity())
        return progreso.progresoId
    }

    override suspend fun delete(progreso: Progreso) {
        dao.delete(progreso.toEntity())
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }
}