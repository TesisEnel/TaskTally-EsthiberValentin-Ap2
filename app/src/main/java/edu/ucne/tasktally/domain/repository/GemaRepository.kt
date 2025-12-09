package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.data.remote.DTOs.gema.tarea.BulkUpdateTareasResponse
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.models.RecompensaGema
import edu.ucne.tasktally.domain.models.TareaGema
import edu.ucne.tasktally.domain.models.Zona
import kotlinx.coroutines.flow.Flow

interface GemaRepository {
    //region Tareas
    fun observeTareas(): Flow<List<TareaGema>>
    suspend fun getTareasGemaLocal(gemaId: Int, dia: String?): List<TareaGema>
    suspend fun iniciarTareaGema(tareaId: String)
    suspend fun completarTareaGema(tareaId: String)
    //endregion

    //region Recompensas
    fun observeRecompensas(): Flow<List<RecompensaGema>>
    suspend fun canjearRecompensa(recompensaId: String, gemaId: Int)
    //endregion

    suspend fun getZoneInfo(zoneId: Int) : Zona

    suspend fun postPendingEstadosTareas(): Resource<BulkUpdateTareasResponse>
    suspend fun postPendingCanjearRecompensas(): Resource<Unit>

}