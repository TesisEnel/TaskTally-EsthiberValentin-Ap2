package edu.ucne.tasktally.domain.usecases.zona

import edu.ucne.tasktally.domain.repository.ZonaRepository
import javax.inject.Inject

class UpdateZoneNameLocalUseCase @Inject constructor(
    private val repository: ZonaRepository
) {
    suspend fun  invoke(zoneId: Int, newName: String) =
        repository.updateZoneName(zoneId, newName)
}