package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.domain.models.Tarea
import edu.ucne.tasktally.domain.repository.TareaRepository
import edu.ucne.tasktally.data.remote.Resource
import javax.inject.Inject

class CreateTareaUseCase @Inject constructor(
    private val tareaRepository: TareaRepository
) {
    suspend operator fun invoke(tarea: Tarea): Resource<Int> {
        return try {
            val id = tareaRepository.upsert(tarea)
            Resource.Success(id)
        } catch (e: Exception) {
            Resource.Error("Error al crear tarea: ${e.message}")
        }
    }
}