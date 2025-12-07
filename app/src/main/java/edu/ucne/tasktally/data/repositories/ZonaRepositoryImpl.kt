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
import kotlin.random.Random

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

    override suspend fun generateNewJoinCode(zonaId: Int): String {
        var newJoinCode: String
        do {
            newJoinCode = generateRandomCode()
        } while (dao.getByJoinCode(newJoinCode) != null)

        dao.updateJoinCode(zonaId, newJoinCode)
        return newJoinCode
    }

    private fun generateRandomCode(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..6)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }

    override suspend fun getZoneInfoMentor(mentorId: Int): Resource<ZoneInfoMentorResponse> {
        return try {
            val response = api.obtenerInformacionZona(mentorId)
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

    override suspend fun getZoneInfoGema(gemaId: Int): Resource<List<ZoneInfoGemaResponse>> {
        return try {
            val response = api.obtenerInformacionZonas(gemaId)
            if (response.isSuccessful) {
                response.body()?.let { zoneInfoList ->
                    Resource.Success(zoneInfoList)
                } ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Log.e("ZonaRepository", "Error getting gema zones info: ${response.code()} ${response.message()}")
                Resource.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("ZonaRepository", "Exception getting gema zones info", e)
            Resource.Error("Error de red: ${e.localizedMessage ?: "Ocurrió un error desconocido"}")
        }
    }
}