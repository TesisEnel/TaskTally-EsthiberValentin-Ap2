package edu.ucne.tasktally.domain.usecases.gema.tareas

import edu.ucne.tasktally.domain.repository.GemaRepository
import javax.inject.Inject

class CompletarTareaUseCase @Inject constructor(
    private val repo: GemaRepository
) {
    suspend operator fun invoke(tareaId: String, gemaId: Int) =
        repo.completarTareaGema(gemaId, tareaId)
}