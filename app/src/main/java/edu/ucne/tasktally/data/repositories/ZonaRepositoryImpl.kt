package edu.ucne.tasktally.data.repositories

import android.util.Log
import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.ZoneInfoGemaResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.ZoneInfoMentorResponse
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.remote.TaskTallyApi
import edu.ucne.tasktally.domain.models.Zona
import edu.ucne.tasktally.domain.repository.ZonaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ZonaRepositoryImpl @Inject constructor(
    private val dao: ZonaDao,
    private val api: TaskTallyApi
) : ZonaRepository {

    override fun observeZonas(): Flow<List<Zona>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getZona(id: Int?): Zona? =
        dao.getById(id)?.toDomain()

    override suspend fun getZonaByMentor(mentorId: String): Zona? =
        dao.getByMentorId(mentorId)?.toDomain()

    override suspend fun getZonaByJoinCode(joinCode: String): Zona? =
        dao.getByJoinCode(joinCode)?.toDomain()

    override suspend fun upsert(zona: Zona): Int {
        dao.upsert(zona.toEntity())
        return zona.zonaId
    }

    override suspend fun delete(zona: Zona) {
        dao.delete(zona.toEntity())
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }

    override suspend fun updateZoneCode(zoneId: Int, newJoinCode: String) {
        dao.updateJoinCode(zoneId, newJoinCode)
    }

    override suspend fun updateZoneName(zoneId: Int, newName: String) {
        dao.updateZoneName(zoneId, newName)
    }

    override suspend fun getZoneInfoMentor(zoneId: Int): Resource<ZoneInfoMentorResponse> {
        return try {
            val response = api.obtenerMentorInfoZona(zoneId)
            if (response.isSuccessful) {
                response.body()?.let { zoneInfo ->
                    Resource.Success(zoneInfo)
                } ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Log.e("ZonaRepository", "Error getting mentor zone info: ${response.code()} ${response.message()}")
                Resource.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("ZonaRepository", "Exception getting mentor zone info", e)
            Resource.Error("Error de red: ${e.localizedMessage ?: "Ocurrió un error desconocido"}")
        }
    }

    override suspend fun getZoneInfoGema(zoneId: Int): Resource<ZoneInfoGemaResponse> {
        return try {
            val response = api.obtenerGemaInfoZona(zoneId)
            if (response.isSuccessful) {
                response.body()?.let { zoneInfo ->
                    Resource.Success(zoneInfo)
                } ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Log.e("ZonaRepository", "Error getting gema zone info: ${response.code()} ${response.message()}")
                Resource.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("ZonaRepository", "Exception getting gema zone info", e)
            Resource.Error("Error de red: ${e.localizedMessage ?: "Ocurrió un error desconocido"}")
        }
    }
}