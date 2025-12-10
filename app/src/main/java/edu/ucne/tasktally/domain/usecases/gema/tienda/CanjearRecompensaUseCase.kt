package edu.ucne.tasktally.domain.usecases.gema.tienda

import edu.ucne.tasktally.domain.repository.GemaRepository
import javax.inject.Inject

class CanjearRecompensaUseCase @Inject constructor(
    private val repo: GemaRepository
) {
    suspend operator fun invoke(recompensaId: String, gemaId: Int) {
        repo.canjearRecompensa(recompensaId, gemaId)
    }
}