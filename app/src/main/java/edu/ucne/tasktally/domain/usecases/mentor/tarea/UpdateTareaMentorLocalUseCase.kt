package edu.ucne.tasktally.domain.usecases.mentor.tarea

import edu.ucne.tasktally.domain.models.TareaMentor
import edu.ucne.tasktally.domain.repository.MentorRepository
import javax.inject.Inject

class UpdateTareaMentorLocalUseCase @Inject constructor(
    private val repo: MentorRepository
) {
    suspend operator fun invoke(tarea: TareaMentor) =
        repo.updateTareaLocal(tarea)
}