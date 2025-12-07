package edu.ucne.tasktally.domain.usecases.gema.zona

import edu.ucne.tasktally.data.remote.DTOs.gema.zone.ZoneInfoGemaResponse
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.repository.ZonaRepository
import javax.inject.Inject

class GetZoneInfoGemaUseCase @Inject constructor(
    private val zonaRepository: ZonaRepository
) {
    suspend operator fun invoke(gemaId: Int): Resource<List<ZoneInfoGemaResponse>> {
        return zonaRepository.getZoneInfoGema(gemaId)
    }
}
