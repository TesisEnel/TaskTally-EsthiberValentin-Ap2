package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.RecompensaDao

import edu.ucne.tasktally.data.local.entidades.RecompensaMentorEntity
import edu.ucne.tasktally.data.mappers.toRecompensaMentorDomain
import edu.ucne.tasktally.domain.models.RecompensaMentor
import edu.ucne.tasktally.domain.repository.RecompensaMentorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecompensaMentorRepositoryImpl @Inject constructor(
    private val dao: RecompensaDao
) : RecompensaMentorRepository {

    override fun observeRecompensasMentor(): Flow<List<RecompensaMentor>> =
        dao.observeAll().map { list ->
            list.map { it.toRecompensaMentorDomain() }
        }

    override suspend fun getRecompensaMentor(id: String?): RecompensaMentor? =
        dao.getById(id)?.toRecompensaMentorDomain()

    override suspend fun getRecompensaMentorByRemoteId(remoteId: Int?): RecompensaMentor? =
        dao.getByRemoteId(remoteId)?.toRecompensaMentorDomain()

    override suspend fun upsert(recompensa: RecompensaMentor): String {
        val entity = RecompensaMentorEntity(
            recompensaId = recompensa.recompensaId,
            remoteId = recompensa.remoteId,
            createdBy = recompensa.createdBy,
            titulo = recompensa.titulo,
            descripcion = recompensa.descripcion,
            precio = recompensa.precio,
            isDisponible = recompensa.isDisponible,
            fechaCreacion = recompensa.fechaCreacion,
            createdByName = recompensa.createdByName,
            totalCanjes = recompensa.totalCanjes,
            nombreImgVector = recompensa.nombreImgVector,
            isPendingCreate = recompensa.isPendingCreate,
            isPendingUpdate = recompensa.isPendingUpdate,
            isPendingDelete = recompensa.isPendingDelete
        )
        dao.upsert(entity)
        return recompensa.recompensaId
    }

    override suspend fun delete(recompensa: RecompensaMentor) {
        val entity = RecompensaMentorEntity(
            recompensaId = recompensa.recompensaId,
            remoteId = recompensa.remoteId,
            createdBy = recompensa.createdBy,
            titulo = recompensa.titulo,
            descripcion = recompensa.descripcion,
            precio = recompensa.precio,
            isDisponible = recompensa.isDisponible,
            fechaCreacion = recompensa.fechaCreacion,
            createdByName = recompensa.createdByName,
            totalCanjes = recompensa.totalCanjes,
            nombreImgVector = recompensa.nombreImgVector,
            isPendingCreate = recompensa.isPendingCreate,
            isPendingUpdate = recompensa.isPendingUpdate,
            isPendingDelete = recompensa.isPendingDelete
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
