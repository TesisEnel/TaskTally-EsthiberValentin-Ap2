package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.local.DAOs.gema.GemaDao
import edu.ucne.tasktally.data.local.preferences.AuthPreferencesManager
import edu.ucne.tasktally.data.mappers.toTareaGemaDomain
import edu.ucne.tasktally.data.mappers.toTareaGemaEntity
import edu.ucne.tasktally.data.mappers.toZonaDomain
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.data.mappers.toRecompensaGemaDomain
import edu.ucne.tasktally.data.mappers.toRecompensaGemaEntity
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GemaRepositoryImpl @Inject constructor(
    private val gemaDao: GemaDao,
    private val zonaDao: ZonaDao,
    private val api: TaskTallyApi,
    private val authPreferencesManager: AuthPreferencesManager
) : GemaRepository {

    companion object {
        private const val ERROR_NULL_RESPONSE_BODY = "Response body is null"
    }

    override suspend fun getTareasRemote(gemaId: Int): Resource<List<TareaGema>> {
        return try {
            val response = api.getTareasGema(gemaId)

            if (response.isSuccessful) {
                response.body()?.let { tareasResponse ->
                    gemaDao.eliminarTodasLasTareas()

                    tareasResponse.forEach { tareaDto ->
                        gemaDao.upsertTarea(tareaDto.toTareaGemaEntity())
                    }

                    val tareas = tareasResponse.map { it.toTareaGemaEntity().toTareaGemaDomain() }
                    Resource.Success(tareas)
                } ?: Resource.Error(ERROR_NULL_RESPONSE_BODY)
            } else {
                Resource.Error("API call failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception occurred: ${e.message}")
        }
    }

    //region Tareas
    override fun observeTareas(): Flow<List<TareaGema>> =
        gemaDao.observeTodasLasTareas().map { list ->
            list.map { it.toTareaGemaDomain() }
        }


    override suspend fun iniciarTareaGema(gemaId: Int, tareaId: String) {
        gemaDao.iniciarTarea(tareaId)
    }

    override suspend fun completarTareaGema(gemaId: Int, tareaId: String) {
        val tarea = gemaDao.obtenerTareaPorId(tareaId)
        tarea?.let {
            gemaDao.completarTarea(tareaId)

            val puntosDisponibles = authPreferencesManager.puntosDisponibles.first() ?: 0
            val newPuntosDisponibles = puntosDisponibles + it.puntos
            val puntosGastados = authPreferencesManager.puntosGastados.first() ?: 0

            authPreferencesManager.updatePuntos(newPuntosDisponibles, puntosGastados)
        }
    }

    override suspend fun getRecompensasRemote(gemaId: Int): Resource<List<RecompensaGema>> {
        return try {
            val response = api.getRecompensasGema(gemaId)

            if (response.isSuccessful) {
                response.body()?.let { recompensasResponse ->
                    gemaDao.eliminarTodasLasRecompensas()

                    recompensasResponse.forEach { recompensaDto ->
                        gemaDao.upsertRecompensa(recompensaDto.toRecompensaGemaEntity())
                    }

                    val recompensas = recompensasResponse.map {
                        it.toRecompensaGemaEntity().toRecompensaGemaDomain()
                    }
                    Resource.Success(recompensas)
                } ?: Resource.Error(ERROR_NULL_RESPONSE_BODY)
            } else {
                Resource.Error("API call failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception occurred: ${e.message}")
        }
    }
    //endregion

    //region Recompensas
    override fun observeRecompensas(): Flow<List<RecompensaGema>> =
        gemaDao.observeTodasLasRecompensas().map { list ->
            list.map { it.toRecompensaGemaDomain() }
        }

    override suspend fun canjearRecompensa(recompensaId: String, gemaId: Int) {
        val recompensa = gemaDao.obtenerRecompensaPorId(recompensaId)
        recompensa?.let {
            val updatedRecompensa = it.copy(
                canjeada = true,
                isPendingUpdate = true,
            )
            gemaDao.upsertRecompensa(updatedRecompensa)

            val puntosDisponibles = authPreferencesManager.puntosDisponibles.first() ?: 0
            val puntosGastados = authPreferencesManager.puntosGastados.first() ?: 0

            val newPuntosDisponibles = puntosDisponibles - it.precio
            val newPuntosGastados = puntosGastados + it.precio

            authPreferencesManager.updatePuntos(newPuntosDisponibles, newPuntosGastados)
        }
    }
    //endregion

    override suspend fun getZoneInfo(gemaId: Int, zoneId: Int): Zona {
        return try {
            val response = api.obtenerGemaInfoZona(gemaId, zoneId)
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

    override suspend fun postPendingEstadosTareas(gemaId: Int): Resource<BulkUpdateTareasResponse> {
        return try {
            val pendingTareas = gemaDao.getPendingUpdateTareas()

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

            val response = api.bulkUpdateEstadoTareas(gemaId, updateRequests)

            if (response.isSuccessful) {
                response.body()?.let { bulkResponse ->
                    pendingTareas.forEach { tarea ->
                        val updatedTarea = tarea.copy(isPendingUpdate = false)
                        gemaDao.upsertTarea(updatedTarea)
                    }

                    bulkResponse.results
                        .firstOrNull { it.success && it.puntosActuales != null }
                        ?.let { result ->
                            val puntosGastados = authPreferencesManager.puntosGastados.first() ?: 0
                            authPreferencesManager.updatePuntos(
                                puntosDisponibles = result.puntosActuales!!,
                                puntosGastados = puntosGastados
                            )
                        }

                    Resource.Success(bulkResponse)
                } ?: Resource.Error(ERROR_NULL_RESPONSE_BODY)
            } else {
                Resource.Error("API call failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception occurred: ${e.message}")
        }
    }

    override suspend fun postPendingCanjearRecompensas(gemaId: Int): Resource<Unit> {
        return try {
            val pendingRecompensas = gemaDao.getPendingUpdateRecompensas()

            if (pendingRecompensas.isEmpty()) {
                return Resource.Success(Unit)
            }

            for (recompensa in pendingRecompensas) {
                recompensa.remoteId?.let { remoteId ->
                    val canjearRequest = CanjearRecompensaRequest(
                        recompensaId = remoteId,
                        gemaId = gemaId
                    )

                    val response = api.canjearRecompensa(canjearRequest)

                    if (response.isSuccessful) {
                        response.body()?.let { canjearResponse ->
                            val updatedRecompensa = recompensa.copy(isPendingUpdate = false)
                            gemaDao.upsertRecompensa(updatedRecompensa)

                            if (canjearResponse.puntosRestantes != null) {
                                val puntosGastados = authPreferencesManager.puntosGastados.first() ?: 0
                                authPreferencesManager.updatePuntos(
                                    puntosDisponibles = canjearResponse.puntosRestantes,
                                    puntosGastados = puntosGastados
                                )
                            }
                        }
                    } else {
                        return Resource.Error(
                            "Failed to canjear recompensa: ${
                                response.errorBody()?.string()
                            }"
                        )
                    }
                }
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Exception occurred: ${e.message}")
        }
    }
}
