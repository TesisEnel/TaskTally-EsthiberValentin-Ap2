package edu.ucne.tasktally.domain.usecases.gema.zona

import android.util.Log
import edu.ucne.tasktally.data.remote.RemoteDataSource
import edu.ucne.tasktally.data.remote.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LeaveZoneRemoteUseCase @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {
    operator fun invoke(gemaId: Int): Flow<Resource<Unit>> = flow {
        try {
            if (gemaId <= 0) {
                emit(Resource.Error("ID de gema invalido"))
                return@flow
            }

            Log.d("LeaveZoneUseCase", "Intentando sacar gema $gemaId de zona")

            emit(Resource.Loading())
            remoteDataSource.leaveZone(gemaId)
            
            Log.d("LeaveZoneUseCase", "Gema $gemaId salio exitosamente de la zona")
            emit(Resource.Success(Unit))
            
        } catch (e: Exception) {
            Log.e("LeaveZoneUseCase", "Error al salir de la zona", e)
            val errorMessage = when {
                e.message?.contains("404") == true -> "No se encontro la zona actual"
                e.message?.contains("400") == true -> "No perteneces a ninguna zona"
                e.message?.contains("network", ignoreCase = true) == true -> "Error de conexion. Verifica tu internet"
                else -> e.message ?: "Error desconocido al salir de la zona"
            }
            emit(Resource.Error(errorMessage))
        }
    }
}