package edu.ucne.tasktally.domain.usecases.gema.tareas

import edu.ucne.tasktally.domain.models.TareaGema
import edu.ucne.tasktally.domain.repository.GemaRepository
import javax.inject.Inject

class GetTareasGemaUseCase @Inject constructor(
    private val repo: GemaRepository
) {
    suspend operator fun invoke(gemaId: Int, dia: String? = null): List<TareaGema> =
        repo.getTareasGemaLocal(gemaId, dia)
}