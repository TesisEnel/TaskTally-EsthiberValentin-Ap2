package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.domain.models.Tarea
import edu.ucne.tasktally.domain.repository.TareaRepository
import edu.ucne.tasktally.data.remote.Resource
import javax.inject.Inject
import android.util.Log

class CreateTareaUseCase @Inject constructor(
    private val tareaRepository: TareaRepository
) {
    suspend operator fun invoke(tarea: Tarea): Resource<String> {
        return try {
            Log.d("CreateTareaUseCase", "Creating tarea: $tarea")
            val id = tareaRepository.upsert(tarea)
            Log.d("CreateTareaUseCase", "Task created successfully with ID: $id")
            Resource.Success(id)
        } catch (e: Exception) {
            Log.e("CreateTareaUseCase", "Error creating task: ${e.message}", e)
            Resource.Error("Error al crear tarea: ${e.message}")
        }
    }
}