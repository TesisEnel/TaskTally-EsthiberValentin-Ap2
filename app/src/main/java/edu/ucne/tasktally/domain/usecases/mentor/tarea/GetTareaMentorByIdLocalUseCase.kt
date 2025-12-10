package edu.ucne.tasktally.domain.usecases.mentor.tarea

import edu.ucne.tasktally.domain.repository.MentorRepository
import javax.inject.Inject

class GetTareaMentorByIdLocalUseCase @Inject constructor(
    private val repo: MentorRepository
) {
    suspend operator fun invoke(id: String) =
        repo.getTareaById(id)
}