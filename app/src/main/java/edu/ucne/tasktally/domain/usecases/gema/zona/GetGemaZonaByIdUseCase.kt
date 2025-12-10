package edu.ucne.tasktally.domain.usecases.gema.zona

import edu.ucne.tasktally.domain.repository.GemaRepository
import javax.inject.Inject

class GetGemaZonaByIdUseCase @Inject constructor(
    private val repo: GemaRepository
) {
    suspend operator fun invoke(gemaId: Int, zoneId: Int) = repo.getZoneInfo(gemaId, zoneId)
}