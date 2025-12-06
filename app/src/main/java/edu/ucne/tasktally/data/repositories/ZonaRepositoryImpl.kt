package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.mappers.toZonaDomain
import edu.ucne.tasktally.data.mappers.toZonaEntity
import edu.ucne.tasktally.domain.models.Zona
import edu.ucne.tasktally.domain.repository.ZonaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ZonaRepositoryImpl @Inject constructor(
    private val dao: ZonaDao
) : ZonaRepository {

    override fun observeZonas(): Flow<List<Zona>> =
        dao.observeAll().map { list -> list.map { it.toZonaDomain() } }

    override suspend fun getZona(id: String?): Zona? =
        dao.getById(id)?.toZonaDomain()

    override suspend fun getZonaByRemoteId(remoteId: Int?): Zona? =
        dao.getByRemoteId(remoteId)?.toZonaDomain()

    override suspend fun upsert(zona: Zona): String {
        dao.upsert(zona.toZonaEntity())
        return zona.zonaId
    }

    override suspend fun delete(zona: Zona) {
        dao.delete(zona.toZonaEntity())
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteByRemoteId(remoteId: Int) {
        dao.deleteByRemoteId(remoteId)
    }
}