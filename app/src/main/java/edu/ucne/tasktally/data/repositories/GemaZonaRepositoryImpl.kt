package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.GemaZonaDao
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.domain.models.GemaZona
import edu.ucne.tasktally.domain.repository.GemaZonaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GemaZonaRepositoryImpl @Inject constructor(
    private val dao: GemaZonaDao
) : GemaZonaRepository {

    override fun observeGemaZonas(): Flow<List<GemaZona>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getGemaZona(id: Int?): GemaZona? =
        dao.getById(id)?.toDomain()

    override suspend fun upsert(gemaZona: GemaZona): Int {
        dao.upsert(gemaZona.toEntity())
        return gemaZona.gemaZonaId
    }

    override suspend fun delete(gemaZona: GemaZona) {
        dao.delete(gemaZona.toEntity())
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }
}