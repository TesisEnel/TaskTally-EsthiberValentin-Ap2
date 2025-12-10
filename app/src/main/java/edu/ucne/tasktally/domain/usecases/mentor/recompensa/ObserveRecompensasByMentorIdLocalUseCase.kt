package edu.ucne.tasktally.domain.usecases.mentor.recompensa

import edu.ucne.tasktally.domain.models.RecompensaMentor
import edu.ucne.tasktally.domain.repository.MentorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRecompensasByMentorIdLocalUseCase @Inject constructor(
    private val repo: MentorRepository
) {
    operator fun invoke(mentorId: Int): Flow<List<RecompensaMentor>> = repo.observeRecompensas()
}