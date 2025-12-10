package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.data.remote.DTOs.gema.tarea.BulkUpdateTareasResponse
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.models.RecompensaGema
import edu.ucne.tasktally.domain.models.TareaGema
import edu.ucne.tasktally.domain.models.Zona
import kotlinx.coroutines.flow.Flow

interface GemaRepository {
    //region Tareas

    suspend fun getTareasRemote(gemaId: Int): Resource<List<TareaGema>>

    fun observeTareas(): Flow<List<TareaGema>>
    suspend fun iniciarTareaGema(gemaId: Int, tareaId: String)
    suspend fun completarTareaGema(gemaId: Int, tareaId: String)
    //endregion

    //region Recompensas
    suspend fun getRecompensasRemote(gemaId: Int): Resource<List<RecompensaGema>>

    fun observeRecompensas(): Flow<List<RecompensaGema>>
    suspend fun canjearRecompensa(recompensaId: String, gemaId: Int)
    //endregion

    suspend fun getZoneInfo(gemaId: Int, zoneId: Int): Zona

    suspend fun postPendingEstadosTareas(gemaId: Int): Resource<BulkUpdateTareasResponse>
    suspend fun postPendingCanjearRecompensas(gemaId: Int): Resource<Unit>

}