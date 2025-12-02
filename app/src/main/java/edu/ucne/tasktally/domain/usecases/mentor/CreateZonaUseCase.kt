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
            val id = zonaRepository.upsert(zona)
            Resource.Success(id)
        } catch (e: Exception) {
            Resource.Error("Error al crear zona: ${e.message}")
        }
    }
}