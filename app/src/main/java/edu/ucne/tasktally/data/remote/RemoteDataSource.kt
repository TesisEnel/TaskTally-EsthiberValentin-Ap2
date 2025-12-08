package edu.ucne.tasktally.data.remote

import android.util.Log
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.JoinZoneRequest
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.JoinZoneResponse
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.LeaveZoneRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.UpdateZoneCodeResponse
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: TaskTallyApi
) {
    suspend fun joinZone(gemaId: Int, zoneCode: String): JoinZoneResponse {
        try {
            val request = JoinZoneRequest(
                gemaId = gemaId,
                zoneCode = zoneCode
            )
            val response = api.joinZone(request)

            if (!response.isSuccessful) {
                Log.e("RemoteDataSource", "Error ingresando a zona: ${response.code()} - ${response.message()}")
                throw Exception("Error al ingresar a Zona: ${response.message()}")
            }

            val joinZoneResponse = response.body()
            if (joinZoneResponse == null) {
                Log.e("RemoteDataSource", "Join zone response body is null")
                throw Exception("Join zone response body is null")
            }

            if (joinZoneResponse.zoneId == null || joinZoneResponse.zoneName == null) {
                Log.e("RemoteDataSource", "Join zone response missing required fields")
                throw Exception("Join zone response missing required fields")
            }

            Log.d("RemoteDataSource", "Se ingreso correctamente a la zona: ${joinZoneResponse.zoneName} (ID: ${joinZoneResponse.zoneId})")
            return joinZoneResponse
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Exception Intentando ingresar a zona: ${e.message}", e)
            throw e
        }
    }

    suspend fun leaveZone(gemaId: Int) {
        try {
            val request = LeaveZoneRequest(gemaId = gemaId)
            val response = api.leaveZone(request)

            if (!response.isSuccessful) {
                Log.e("RemoteDataSource", "Error saliendo de zona: ${response.code()} - ${response.message()}")
                throw Exception("Error al salir de Zona: ${response.message()}")
            }

            Log.d("RemoteDataSource", "Se salio correctamente de la zona para gema ID: $gemaId")
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Exception intentando salir de zona: ${e.message}", e)
            throw e
        }
    }

    suspend fun updateZoneCode(mentorId: Int): UpdateZoneCodeResponse {
        try {
            val response = api.updateZoneCode(mentorId)

            if (!response.isSuccessful) {
                Log.e("RemoteDataSource", "Error actualizando codigo de zona: ${response.code()} - ${response.message()}")
                throw Exception("Error al actualizar codigo de zona: ${response.message()}")
            }

            val updateZoneCodeResponse = response.body()
            if (updateZoneCodeResponse == null) {
                Log.e("RemoteDataSource", "Update zone code response body is null")
                throw Exception("Update zone code response body is null")
            }

            Log.d("RemoteDataSource", "Codigo de zona actualizado correctamente para mentor ID: $mentorId")
            return updateZoneCodeResponse
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Exception intentando actualizar codigo de zona: ${e.message}", e)
            throw e
        }
    }
}