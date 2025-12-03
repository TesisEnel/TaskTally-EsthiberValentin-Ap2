package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.domain.models.Tarea
import edu.ucne.tasktally.domain.repository.TareaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveTareasUseCase @Inject constructor(
    private val tareaRepository: TareaRepository
) {
    operator fun invoke(): Flow<List<Tarea>> {
        return tareaRepository.observeTareas()
    }
}