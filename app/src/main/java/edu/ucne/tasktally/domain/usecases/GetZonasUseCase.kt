package edu.ucne.tasktally.domain.usecases

import edu.ucne.tasktally.domain.models.Zona
import edu.ucne.tasktally.domain.repository.ZonaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetZonasUseCase @Inject constructor(
    private val repo: ZonaRepository
) {
    operator fun invoke(): Flow<List<Zona>> = repo.observeZonas()
}
