package edu.ucne.tasktally.domain.usecases.gema.tareas

import edu.ucne.tasktally.domain.models.TareaGema
import edu.ucne.tasktally.domain.repository.GemaRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTareasGemaUseCase @Inject constructor(
    private val repo: GemaRepository
) {
    suspend operator fun invoke(gemaId: Int, dia: String? = null): List<TareaGema> {
        val todasLasTareas = repo.observeTareas().first()

        return if (dia != null) {
            todasLasTareas.filter { it.dia == dia }
        } else {
            todasLasTareas
        }
    }
}