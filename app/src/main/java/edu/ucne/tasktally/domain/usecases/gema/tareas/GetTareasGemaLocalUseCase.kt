package edu.ucne.tasktally.domain.usecases.gema.tareas

import edu.ucne.tasktally.domain.models.TareaGema
import edu.ucne.tasktally.domain.repository.TareaGemaRepository
import edu.ucne.tasktally.domain.repository.ZonaRepository
import javax.inject.Inject

class GetTareasGemaLocalUseCase @Inject constructor(
    private val repository: TareaGemaRepository
) {
    suspend operator fun invoke(gemaId: Int, dia: String? = null): List<TareaGema> =
        repository.getTareasGemaLocal(gemaId, dia)
}