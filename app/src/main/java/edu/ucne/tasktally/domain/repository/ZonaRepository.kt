package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.data.remote.DTOs.gema.zone.ZoneInfoGemaResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.ZoneInfoMentorResponse
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.models.Zona
import kotlinx.coroutines.flow.Flow

interface ZonaRepository {
    fun observeZonas(): Flow<List<Zona>>
    suspend fun getZona(id: Int?): Zona?
    suspend fun getZonaByMentor(mentorId: String): Zona?
    suspend fun getZonaByJoinCode(joinCode: String): Zona?
    suspend fun upsert(zona: Zona): Int
    suspend fun delete(zona: Zona)
    suspend fun deleteById(id: Int)
    suspend fun generateNewJoinCode(zonaId: Int): String

    suspend fun getZoneInfoMentor(mentorId: Int): Resource<ZoneInfoMentorResponse>
    suspend fun getZoneInfoGema(gemaId: Int): Resource<List<ZoneInfoGemaResponse>>
}
