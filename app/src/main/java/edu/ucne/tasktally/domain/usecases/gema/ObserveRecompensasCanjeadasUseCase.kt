package edu.ucne.tasktally.domain.usecases.gema

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveRecompensasCanjeadasUseCase @Inject constructor(
    private val repository: TransaccionRecompensaRepository
) {
    operator fun invoke(gemaId: Int): Flow<List<TransaccionRecompensa>> {
        return repository.observeTransacciones()
            .map { list -> list.filter { it.gemaId == gemaId } }
    }
}