package edu.ucne.tasktally.domain.usecase.gema

import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.repository.ZonaRepository
import javax.inject.Inject

class CheckGemaZoneAccessUseCase @Inject constructor(
    private val zonaRepository: ZonaRepository
) {
    suspend operator fun invoke(gemaId: Int, zoneId: Int): Resource<Boolean> {
        return when (val result = zonaRepository.getZoneInfoGema(gemaId, zoneId)) {
            is Resource.Success -> {
                val hasAccess = result.data != null
                Resource.Success(hasAccess)
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error al verificar acceso a la zona")
            }
            is Resource.Loading -> {
                Resource.Loading()
            }
        }
    }
}
