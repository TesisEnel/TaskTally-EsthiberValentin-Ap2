package edu.ucne.tasktally.domain.usecases.mentor.tarea

import edu.ucne.tasktally.domain.repository.MentorRepository
import javax.inject.Inject

class ObserveTareasByMentorIdLocalUseCase @Inject constructor(
    private val repo: MentorRepository
)  {
    operator fun invoke(mentorId: Int) = repo.observeTareasByMentor(mentorId)
}

