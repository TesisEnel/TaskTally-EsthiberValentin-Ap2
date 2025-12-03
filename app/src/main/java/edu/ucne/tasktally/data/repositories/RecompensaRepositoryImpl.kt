package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.RecompensaDao
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.domain.models.Recompensa
import edu.ucne.tasktally.domain.repository.RecompensaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class RecompensaRepositoryImpl @Inject constructor(
    private val dao: RecompensaDao
) : RecompensaRepository {

    override fun observeRecompensas(): Flow<List<Recompensa>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getRecompensa(id: String?): Recompensa? =
        dao.getById(id)?.toDomain()

    override suspend fun getRecompensaByRemoteId(remoteId: Int?): Recompensa? =
        dao.getByRemoteId(remoteId)?.toDomain()

    override suspend fun upsert(recompensa: Recompensa): String {
        val entityToSave = if (recompensa.id.isBlank()) {
            recompensa.toEntity().copy(id = UUID.randomUUID().toString())
        } else {
            recompensa.toEntity()
        }
        dao.upsert(entityToSave)
        return entityToSave.id
    }

    override suspend fun delete(recompensa: Recompensa) {
        dao.delete(recompensa.toEntity())
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteByRemoteId(remoteId: Int) {
        dao.deleteByRemoteId(remoteId)
    }
}