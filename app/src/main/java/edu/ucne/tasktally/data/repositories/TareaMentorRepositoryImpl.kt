package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.TareaMentorDao
import edu.ucne.tasktally.data.mappers.toTareaMentorDomain
import edu.ucne.tasktally.data.mappers.toTareaMentorEntity
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
            list.map { it.toTareaMentorDomain() }
        }

    override suspend fun getTareaMentor(id: String?): TareaMentor? =
        dao.getById(id)?.toTareaMentorDomain()

    override suspend fun getTareaMentorByRemoteId(remoteId: Int): TareaMentor? =
        dao.getByRemoteId(remoteId)?.toTareaMentorDomain()

    override suspend fun upsert(tarea: TareaMentor): String {
        dao.upsert(tarea.toTareaMentorEntity())
        return tarea.tareaId
    }

    override suspend fun delete(tarea: TareaMentor) {
        dao.delete(tarea.toTareaMentorEntity())
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteByRemoteId(remoteId: Int) {
        dao.deleteByRemoteId(remoteId)
    }
}
