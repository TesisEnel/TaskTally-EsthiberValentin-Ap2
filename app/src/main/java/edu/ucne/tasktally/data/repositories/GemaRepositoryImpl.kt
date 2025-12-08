package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.RecompensaGemaDao
import edu.ucne.tasktally.data.local.DAOs.TareaGemaDao
import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.mappers.toTareaGemaDomain
import edu.ucne.tasktally.data.mappers.toZonaDomain
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.data.mappers.toRecompensaGemaDomain
import edu.ucne.tasktally.data.remote.DTOs.gema.recompensa.CanjearRecompensaRequest
import edu.ucne.tasktally.data.remote.DTOs.gema.tarea.BulkUpdateTareasResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.tarea.UpdateTareaEstadoRequest
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.remote.TaskTallyApi
import edu.ucne.tasktally.domain.models.RecompensaGema
import edu.ucne.tasktally.domain.models.TareaGema
import edu.ucne.tasktally.domain.models.Zona
import edu.ucne.tasktally.domain.repository.GemaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GemaRepositoryImpl @Inject constructor(
    private val tareaDao: TareaGemaDao,
    private val gemaDao: RecompensaGemaDao,
    private val zonaDao: ZonaDao,
    private val api: TaskTallyApi
) : GemaRepository {

    //region Tareas
    override fun observeTareas(): Flow<List<TareaGema>> =
        tareaDao.observeAll().map { list ->
            list.map { it.toTareaGemaDomain() }
        }

    override suspend fun getTareasGemaLocal(gemaId: Int, dia: String?): List<TareaGema> =
        tareaDao.getTareasGemaLocal(gemaId, dia).map { it.toTareaGemaDomain() }

    override suspend fun iniciarTareaGema(tareaId: String) {
        tareaDao.iniciarTarea(tareaId)
    }

    override suspend fun completarTareaGema(tareaId: String) {
        tareaDao.completarTarea(tareaId)
    }
    //endregion

    //region Recompensas
    override fun observeRecompensas(): Flow<List<RecompensaGema>> =
        gemaDao.observeAll().map { list ->
            list.map { it.toRecompensaGemaDomain() }
        }

    override suspend fun canjearRecompensa(recompensaId: String, gemaId: Int) {
        val recompensa = gemaDao.getById(recompensaId)
        recompensa?.let {
            val updatedRecompensa = it.copy(
                isPendingUpdate = true,
                perteneceA = gemaId
            )
            gemaDao.upsert(updatedRecompensa)
        }
    }
    //endregion

    override suspend fun getZoneInfo(zoneId: Int): Zona {
        return try {
            val response = api.obtenerGemaInfoZona(zoneId)
            if (response.isSuccessful) {
                response.body()?.let { zoneInfo ->
                    val zona = zoneInfo.toZonaDomain()

                    zonaDao.upsert(zona.toEntity())

                    zona
                } ?: run {
                    getLocalZoneInfo(zoneId)
                }
            } else {
                getLocalZoneInfo(zoneId)
            }
        } catch (e: Exception) {
            getLocalZoneInfo(zoneId)
        }
    }

    private suspend fun getLocalZoneInfo(zoneId: Int): Zona {
        return zonaDao.getById(zoneId)?.toDomain() ?: Zona(
            zonaId = 0,
            nombre = "",
            joinCode = "",
            mentorId = "",
            gemas = emptyList()
        )
    }

    override suspend fun postPendingEstadosTareas(): Resource<BulkUpdateTareasResponse> {
        return try {
            val pendingTareas = tareaDao.getPendingUpdate()

        if (pendingTareas.isEmpty()) {
            return Resource.Success(BulkUpdateTareasResponse(0, 0, emptyList()))
        }

        val updateRequests = pendingTareas.mapNotNull { tarea ->
            tarea.remoteId?.let { remoteId ->
                UpdateTareaEstadoRequest(
                    tareaId = remoteId,
                    estado = tarea.estado
                )
            }
        }

        if (updateRequests.isEmpty()) {
            return Resource.Success(BulkUpdateTareasResponse(0, 0, emptyList()))
        }

            val gemaId = pendingTareas.firstOrNull()?.perteneceA ?: return Resource.Error("No tiene gemaId")

            val response = api.bulkUpdateEstadoTareas(gemaId, updateRequests)

            if (response.isSuccessful) {
                response.body()?.let { bulkResponse ->

                    pendingTareas.forEach { tarea ->
                        val updatedTarea = tarea.copy(isPendingUpdate = false)
                        tareaDao.upsert(updatedTarea)
                    }
                    Resource.Success(bulkResponse)
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error("API call failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception occurred: ${e.message}")
        }
    }

    override suspend fun postPendingCanjearRecompensas(): Resource<Unit> {
        return try {
            val pendingRecompensas = gemaDao.getPendingUpdate()

            if (pendingRecompensas.isEmpty()) {
                return Resource.Success(Unit)
            }

            for (recompensa in pendingRecompensas) {
                recompensa.remoteId?.let { remoteId ->
                    val canjearRequest = CanjearRecompensaRequest(
                        recompensaId = remoteId,
                        gemaId = recompensa.perteneceA ?: 0
                    )

                    val response = api.canjearRecompensa(canjearRequest)

                    if (response.isSuccessful) {
                        val updatedRecompensa = recompensa.copy(isPendingUpdate = false)
                        gemaDao.upsert(updatedRecompensa)
                    } else {
                        return Resource.Error("Failed to canjear recompensa: ${response.errorBody()?.string()}")
                    }
                }
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Exception occurred: ${e.message}")
        }
    }
}
