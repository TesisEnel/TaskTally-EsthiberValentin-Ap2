package edu.ucne.tasktally.domain.usecases.gema

import edu.ucne.tasktally.domain.models.Tarea
import edu.ucne.tasktally.domain.repository.TareaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveMisTareasUseCase @Inject constructor(
    private val tareaRepository: TareaRepository,
    private val progresoRepository: ProgresoRepository
) {
    operator fun invoke(gemaId: Int): Flow<List<Tarea>> {
        return combine(
            tareaRepository.observeTareas(),
            progresoRepository.observeProgresos()
        ) { tareas, progresos ->
            val tareasDeLaGemaIds = progresos
                .filter { it.gemaId == gemaId }
                .map { it.tareaId }

            tareas.filter { it.tareaId in tareasDeLaGemaIds }
        }
    }
}