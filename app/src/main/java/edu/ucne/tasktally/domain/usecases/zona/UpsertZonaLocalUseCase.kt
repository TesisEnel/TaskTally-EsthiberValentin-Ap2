package edu.ucne.tasktally.domain.usecases.zona

import edu.ucne.tasktally.domain.models.Zona
import edu.ucne.tasktally.domain.repository.ZonaRepository
import javax.inject.Inject

class UpsertZonaLocalUseCase @Inject constructor(
    private val repository: ZonaRepository
) {
    suspend fun invoke(zona: Zona) =
        repository.upsert(zona)
}