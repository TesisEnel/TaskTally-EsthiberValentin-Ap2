package edu.ucne.tasktally.domain.usecases.gema.tienda

import edu.ucne.tasktally.domain.models.RecompensaGema
import edu.ucne.tasktally.domain.repository.GemaRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetRecompensasGemaUseCase @Inject constructor(
    private val repo: GemaRepository
) {
    suspend operator fun invoke(): List<RecompensaGema> {
        return repo.observeRecompensas().first().filter { !it.canjeada }
    }
}