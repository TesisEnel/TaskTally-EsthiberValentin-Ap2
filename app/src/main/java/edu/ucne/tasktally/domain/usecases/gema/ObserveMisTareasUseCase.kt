package edu.ucne.tasktally.domain.usecases.gema

import edu.ucne.tasktally.domain.models.Tarea
import edu.ucne.tasktally.domain.repository.TareaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveMisTareasUseCase @Inject constructor(
    private val tareaRepository: TareaRepository
) {
    operator fun invoke(estado: String = "Asignada"): Flow<List<Tarea>> {
        return tareaRepository.observeTareas()
            .map { tareas -> tareas.filter { it.estado == estado } }
    }
}