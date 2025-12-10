package edu.ucne.tasktally.domain.usecases.gema.tareas

import edu.ucne.tasktally.domain.repository.GemaRepository
import javax.inject.Inject

class IniciarTareaUseCase @Inject constructor(
    private val repo: GemaRepository
) {
    suspend operator fun invoke(tareaId: String, gemaId: Int) =
        repo.iniciarTareaGema(gemaId, tareaId)
}