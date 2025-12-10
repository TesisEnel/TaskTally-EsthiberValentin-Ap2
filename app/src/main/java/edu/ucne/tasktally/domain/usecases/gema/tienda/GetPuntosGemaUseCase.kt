package edu.ucne.tasktally.domain.usecases.gema.tienda

import edu.ucne.tasktally.domain.repository.GemaRepository
import javax.inject.Inject

class GetPuntosGemaUseCase @Inject constructor(
    private val repo: GemaRepository
) {
    suspend operator fun invoke(gemaId: Int, zoneId: Int): Int {
        val zona = repo.getZoneInfo(gemaId, zoneId)
        return zona.gemas.find { it.remoteId == gemaId }?.puntosActuales ?: 0
    }
}