package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.GemaDao
import edu.ucne.tasktally.data.mappers.toGemaDomain
import edu.ucne.tasktally.data.mappers.toGemaEntity
import edu.ucne.tasktally.domain.models.Gema
import edu.ucne.tasktally.domain.repository.GemaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GemaRepositoryImpl @Inject constructor(
    private val dao: GemaDao
) : GemaRepository {

    override fun observeGemas(): Flow<List<Gema>> =
        dao.observeAll().map { list ->
            list.map { it.toGemaDomain() }
        }

    override suspend fun getGema(id: String?): Gema? =
        dao.getById(id)?.toGemaDomain()

    override suspend fun getGemaByRemoteId(remoteId: Int?): Gema? =
        dao.getByRemoteId(remoteId)?.toGemaDomain()

    override suspend fun upsert(gema: Gema): String {
        dao.upsert(gema.toGemaEntity())
        return gema.gemaId
    }

    override suspend fun delete(gema: Gema) {
        dao.delete(gema.toGemaEntity())
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteByRemoteId(remoteId: Int) {
        dao.deleteByRemoteId(remoteId)
    }
}