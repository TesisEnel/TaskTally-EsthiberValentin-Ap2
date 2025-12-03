package edu.ucne.tasktally.domain.usecases.gema

import edu.ucne.tasktally.domain.repository.GemaRepository
import edu.ucne.tasktally.data.remote.Resource
import javax.inject.Inject

class CompletarTareaUseCase @Inject constructor(
    private val progresoRepository: ProgresoRepository,
    private val gemaRepository: GemaRepository
) {
    suspend operator fun invoke(progreso: Progreso): Resource<Unit> {
        return try {
            progresoRepository.upsert(progreso)

            val gema = gemaRepository.getGema(progreso.gemaId)
            gema?.let {
                val total = it.puntosActuales + progreso.puntosGanados
                gemaRepository.upsert(it.copy(puntosActuales = total, puntosTotales = it.puntosTotales + progreso.puntosGanados))
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error al completar tarea: ${e.message}")
        }
    }
}