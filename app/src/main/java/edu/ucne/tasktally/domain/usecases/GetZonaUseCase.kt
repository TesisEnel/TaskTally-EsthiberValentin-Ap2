package edu.ucne.tasktally.domain.usecases

import edu.ucne.tasktally.domain.models.Zona
import edu.ucne.tasktally.domain.repository.ZonaRepository
import javax.inject.Inject

class GetZonaUseCase @Inject constructor(
    private val repo: ZonaRepository
) {
    suspend operator fun invoke(id: Int?): Zona? = repo.getZona(id)
}
