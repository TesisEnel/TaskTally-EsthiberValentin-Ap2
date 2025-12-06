package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.data.remote.DTOs.mentor.MentorTareasRecompensasResponse
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.remote.TaskTallyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTareasRecompensasMentorUseCase @Inject constructor(
    private val api: TaskTallyApi
) {
    operator fun invoke(mentorId: Int): Flow<Resource<List<MentorTareasRecompensasResponse>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getTareasRecompensasMentor(mentorId)
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Error al obtener datos: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }
}