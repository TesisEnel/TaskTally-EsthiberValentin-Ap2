package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.RecompensaGemaDao
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
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
            list.map { it.toDomain() }
        }

    override suspend fun getRecompensaGema(id: String?): RecompensaGema? =
        dao.getById(id)?.toDomain()

    override suspend fun getRecompensaGemaByRemoteId(remoteId: Int?): RecompensaGema? =
        dao.getByRemoteId(remoteId)?.toDomain()

    override suspend fun upsert(recompensa: RecompensaGema): String {
        dao.upsert(recompensa.toEntity())
        return recompensa.recompensaId
    }

    override suspend fun delete(recompensa: RecompensaGema) {
        dao.delete(recompensa.toEntity())
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteByRemoteId(remoteId: Int) {
        dao.deleteByRemoteId(remoteId)
    }
}
