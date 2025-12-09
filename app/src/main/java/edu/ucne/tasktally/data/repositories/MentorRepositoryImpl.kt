package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.RecompensaMentorDao
import edu.ucne.tasktally.data.local.DAOs.TareaMentorDao
import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.mappers.toRecompensaMentorDomain
import edu.ucne.tasktally.data.mappers.toRecompensaMentorEntity
import edu.ucne.tasktally.data.mappers.toTareaMentorDomain
import edu.ucne.tasktally.data.mappers.toTareaMentorEntity
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.data.mappers.toZonaDomain
import edu.ucne.tasktally.data.remote.DTOs.mentor.tareas.BulkTareasRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.tareas.BulkTareasResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.tareas.TareaOperationDto
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.BulkRecompensasRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.BulkRecompensasResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.RecompensaOperationDto
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneCodeResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.ZoneInfoMentorResponse
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.remote.TaskTallyApi
import edu.ucne.tasktally.domain.models.RecompensaMentor
import edu.ucne.tasktally.domain.models.TareaMentor
import edu.ucne.tasktally.domain.models.Zona
import edu.ucne.tasktally.domain.repository.MentorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MentorRepositoryImpl @Inject constructor(
    private val tareaDao: TareaMentorDao,
    private val recompensaDao: RecompensaMentorDao,
    private val zonaDao: ZonaDao,
    private val api: TaskTallyApi
) : MentorRepository {

    //region Tareas
    override fun observeTareas(): Flow<List<TareaMentor>> =
        tareaDao.observeAll().map { list ->
            list.map { it.toTareaMentorDomain() }
        }

    override fun observeTareasByMentor(mentorId: Int): Flow<List<TareaMentor>> =
        tareaDao.observeByMentor(mentorId).map { list ->
            list.map { it.toTareaMentorDomain() }
        }

    override suspend fun getTareaById(id: String): TareaMentor? =
        tareaDao.getById(id)?.toTareaMentorDomain()

    override suspend fun createTareaLocal(tarea: TareaMentor) {
        tareaDao.upsert(tarea.toTareaMentorEntity())
    }

    override suspend fun updateTareaLocal(tarea: TareaMentor) {
        tareaDao.upsert(tarea.copy(isPendingUpdate = true).toTareaMentorEntity())
    }

    override suspend fun deleteTareaLocal(tarea: TareaMentor) {
        tareaDao.upsert(tarea.copy(isPendingDelete = true).toTareaMentorEntity())
    }
    //endregion

    //region Recompensas
    override fun observeRecompensas(): Flow<List<RecompensaMentor>> =
        recompensaDao.observeAll().map { list ->
            list.map { it.toRecompensaMentorDomain() }
        }

    override fun observeRecompensasByMentor(mentorId: Int): Flow<List<RecompensaMentor>> =
        recompensaDao.observeByMentor(mentorId).map { list ->
            list.map { it.toRecompensaMentorDomain() }
        }

    override suspend fun getRecompensaById(id: String): RecompensaMentor? =
        recompensaDao.getById(id)?.toRecompensaMentorDomain()

    override suspend fun createRecompensaLocal(recompensa: RecompensaMentor) {
        recompensaDao.upsert(recompensa.toRecompensaMentorEntity())
    }

    override suspend fun updateRecompensaLocal(recompensa: RecompensaMentor) {
        recompensaDao.upsert(recompensa.copy(isPendingUpdate = true).toRecompensaMentorEntity())
    }

    override suspend fun deleteRecompensaLocal(recompensa: RecompensaMentor) {
        recompensaDao.upsert(recompensa.copy(isPendingDelete = true).toRecompensaMentorEntity())
    }
    //endregion

    //region zona
    override suspend fun getZoneInfo(zoneId: Int): Zona {
        return try {
            val response = api.obtenerMentorInfoZona(zoneId)
            if (response.isSuccessful) {
                response.body()?.let { zoneInfo ->
                    val zona = zoneInfo.toZonaDomain().copy(zonaId = zoneId)

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
            zonaId = zoneId,
            nombre = "",
            joinCode = "",
            mentorId = "",
            gemas = emptyList()
        )
    }

    override suspend fun updateZoneCode(mentorId: Int): Resource<UpdateZoneCodeResponse> {
        return try {
            val response = api.updateZoneCode(mentorId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error("No se pudo actualizar zone code: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception occurred: ${e.message}")
        }
    }

    override suspend fun updateZoneName(zoneName: String): Resource<UpdateZoneResponse> {
        return try {
            val mentorId = 0 //TODO obtener mentor id
            val request = UpdateZoneRequest(zoneName)
            val response = api.updateZoneName(mentorId, request)

            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error("No se pudo actualizar name: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Resource.Error("Exception occurred: ${e.message}")
        }
    }
    //endregion

    override suspend fun postPendingTareas(): Resource<BulkTareasResponse> {
        return try {
            val pendingCreateTareas = tareaDao.getPendingCreate()
            val pendingUpdateTareas = tareaDao.getPendingUpdate()
            val pendingDeleteTareas = tareaDao.getPendingDelete()

            val allOperations = mutableListOf<TareaOperationDto>()

            pendingCreateTareas.forEach { tarea ->
                allOperations.add(
                    TareaOperationDto(
                        accion = "create",
                        tareasGroupId = null,
                        titulo = tarea.titulo,
                        descripcion = tarea.descripcion,
                        puntos = tarea.puntos,
                        recurrente = tarea.recurrente,
                        dias = tarea.dias,
                        nombreImgVector = tarea.nombreImgVector,
                        asignada = null
                    )
                )
            }

            pendingUpdateTareas.forEach { tarea ->
                tarea.tareasGroupId?.let { groupId ->
                    allOperations.add(
                        TareaOperationDto(
                            accion = "update",
                            tareasGroupId = groupId,
                            titulo = tarea.titulo,
                            descripcion = tarea.descripcion,
                            puntos = tarea.puntos,
                            recurrente = tarea.recurrente,
                            dias = tarea.dias,
                            nombreImgVector = tarea.nombreImgVector,
                            asignada = null
                        )
                    )
                }
            }

            pendingDeleteTareas.forEach { tarea ->
                tarea.tareasGroupId?.let { groupId ->
                    allOperations.add(
                        TareaOperationDto(
                            accion = "delete",
                            tareasGroupId = groupId,
                            titulo = null,
                            descripcion = null,
                            puntos = null,
                            recurrente = null,
                            dias = null,
                            nombreImgVector = null,
                            asignada = null
                        )
                    )
                }
            }

            if (allOperations.isEmpty()) {
                return Resource.Success(BulkTareasResponse(0, 0, emptyList()))
            }

            val mentorId = pendingCreateTareas.firstOrNull()?.userInfoId
                ?: pendingUpdateTareas.firstOrNull()?.userInfoId
                ?: pendingDeleteTareas.firstOrNull()?.userInfoId
                ?: return Resource.Error("No se encontro mentorId")

            val bulkRequest = BulkTareasRequest(
                mentorId = mentorId,
                tareas = allOperations
            )

            val response = api.bulkTareas(bulkRequest)

            if (response.isSuccessful) {
                response.body()?.let { bulkResponse ->
                    bulkResponse.results.forEach { result ->
                        when (result.accion) {
                            "create" -> {
                                if (result.success && result.tareasGroupId != null) {
                                    val localTarea = pendingCreateTareas.find { it.titulo == result.titulo }
                                    localTarea?.let {
                                        val updatedTarea = it.copy(
                                            remoteId = result.tareasGroupId,
                                            tareasGroupId = result.tareasGroupId,
                                            isPendingCreate = false
                                        )
                                        tareaDao.upsert(updatedTarea)
                                    }
                                }
                            }
                            "update" -> {
                                if (result.success && result.tareasGroupId != null) {
                                    val localTarea = pendingUpdateTareas.find { it.tareasGroupId == result.tareasGroupId }
                                    localTarea?.let {
                                        val updatedTarea = it.copy(isPendingUpdate = false)
                                        tareaDao.upsert(updatedTarea)
                                    }
                                }
                            }
                            "delete" -> {
                                if (result.success && result.tareasGroupId != null) {
                                    val localTarea = pendingDeleteTareas.find { it.tareasGroupId == result.tareasGroupId }
                                    localTarea?.let {
                                        tareaDao.delete(it)
                                    }
                                }
                            }
                        }
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

    override suspend fun postPendingRecompensas(): Resource<BulkRecompensasResponse> {
        return try {
            val pendingCreateRecompensas = recompensaDao.getPendingCreate()
            val pendingUpdateRecompensas = recompensaDao.getPendingUpdate()
            val pendingDeleteRecompensas = recompensaDao.getPendingDelete()

            val allOperations = mutableListOf<RecompensaOperationDto>()

            pendingCreateRecompensas.forEach { recompensa ->
                allOperations.add(
                    RecompensaOperationDto(
                        accion = "create",
                        recompensaId = null,
                        titulo = recompensa.titulo,
                        descripcion = recompensa.descripcion,
                        precio = recompensa.precio,
                        isDisponible = recompensa.isDisponible,
                        nombreImgVector = recompensa.nombreImgVector
                    )
                )
            }

            pendingUpdateRecompensas.forEach { recompensa ->
                recompensa.remoteId?.let { remoteId ->
                    allOperations.add(
                        RecompensaOperationDto(
                            accion = "update",
                            recompensaId = remoteId,
                            titulo = recompensa.titulo,
                            descripcion = recompensa.descripcion,
                            precio = recompensa.precio,
                            isDisponible = recompensa.isDisponible,
                            nombreImgVector = recompensa.nombreImgVector
                        )
                    )
                }
            }

            pendingDeleteRecompensas.forEach { recompensa ->
                recompensa.remoteId?.let { remoteId ->
                    allOperations.add(
                        RecompensaOperationDto(
                            accion = "delete",
                            recompensaId = remoteId,
                            titulo = null,
                            descripcion = null,
                            precio = null,
                            isDisponible = null,
                            nombreImgVector = null
                        )
                    )
                }
            }

            if (allOperations.isEmpty()) {
                return Resource.Success(BulkRecompensasResponse(0, 0, emptyList()))
            }

            val mentorId = pendingCreateRecompensas.firstOrNull()?.createdBy
                ?: pendingUpdateRecompensas.firstOrNull()?.createdBy
                ?: pendingDeleteRecompensas.firstOrNull()?.createdBy
                ?: return Resource.Error("No mentorId found")

            val bulkRequest = BulkRecompensasRequest(
                mentorId = mentorId,
                recompensas = allOperations
            )

            val response = api.bulkRecompensas(bulkRequest)

            if (response.isSuccessful) {
                response.body()?.let { bulkResponse ->
                    bulkResponse.results.forEach { result ->
                        when (result.accion) {
                            "create" -> {
                                if (result.success && result.recompensaId != null) {
                                    val localRecompensa = pendingCreateRecompensas.find { it.titulo == result.titulo }
                                    localRecompensa?.let {
                                        val updatedRecompensa = it.copy(
                                            remoteId = result.recompensaId,
                                            isPendingCreate = false
                                        )
                                        recompensaDao.upsert(updatedRecompensa)
                                    }
                                }
                            }
                            "update" -> {
                                if (result.success && result.recompensaId != null) {
                                    val localRecompensa = pendingUpdateRecompensas.find { it.remoteId == result.recompensaId }
                                    localRecompensa?.let {
                                        val updatedRecompensa = it.copy(isPendingUpdate = false)
                                        recompensaDao.upsert(updatedRecompensa)
                                    }
                                }
                            }
                            "delete" -> {
                                if (result.success && result.recompensaId != null) {
                                    val localRecompensa = pendingDeleteRecompensas.find { it.remoteId == result.recompensaId }
                                    localRecompensa?.let {
                                        recompensaDao.delete(it)
                                    }
                                }
                            }
                        }
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
}
