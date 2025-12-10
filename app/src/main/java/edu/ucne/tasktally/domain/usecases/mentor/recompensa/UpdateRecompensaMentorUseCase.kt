package edu.ucne.tasktally.domain.usecases.mentor.recompensa

import edu.ucne.tasktally.domain.models.RecompensaMentor
import edu.ucne.tasktally.domain.repository.MentorRepository
import javax.inject.Inject

class UpdateRecompensaMentorUseCase @Inject constructor(
    private val repo: MentorRepository
)  {
    suspend operator fun invoke(recompensa: RecompensaMentor) =
        repo.updateRecompensaLocal(recompensa)
}