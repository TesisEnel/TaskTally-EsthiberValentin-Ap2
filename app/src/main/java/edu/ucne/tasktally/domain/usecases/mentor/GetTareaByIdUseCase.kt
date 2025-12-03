package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.domain.models.Tarea
import edu.ucne.tasktally.domain.repository.TareaRepository
import javax.inject.Inject

class GetTareaByIdUseCase @Inject constructor(
    private val tareaRepository: TareaRepository
) {
    suspend operator fun invoke(id: String): Tarea? {
        return tareaRepository.getTarea(id)
    }
}