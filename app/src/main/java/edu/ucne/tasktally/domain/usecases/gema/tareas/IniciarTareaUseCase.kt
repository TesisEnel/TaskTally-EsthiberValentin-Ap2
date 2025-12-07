package edu.ucne.tasktally.domain.usecases.gema.tareas

import edu.ucne.tasktally.domain.repository.TareaGemaRepository
import javax.inject.Inject

class IniciarTareaUseCase @Inject constructor(
    private val repository: TareaGemaRepository
) {
    suspend operator fun invoke(tareaId: String) {
        repository.iniciarTarea(tareaId)
    }
}