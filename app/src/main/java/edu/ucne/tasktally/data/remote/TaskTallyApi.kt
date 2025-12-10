package edu.ucne.tasktally.data.remote

import edu.ucne.tasktally.data.remote.DTOs.mentor.MentorTareasRecompensasResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.recompensa.CanjearRecompensaRequest
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.JoinZoneRequest
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.LeaveZoneRequest
import edu.ucne.tasktally.data.remote.DTOs.gema.tarea.TareasGemaResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.tarea.UpdateTareaEstadoRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.tareas.BulkTareasRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.tareas.BulkTareasResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.tarea.BulkUpdateTareasResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.BulkRecompensasRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.BulkRecompensasResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.recompensa.CanjearRecompensaResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.recompensa.RecompensasGemaResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.JoinZoneResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.ZoneInfoGemaResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneCodeResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.ZoneInfoMentorResponse
import retrofit2.Response
import retrofit2.http.*

interface TaskTallyApi {
    //region Acciones Gema
    @POST("api/Gemas/join-zone")
    suspend fun joinZone(@Body request: JoinZoneRequest): Response<JoinZoneResponse>

    @POST("api/Gemas/leave-zone")
    suspend fun leaveZone(@Body request: LeaveZoneRequest): Response<Unit>

    @GET("api/Gemas/{gemaId}/tareas")
    suspend fun getTareasGema(@Path("gemaId") gemaId: Int): Response<List<TareasGemaResponse>>

    @GET("api/Gemas/{gemaId}/recompensas")
    suspend fun getRecompensasGema(@Path("gemaId") gemaId: Int): Response<List<RecompensasGemaResponse>>

    @PUT("api/Gemas/{gemaId}/tareas/bulk-update-estado")
    suspend fun bulkUpdateEstadoTareas(
        @Path("gemaId") gemaId: Int,
        @Body requests: List<UpdateTareaEstadoRequest>
    ): Response<BulkUpdateTareasResponse>

    @POST("api/Gemas/canjear-recompensa")
    suspend fun canjearRecompensa(@Body request: CanjearRecompensaRequest): Response<CanjearRecompensaResponse>
    //endregion

    //region Acciones Mentor
    @GET("api/Mentors/{mentorId}/tareas-recompensas")
    suspend fun getTareasRecompensasMentor(
        @Path("mentorId") mentorId: Int
    ): Response<MentorTareasRecompensasResponse>

    @POST("api/Mentors/tareas/bulk")
    suspend fun bulkTareas(@Body request: BulkTareasRequest): Response<BulkTareasResponse>

    @POST("api/Mentors/recompensas/bulk")
    suspend fun bulkRecompensas(@Body request: BulkRecompensasRequest): Response<BulkRecompensasResponse>

    @POST("api/Mentors/{mentorId}/zone/update-code")
    suspend fun updateZoneCode(@Path("mentorId") mentorId: Int): Response<UpdateZoneCodeResponse>

    @PUT("api/Mentors/{mentorId}/zone/name")
    suspend fun updateZoneName(
        @Path("mentorId") mentorId: Int,
        @Body request: UpdateZoneRequest
    ): Response<UpdateZoneResponse>

    @GET("api/Mentors/{mentorId}/Zones/{zoneId}/info")
    suspend fun obtenerMentorInfoZona(
        @Path("mentorId") mentorId: Int,
        @Path("zoneId") zoneId: Int
    ): Response<ZoneInfoMentorResponse>

    @GET("api/Gemas/{gemaId}/Zones/{zoneId}/info")
    suspend fun obtenerGemaInfoZona(
        @Path("gemaId") gemaId: Int,
        @Path("zoneId") zoneId: Int
    ): Response<ZoneInfoGemaResponse>
//endregion
}