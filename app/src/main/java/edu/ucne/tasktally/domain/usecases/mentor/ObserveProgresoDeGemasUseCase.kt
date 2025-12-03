package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.domain.models.Gema
import edu.ucne.tasktally.domain.repository.GemaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveProgresoDeGemasUseCase @Inject constructor(
    private val gemaRepository: GemaRepository
) {
    operator fun invoke(): Flow<List<Gema>> {
        return gemaRepository.observeGemas()
    }
}