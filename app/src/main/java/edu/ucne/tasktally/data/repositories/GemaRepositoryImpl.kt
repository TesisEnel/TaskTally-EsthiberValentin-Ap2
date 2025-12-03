package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.GemaDao
import edu.ucne.tasktally.data.local.entidades.GemaEntity
import edu.ucne.tasktally.domain.models.Gema
import edu.ucne.tasktally.domain.repository.GemaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import java.util.UUID

class GemaRepositoryImpl @Inject constructor(
    private val dao: GemaDao
) : GemaRepository {

    override fun observeGemas(): Flow<List<Gema>> =
        dao.observeAll().map { list ->
            list.map { gemaEntity ->
                Gema(
                    id = gemaEntity.id,
                    remoteId = gemaEntity.remoteId,
                    nombre = gemaEntity.nombre,
                    apellido = gemaEntity.apellido,
                    fechaNacimiento = gemaEntity.fechaNacimiento,
                    puntosActuales = gemaEntity.puntosActuales,
                    isPendingPost = gemaEntity.isPendingPost,
                    isPendingUpdate = gemaEntity.isPendingUpdate
                )
            }
        }

    override suspend fun getGema(id: String?): Gema? =
        dao.getById(id)?.let { entity ->
            Gema(
                id = entity.id,
                remoteId = entity.remoteId,
                nombre = entity.nombre,
                apellido = entity.apellido,
                fechaNacimiento = entity.fechaNacimiento,
                puntosActuales = entity.puntosActuales,
                isPendingPost = entity.isPendingPost,
                isPendingUpdate = entity.isPendingUpdate
            )
        }

    override suspend fun getGemaByRemoteId(remoteId: Int?): Gema? =
        dao.getByRemoteId(remoteId)?.let { entity ->
            Gema(
                id = entity.id,
                remoteId = entity.remoteId,
                nombre = entity.nombre,
                apellido = entity.apellido,
                fechaNacimiento = entity.fechaNacimiento,
                puntosActuales = entity.puntosActuales,
                isPendingPost = entity.isPendingPost,
                isPendingUpdate = entity.isPendingUpdate
            )
        }

    override suspend fun upsert(gema: Gema): String {
        val entity = GemaEntity(
            id = gema.id,
            remoteId = gema.remoteId,
            nombre = gema.nombre,
            apellido = gema.apellido,
            fechaNacimiento = gema.fechaNacimiento,
            puntosActuales = gema.puntosActuales,
            isPendingPost = gema.isPendingPost,
            isPendingUpdate = gema.isPendingUpdate
        )
        dao.upsert(entity)
        return gema.id
    }

    override suspend fun delete(gema: Gema) {
        val entity = GemaEntity(
            id = gema.id,
            remoteId = gema.remoteId,
            nombre = gema.nombre,
            apellido = gema.apellido,
            fechaNacimiento = gema.fechaNacimiento,
            puntosActuales = gema.puntosActuales,
            isPendingPost = gema.isPendingPost,
            isPendingUpdate = gema.isPendingUpdate
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