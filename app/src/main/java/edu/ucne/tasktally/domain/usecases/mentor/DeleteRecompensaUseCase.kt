package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.domain.repository.RecompensaRepository
import javax.inject.Inject

class DeleteRecompensaUseCase @Inject constructor(
    private val recompensaRepository: RecompensaRepository
) {
    suspend operator fun invoke(id: String) {
        recompensaRepository.deleteById(id)
    }
}