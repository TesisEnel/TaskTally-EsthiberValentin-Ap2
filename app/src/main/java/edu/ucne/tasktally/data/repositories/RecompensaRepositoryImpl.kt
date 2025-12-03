package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.RecompensaDao
import edu.ucne.tasktally.data.local.entidades.RecompensaEntity
import edu.ucne.tasktally.domain.models.Recompensa
import edu.ucne.tasktally.domain.repository.RecompensaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecompensaRepositoryImpl @Inject constructor(
    private val dao: RecompensaDao
) : RecompensaRepository {

    override fun observeRecompensas(): Flow<List<Recompensa>> =
        dao.observeAll().map { list ->
            list.map { recompensaEntity ->
                Recompensa(
                    id = recompensaEntity.id,
                    remoteId = recompensaEntity.remoteId,
                    titulo = recompensaEntity.titulo,
                    descripcion = recompensaEntity.descripcion,
                    precio = recompensaEntity.precio,
                    isPendingPost = recompensaEntity.isPendingPost,
                    isPendingUpdate = recompensaEntity.isPendingUpdate
                )
            }
        }

    override suspend fun getRecompensa(id: String?): Recompensa? =
        dao.getById(id)?.let { entity ->
            Recompensa(
                id = entity.id,
                remoteId = entity.remoteId,
                titulo = entity.titulo,
                descripcion = entity.descripcion,
                precio = entity.precio,
                isPendingPost = entity.isPendingPost,
                isPendingUpdate = entity.isPendingUpdate
            )
        }

    override suspend fun getRecompensaByRemoteId(remoteId: Int?): Recompensa? =
        dao.getByRemoteId(remoteId)?.let { entity ->
            Recompensa(
                id = entity.id,
                remoteId = entity.remoteId,
                titulo = entity.titulo,
                descripcion = entity.descripcion,
                precio = entity.precio,
                isPendingPost = entity.isPendingPost,
                isPendingUpdate = entity.isPendingUpdate
            )
        }

    override suspend fun upsert(recompensa: Recompensa): String {
        val entity = RecompensaEntity(
            id = recompensa.id,
            remoteId = recompensa.remoteId,
            titulo = recompensa.titulo,
            descripcion = recompensa.descripcion,
            precio = recompensa.precio,
            isPendingPost = recompensa.isPendingPost,
            isPendingUpdate = recompensa.isPendingUpdate
        )
        dao.upsert(entity)
        return recompensa.id
    }

    override suspend fun delete(recompensa: Recompensa) {
        val entity = RecompensaEntity(
            id = recompensa.id,
            remoteId = recompensa.remoteId,
            titulo = recompensa.titulo,
            descripcion = recompensa.descripcion,
            precio = recompensa.precio,
            isPendingPost = recompensa.isPendingPost,
            isPendingUpdate = recompensa.isPendingUpdate
        )
        dao.delete(entity)
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteByRemoteId(remoteId: Int) {
        dao.deleteByRemoteId(remoteId)
    }
}