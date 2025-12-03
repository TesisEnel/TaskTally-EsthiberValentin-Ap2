package edu.ucne.tasktally.domain.usecases.gema

import edu.ucne.tasktally.domain.models.Recompensa
import edu.ucne.tasktally.domain.repository.RecompensaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveRecompensasCanjeadasUseCase @Inject constructor(
    private val recompensaRepository: RecompensaRepository
) {
    operator fun invoke(gemaId: String): Flow<List<Recompensa>> {
        // Since we don't have a transaction system, return empty list for now
        // This could be implemented with a proper transaction/canje entity later
        return flowOf(emptyList())
    }
}