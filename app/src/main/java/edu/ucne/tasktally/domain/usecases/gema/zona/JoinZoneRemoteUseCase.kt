package edu.ucne.tasktally.domain.usecases.gema.zona

import android.util.Log
import edu.ucne.tasktally.data.remote.RemoteDataSource
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.remote.DTOs.gema.zone.JoinZoneResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class JoinZoneRemoteUseCase @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    operator fun invoke(gemaId: Int, zoneCode: String): Flow<Resource<JoinZoneResponse>> = flow {
        try {
            // Input validation
            if (gemaId <= 0) {
                emit(Resource.Error("ID de gema inválido"))
                return@flow
            }

            if (zoneCode.isBlank()) {
                emit(Resource.Error("El código de zona no puede estar vacío"))
                return@flow
            }

            if (zoneCode.length < 3) {
                emit(Resource.Error("El código de zona debe tener al menos 3 caracteres"))
                return@flow
            }

            Log.d("JoinZoneUseCase", "Intentando unir gema $gemaId a zona con código: $zoneCode")

            emit(Resource.Loading())
            val response = remoteDataSource.joinZone(gemaId, zoneCode.uppercase().trim())

            Log.d("JoinZoneUseCase", "Gema unida exitosamente a zona: ${response.zoneName} (ID: ${response.zoneId})")
            emit(Resource.Success(response))

        } catch (e: Exception) {
            Log.e("JoinZoneUseCase", "Error al unirse a la zona", e)
            val errorMessage = when {
                e.message?.contains("404") == true -> "Zona no encontrada. Verifica el código"
                e.message?.contains("409") == true -> "Ya perteneces a esta zona"
                e.message?.contains("403") == true -> "No tienes permisos para unirte a esta zona"
                e.message?.contains("400") == true -> "Código de zona inválido"
                e.message?.contains("network", ignoreCase = true) == true -> "Error de conexión. Verifica tu internet"
                else -> e.message ?: "Error desconocido al unirse a la zona"
            }
            emit(Resource.Error(errorMessage))
        }
    }
}