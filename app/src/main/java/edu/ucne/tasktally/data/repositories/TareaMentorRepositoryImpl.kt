package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.TareaMentorDao
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.domain.models.TareaMentor
import edu.ucne.tasktally.domain.repository.TareaMentorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TareaMentorRepositoryImpl @Inject constructor(
    private val dao: TareaMentorDao
) : TareaMentorRepository {

    override fun observeTareasMentor(): Flow<List<TareaMentor>> =
        dao.observeAll().map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getTareaMentor(id: String?): TareaMentor? =
        dao.getById(id)?.toDomain()

    override suspend fun getTareaMentorByRemoteId(remoteId: Int?): TareaMentor? =
        dao.getByRemoteId(remoteId)?.toDomain()

    override suspend fun upsert(tarea: TareaMentor): String {
        dao.upsert(tarea.toEntity())
        return tarea.tareaId
    }

    override suspend fun delete(tarea: TareaMentor) {
        dao.delete(tarea.toEntity())
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteByRemoteId(remoteId: Int) {
        dao.deleteByRemoteId(remoteId)
    }
}
