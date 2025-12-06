package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.TareaGemaDao
import edu.ucne.tasktally.data.mappers.toTareaGemaDomain
import edu.ucne.tasktally.data.mappers.toTareaGemaEntity
import edu.ucne.tasktally.domain.models.TareaGema
import edu.ucne.tasktally.domain.repository.TareaGemaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TareaGemaRepositoryImpl @Inject constructor(
    private val dao: TareaGemaDao
) : TareaGemaRepository {

    override fun observeTareasGema(): Flow<List<TareaGema>> =
        dao.observeAll().map { list ->
            list.map { it.toTareaGemaDomain() }
        }

    override suspend fun getTareaGema(id: String?): TareaGema? =
        dao.getById(id)?.toTareaGemaDomain()

    override suspend fun getTareaGemaByRemoteId(remoteId: Int): TareaGema? =
        dao.getByRemoteId(remoteId)?.toTareaGemaDomain()

    override suspend fun upsert(tarea: TareaGema): String {
        dao.upsert(tarea.toTareaGemaEntity())
        return tarea.tareaId
    }

    override suspend fun delete(tarea: TareaGema) {
        dao.delete(tarea.toTareaGemaEntity())
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteByRemoteId(remoteId: Int) {
        dao.deleteByRemoteId(remoteId)
    }
}
