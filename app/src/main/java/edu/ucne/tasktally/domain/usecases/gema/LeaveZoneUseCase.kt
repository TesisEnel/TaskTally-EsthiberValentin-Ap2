package edu.ucne.tasktally.domain.usecases.gema

import edu.ucne.tasktally.data.remote.DTOs.gema.zone.LeaveZoneRequest
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.remote.TaskTallyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LeaveZoneUseCase @Inject constructor(
    private val api: TaskTallyApi
) {
    operator fun invoke(gemaId: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.leaveZone(LeaveZoneRequest(gemaId))
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Error al salir de la zona: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }
}