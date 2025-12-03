package edu.ucne.tasktally.domain.usecases.gema

import edu.ucne.tasktally.domain.models.Recompensa
import edu.ucne.tasktally.domain.repository.RecompensaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveRecompensasDispoUseCase @Inject constructor(
    private val recompensaRepository: RecompensaRepository
) {
    operator fun invoke(): Flow<List<Recompensa>> {
        return recompensaRepository.observeRecompensas()
    }
}