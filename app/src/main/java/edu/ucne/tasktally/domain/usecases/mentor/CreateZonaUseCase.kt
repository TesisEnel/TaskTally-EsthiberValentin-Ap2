package edu.ucne.tasktally.domain.usecases.mentor

import edu.ucne.tasktally.domain.models.Zona
import edu.ucne.tasktally.domain.repository.ZonaRepository
import edu.ucne.tasktally.data.remote.Resource
import javax.inject.Inject

class CreateZonaUseCase @Inject constructor(
    private val zonaRepository: ZonaRepository
) {
    suspend operator fun invoke(zona: Zona): Resource<Int> {
        return try {
            Resource.Success(zonaRepository.upsert(zona))
        } catch (e: Exception) {
            Resource.Error("Error al crear zona: ${e.message}")
        }
    }
}