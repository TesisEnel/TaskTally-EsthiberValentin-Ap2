package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.domain.models.Recompensa
import edu.ucne.tasktally.domain.repository.RecompensaRepository
import javax.inject.Inject

class GetRecompensaByIdUseCase @Inject constructor(
    private val recompensaRepository: RecompensaRepository
) {
    suspend operator fun invoke(id: String): Recompensa? {
        return recompensaRepository.getRecompensa(id)
    }
}