package edu.ucne.tasktally.domain.usecases.gema

import edu.ucne.tasktally.domain.repository.GemaRepository
import javax.inject.Inject

class GetTareasRemoteUseCase @Inject constructor(
    private val repo: GemaRepository
) {
    suspend operator fun invoke(gemaId: Int) = repo.getTareasRemote(gemaId)
}