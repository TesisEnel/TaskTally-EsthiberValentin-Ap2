package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.repository.MentorRepository
import javax.inject.Inject

class GetTareasRecompensasMentorRemoteUseCase @Inject constructor(
    private val repo: MentorRepository
) {

    suspend operator fun invoke(mentorId: Int): Resource<Unit> {
        return try {
            val result = repo.getTareasRecompensasMentor(mentorId)

            when (result) {
                is Resource.Success -> {
                    Resource.Success(Unit)
                }
                is Resource.Error -> {
                    result
                }
                is Resource.Loading -> {
                    Resource.Loading()
                }
            }
        } catch (e: Exception) {
            Resource.Error("Error en sincronización: ${e.message}")
        }
    }
}