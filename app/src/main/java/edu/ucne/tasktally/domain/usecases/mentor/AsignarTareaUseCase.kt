package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.domain.models.Progreso
import edu.ucne.tasktally.domain.repository.ProgresoRepository
import edu.ucne.tasktally.data.remote.Resource
import javax.inject.Inject

class AsignarTareaUseCase @Inject constructor(
    private val progresoRepository: ProgresoRepository
) {
    suspend operator fun invoke(progreso: Progreso): Resource<Int> {
        return try {
            val id = progresoRepository.upsert(progreso)
            Resource.Success(id)
        } catch (e: Exception) {
            Resource.Error("Error al asignar tarea a la gema: ${e.message}")
        }
    }
}