package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.RecompensaGemaDao
import edu.ucne.tasktally.data.mappers.toRecompensaGemaDomain
import edu.ucne.tasktally.data.mappers.toRecompensaGemaEntity
import edu.ucne.tasktally.domain.models.RecompensaGema
import edu.ucne.tasktally.domain.repository.RecompensaGemaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecompensaGemaRepositoryImpl @Inject constructor(
    private val dao: RecompensaGemaDao
) : RecompensaGemaRepository {

    override fun observeRecompensasGema(): Flow<List<RecompensaGema>> =
        dao.observeAll().map { list ->
            list.map { it.toRecompensaGemaDomain() }
        }

    override suspend fun getRecompensaGema(id: String?): RecompensaGema? =
        dao.getById(id)?.toRecompensaGemaDomain()

    override suspend fun getRecompensaGemaByRemoteId(remoteId: Int?): RecompensaGema? =
        dao.getByRemoteId(remoteId)?.toRecompensaGemaDomain()

    override suspend fun upsert(recompensa: RecompensaGema): String {
        dao.upsert(recompensa.toRecompensaGemaEntity())
        return recompensa.recompensaId
    }

    override suspend fun delete(recompensa: RecompensaGema) {
        dao.delete(recompensa.toRecompensaGemaEntity())
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteByRemoteId(remoteId: Int) {
//        dao.deleteByRemoteId(remoteId)
    }
}
