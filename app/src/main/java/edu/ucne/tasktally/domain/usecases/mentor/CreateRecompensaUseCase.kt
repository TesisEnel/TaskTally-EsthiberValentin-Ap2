package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.domain.models.Recompensa
import edu.ucne.tasktally.domain.repository.RecompensaRepository
import edu.ucne.tasktally.data.remote.Resource
import javax.inject.Inject

class CreateRecompensaUseCase @Inject constructor(
    private val recompensaRepository: RecompensaRepository
) {
    suspend operator fun invoke(recompensa: Recompensa): Resource<Int> {
        return try {
            Resource.Success(recompensaRepository.upsert(recompensa))
        } catch (e: Exception) {
            Resource.Error("Error al crear recompensa: ${e.message}")
        }
    }
}