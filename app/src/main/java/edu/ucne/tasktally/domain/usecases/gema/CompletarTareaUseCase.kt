package edu.ucne.tasktally.domain.usecases.gema

import edu.ucne.tasktally.domain.repository.GemaRepository
import edu.ucne.tasktally.domain.repository.TareaRepository
import edu.ucne.tasktally.data.remote.Resource
import javax.inject.Inject

class CompletarTareaUseCase @Inject constructor(
    private val gemaRepository: GemaRepository,
    private val tareaRepository: TareaRepository
) {
    suspend operator fun invoke(tareaId: String, gemaId: String, puntosGanados: Double): Resource<Unit> {
        return try {
            // Get the task and mark it as completed
            val tarea = tareaRepository.getTarea(tareaId)
            tarea?.let {
                tareaRepository.upsert(it.copy(estado = "Completada"))
            }

            // Update gem points
            val gema = gemaRepository.getGema(gemaId)
            gema?.let {
                val nuevoPuntos = it.puntosActuales + puntosGanados
                gemaRepository.upsert(it.copy(puntosActuales = nuevoPuntos))
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error al completar tarea: ${e.message}")
        }
    }
}