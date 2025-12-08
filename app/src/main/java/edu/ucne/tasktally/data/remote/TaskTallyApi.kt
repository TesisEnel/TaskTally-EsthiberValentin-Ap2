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
import edu.ucne.tasktally.data.remote.DTOs.mentor.tareas.CreateTareaRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.tareas.UpdateTareaRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.BulkRecompensasRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.BulkRecompensasResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.recompensa.CanjearRecompensaResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.recompensa.RecompensasGemaResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.JoinZoneResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.ZoneInfoGemaResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.TareaDto
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneResponse
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.CreateRecompensaRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.RecompensaDto
import edu.ucne.tasktally.data.remote.DTOs.mentor.recompensa.UpdateRecompensaRequest
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
        @Path("mentorId") mentorId: Int ): Response<List<MentorTareasRecompensasResponse>>


    @POST("api/Mentors/{mentorId}/tareas")
    suspend fun createTarea(
        @Path("mentorId") mentorId: Int,
        @Body request: CreateTareaRequest
    ): Response<TareaDto>

    @PUT("api/Mentors/{mentorId}/tareas/{tareasGroupId}")
    suspend fun updateTarea(
        @Path("mentorId") mentorId: Int,
        @Path("tareasGroupId") tareasGroupId: Int,
        @Body request: UpdateTareaRequest
    ): Response<TareaDto>

    @DELETE("api/Mentors/{mentorId}/tareas/{tareasGroupId}")
    suspend fun deleteTarea(
        @Path("mentorId") mentorId: Int,
        @Path("tareasGroupId") tareasGroupId: Int
    ): Response<Unit>

    @POST("api/Mentors/tareas/bulk")
    suspend fun bulkTareas(@Body request: BulkTareasRequest): Response<BulkTareasResponse>

    @POST("api/Mentors/{mentorId}/recompensas")
    suspend fun createRecompensa(
        @Path("mentorId") mentorId: Int,
        @Body request: CreateRecompensaRequest
    ): Response<RecompensaDto>

    @PUT("api/Mentors/{mentorId}/recompensas/{recompensaId}")
    suspend fun updateRecompensa(
        @Path("mentorId") mentorId: Int,
        @Path("recompensaId") recompensaId: Int,
        @Body request: UpdateRecompensaRequest
    ): Response<RecompensaDto>

    @DELETE("api/Mentors/{mentorId}/recompensas/{recompensaId}")
    suspend fun deleteRecompensa(
        @Path("mentorId") mentorId: Int,
        @Path("recompensaId") recompensaId: Int
    ): Response<Unit>

    @POST("api/Mentors/recompensas/bulk")
    suspend fun bulkRecompensas(@Body request: BulkRecompensasRequest): Response<BulkRecompensasResponse>

    @POST("api/Mentors/{mentorId}/zone/update-code")
    suspend fun updateZoneCode(@Path("mentorId") mentorId: Int): Response<UpdateZoneCodeResponse>

    @PUT("api/Mentors/{mentorId}/zone/name")
    suspend fun updateZoneName(
        @Path("mentorId") mentorId: Int,
        @Body request: UpdateZoneRequest
    ): Response<UpdateZoneResponse>

    @GET("api/Mentors/Zones/{zoneId}/info")
    suspend fun obtenerMentorInfoZona(@Path("zoneId") zoneId: Int): Response<ZoneInfoMentorResponse>

    @GET("api/Gemas/Zones/{zoneId}/info")
    suspend fun obtenerGemaInfoZona(@Path("zoneId") zoneId: Int): Response<ZoneInfoGemaResponse>
//endregion
}