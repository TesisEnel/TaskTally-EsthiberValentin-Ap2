package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.domain.models.Progreso
import edu.ucne.tasktally.domain.repository.ProgresoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveProgresoDeGemasUseCase @Inject constructor(
    private val progresoRepository: ProgresoRepository
) {
    operator fun invoke(mentorId: Int): Flow<List<Progreso>> {
        return progresoRepository.observeProgresos()
            .map { progresos ->
                progresos.filter { it.gemaId != null }
            }
    }
}