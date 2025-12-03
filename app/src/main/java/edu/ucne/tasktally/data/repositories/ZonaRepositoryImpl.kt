package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.domain.models.Zona
import edu.ucne.tasktally.domain.repository.ZonaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ZonaRepositoryImpl @Inject constructor(
    private val dao: ZonaDao
) : ZonaRepository {

    override fun observeZonas(): Flow<List<Zona>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getZona(id: Int?): Zona? =
        dao.getById(id)?.toDomain()

    override suspend fun upsert(zona: Zona): String {
        dao.upsert(zona.toEntity())
        return zona.id
    }

    override suspend fun delete(zona: Zona) {
        dao.delete(zona.toEntity())
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }
}