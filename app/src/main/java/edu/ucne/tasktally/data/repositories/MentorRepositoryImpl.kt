package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.local.DAOs.mentor.MentorDao
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
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.remote.TaskTallyApi
import edu.ucne.tasktally.domain.models.RecompensaMentor
import edu.ucne.tasktally.domain.models.TareaMentor
import edu.ucne.tasktally.domain.models.Zona
import edu.ucne.tasktally.domain.repository.MentorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class MentorRepositoryImpl @Inject constructor(
    private val mentorDao: MentorDao,
    private val zonaDao: ZonaDao,
    private val api: TaskTallyApi
) : MentorRepository {

    //region Tareas
    override fun observeTareas(): Flow<List<TareaMentor>> =
        mentorDao.observeTodasLasTareasMentor().map { list ->
            list.map { it.toTareaMentorDomain() }
        }

    override suspend fun getTareaById(id: String): TareaMentor? =
        mentorDao.obtenerTareaMentorPorId(id)?.toTareaMentorDomain()

    override suspend fun createTareaLocal(tarea: TareaMentor) {
        val tareaToSave = if (tarea.remoteId != null) {
            tarea.copy(isPendingCreate = false, isPendingUpdate = true)
        } else {
            tarea
        }
        mentorDao.upsertTarea(tareaToSave.toTareaMentorEntity())
    }

    override suspend fun updateTareaLocal(tarea: TareaMentor) {
        mentorDao.upsertTarea(tarea.copy(isPendingUpdate = true).toTareaMentorEntity())
    }

    override suspend fun deleteTareaLocal(tarea: TareaMentor) {
        mentorDao.upsertTarea(tarea.copy(isPendingDelete = true).toTareaMentorEntity())
    }

    override suspend fun deleteAllTareasLocal(mentorId: Int) {
        mentorDao.eliminarTodasLasTareas()
    }
    //endregion

    //region Recompensas
    override fun observeRecompensas(): Flow<List<RecompensaMentor>> =
        mentorDao.observeTodasLasRecompensasMentor().map { list ->
            list.map { it.toRecompensaMentorDomain() }
        }

    override suspend fun getRecompensaById(id: String): RecompensaMentor? =
        mentorDao.obtenerRecompensaMentorPorId(id)?.toRecompensaMentorDomain()

    override suspend fun createRecompensaLocal(recompensa: RecompensaMentor) {
        // If the recompensa already has a remoteId, it should be treated as an update, not create
        val recompensaToSave = if (recompensa.remoteId != null) {
            recompensa.copy(isPendingCreate = false, isPendingUpdate = true)
        } else {
            recompensa
        }
        mentorDao.upsertRecompensa(recompensaToSave.toRecompensaMentorEntity())
    }

    override suspend fun updateRecompensaLocal(recompensa: RecompensaMentor) {
        mentorDao.upsertRecompensa(
            recompensa.copy(isPendingUpdate = true).toRecompensaMentorEntity()
        )
    }

    override suspend fun deleteRecompensaLocal(recompensa: RecompensaMentor) {
        mentorDao.upsertRecompensa(
            recompensa.copy(isPendingDelete = true).toRecompensaMentorEntity()
        )
    }

    override suspend fun deleteAllRecompensasLocal() {
        mentorDao.observeTodasLasRecompensasMentor().collect { list -> // TODO REVISAR
            list.forEach {
                mentorDao.upsertRecompensa(
                    it.copy(isPendingDelete = true)
                )
            }
        }
    }
    //endregion

    //region zona
    override suspend fun getZoneInfo(mentorId: Int, zoneId: Int): Zona {
        return try {
            val response = api.obtenerMentorInfoZona(mentorId, zoneId)
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

    override suspend fun updateZoneName(
        mentorId: Int,
        zoneName: String
    ): Resource<UpdateZoneResponse> {
        return try {
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

    override suspend fun getTareasRecompensasMentor(mentorId: Int): Resource<Unit> {
        if (mentorId == 0) {
            return Resource.Error("No mentorId found")
        }
        return try {
            val response = api.getTareasRecompensasMentor(mentorId)
            if (response.isSuccessful) {

                mentorDao.eliminarTodasLasTareas()
                mentorDao.eliminarTodasLasRecompensas()

                response.body()?.let { mentorData ->
                    mentorData.tareas.forEach { tareaDto ->
                            val tarea = TareaMentor(
                                tareaId = UUID.randomUUID().toString(),
                                remoteId = tareaDto.tareasGroupId ?: 0,

                                titulo = tareaDto.titulo ?: "",
                                descripcion = tareaDto.descripcion ?: "",
                                puntos = tareaDto.puntos ?: 0,
                                dias = tareaDto.dias ?: "",
                                repetir = tareaDto.repetir ?: 0,
                                nombreImgVector = tareaDto.nombreImgVector ?: "",

                                isPendingCreate = false,
                                isPendingUpdate = false,
                                isPendingDelete = false
                            )
                            mentorDao.upsertTarea(tarea.toTareaMentorEntity())
                    }
                    mentorData.recompensas.forEach { recompensaDto ->
                            val recompensa = RecompensaMentor(
                                recompensaId = UUID.randomUUID().toString(),
                                remoteId = recompensaDto.recompensaId ?: 0,

                                titulo = recompensaDto.titulo ?: "",
                                descripcion = recompensaDto.descripcion ?: "",
                                precio = recompensaDto.precio ?: 0,
                                isDisponible = recompensaDto.isDisponible ?: true,
                                nombreImgVector = recompensaDto.nombreImgVector ?: "",

                                isPendingCreate = false,
                                isPendingUpdate = false,
                                isPendingDelete = false
                            )
                        mentorDao.upsertRecompensa(recompensa.toRecompensaMentorEntity())
                    }
                    Resource.Success(Unit)
                } ?: Resource.Error("Response body is null")
            } else {
                Resource.Error(
                    "Failed to fetch tareas and recompensas: ${
                        response.errorBody()?.string()
                    }"
                )
            }
        } catch (e: Exception) {
            Resource.Error("Exception occurred: ${e.message}")
        }
    }

    override suspend fun postPendingTareas(zoneId:Int,mentorId: Int): Resource<BulkTareasResponse> {
        if (mentorId == 0) {
            return Resource.Error("No mentorId found")
        }

        return try {
            val pendingCreateTareas = mentorDao.obtenerTareasPendientesDeCrear()
            val pendingUpdateTareas = mentorDao.obtenerTareasPendientesDeActualizar()
            val pendingDeleteTareas = mentorDao.obtenerTareasPendientesDeEliminar()

            val allOperations = mutableListOf<TareaOperationDto>()

            pendingCreateTareas.forEach { tarea ->
                allOperations.add(
                    TareaOperationDto(
                        accion = "create",
                        tareasGroupId = null,
                        titulo = tarea.titulo,
                        descripcion = tarea.descripcion,
                        puntos = tarea.puntos,
                        repetir = tarea.repetir,
                        dias = tarea.dias,
                        nombreImgVector = tarea.nombreImgVector,
                    )
                )
            }

            pendingUpdateTareas.forEach { tarea ->
                tarea.remoteId?.let { groupId ->
                    allOperations.add(
                        TareaOperationDto(
                            accion = "update",
                            tareasGroupId = groupId,
                            titulo = tarea.titulo,
                            descripcion = tarea.descripcion,
                            puntos = tarea.puntos,
                            repetir = tarea.repetir,
                            dias = tarea.dias,
                            nombreImgVector = tarea.nombreImgVector
                        )
                    )
                }
            }

            pendingDeleteTareas.forEach { tarea ->
                tarea.remoteId?.let { groupId ->
                    allOperations.add(
                        TareaOperationDto(
                            accion = "delete",
                            tareasGroupId = groupId,
                            titulo = null,
                            descripcion = null,
                            puntos = null,
                            repetir = null,
                            dias = null,
                            nombreImgVector = null
                        )
                    )
                }
            }

            if (allOperations.isEmpty()) {
                return Resource.Success(BulkTareasResponse(0, 0, emptyList()))
            }

            val bulkRequest = BulkTareasRequest(
                mentorId = mentorId,
                zoneId = zoneId,
                tareas = allOperations
            )

            val response = api.bulkTareas(bulkRequest)

            if (response.isSuccessful) {
                response.body()?.let { bulkResponse ->
                    bulkResponse.results.forEach { result ->
                        when (result.accion) {
                            "create" -> {
                                if (result.success && result.tareasGroupId != null) {
                                    val localTarea =
                                        pendingCreateTareas.find { it.titulo == result.titulo }
                                    localTarea?.let {
                                        val updatedTarea = it.copy(
                                            remoteId = result.tareasGroupId,
                                            isPendingCreate = false
                                        )
                                        mentorDao.upsertTarea(updatedTarea)
                                    }
                                }
                            }

                            "update" -> {
                                if (result.success && result.tareasGroupId != null) {
                                    val localTarea =
                                        pendingUpdateTareas.find { it.remoteId == result.tareasGroupId }
                                    localTarea?.let {
                                        val updatedTarea = it.copy(isPendingUpdate = false)
                                        mentorDao.upsertTarea(updatedTarea)
                                    }
                                }
                            }

                            "delete" -> {
                                if (result.success && result.tareasGroupId != null) {
                                    val localTarea =
                                        pendingDeleteTareas.find { it.remoteId == result.tareasGroupId }
                                    localTarea?.let {
                                        mentorDao.eliminarTarea(it)
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

    override suspend fun postPendingRecompensas(zoneId:Int, mentorId: Int): Resource<BulkRecompensasResponse> {
        if (mentorId == 0) {
            return Resource.Error("No mentorId found")
        }

        return try {
            val pendingCreateRecompensas = mentorDao.obtenerRecompensasPendientesDeCrear()
            val pendingUpdateRecompensas = mentorDao.obtenerRecompensasPendientesDeActualizar()
            val pendingDeleteRecompensas = mentorDao.obtenerRecompensasPendientesDeEliminar()

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

            val bulkRequest = BulkRecompensasRequest(
                mentorId = mentorId,
                zoneId = zoneId,
                recompensas = allOperations
            )

            val response = api.bulkRecompensas(bulkRequest)

            if (response.isSuccessful) {
                response.body()?.let { bulkResponse ->
                    bulkResponse.results.forEach { result ->
                        when (result.accion) {
                            "create" -> {
                                if (result.success && result.recompensaId != null) {
                                    val localRecompensa =
                                        pendingCreateRecompensas.find { it.titulo == result.titulo }
                                    localRecompensa?.let {
                                        val updatedRecompensa = it.copy(
                                            remoteId = result.recompensaId,
                                            isPendingCreate = false
                                        )
                                        mentorDao.upsertRecompensa(updatedRecompensa)
                                    }
                                }
                            }

                            "update" -> {
                                if (result.success && result.recompensaId != null) {
                                    val localRecompensa =
                                        pendingUpdateRecompensas.find { it.remoteId == result.recompensaId }
                                    localRecompensa?.let {
                                        val updatedRecompensa = it.copy(isPendingUpdate = false)
                                        mentorDao.upsertRecompensa(updatedRecompensa)
                                    }
                                }
                            }

                            "delete" -> {
                                if (result.success && result.recompensaId != null) {
                                    val localRecompensa =
                                        pendingDeleteRecompensas.find { it.remoteId == result.recompensaId }
                                    localRecompensa?.let {
                                        mentorDao.eliminarRecompensaMentor(it)
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
