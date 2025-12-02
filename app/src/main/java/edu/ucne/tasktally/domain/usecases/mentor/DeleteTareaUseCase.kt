package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.domain.repository.TareaRepository
import javax.inject.Inject

class DeleteTareaUseCase @Inject constructor(
    private val tareaRepository: TareaRepository
) {
    suspend operator fun invoke(tareaId: Int) {
        tareaRepository.deleteById(tareaId)
    }
}