package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.data.remote.DTOs.mentor.tareas.BulkTareasResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.BulkRecompensasResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneCodeResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneResponse
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.models.RecompensaMentor
import edu.ucne.tasktally.domain.models.TareaMentor
import edu.ucne.tasktally.domain.models.Zona
import kotlinx.coroutines.flow.Flow


interface MentorRepository {
    //region Tareas
    fun observeTareas(): Flow<List<TareaMentor>>
    suspend fun getTareaById(id: String): TareaMentor?
    suspend fun createTareaLocal(tarea: TareaMentor)
    suspend fun updateTareaLocal(tarea: TareaMentor)
    suspend fun deleteTareaLocal(tarea: TareaMentor)
    suspend fun deleteAllTareasLocal(mentorId: Int)
    //endregion

    //region Recompensas
    fun observeRecompensas(): Flow<List<RecompensaMentor>>
    suspend fun getRecompensaById(id: String): RecompensaMentor?
    suspend fun createRecompensaLocal(recompensa: RecompensaMentor)
    suspend fun updateRecompensaLocal(recompensa: RecompensaMentor)
    suspend fun deleteRecompensaLocal(recompensa: RecompensaMentor)
    suspend fun deleteAllRecompensasLocal()
    //endregion

    //region zona
    suspend fun getZoneInfo(mentorId: Int, zoneId: Int): Zona
    suspend fun updateZoneCode(mentorId: Int): Resource<UpdateZoneCodeResponse>
    suspend fun updateZoneName(mentorId: Int, zoneName: String): Resource<UpdateZoneResponse>
    //endregion

    suspend fun getTareasRecompensasMentor(mentorId: Int): Resource<Unit>
    suspend fun postPendingTareas(zoneId: Int, mentorId: Int): Resource<BulkTareasResponse>
    suspend fun postPendingRecompensas(
        zoneId: Int,
        mentorId: Int
    ): Resource<BulkRecompensasResponse>
}