package edu.ucne.tasktally.domain.usecases

import edu.ucne.tasktally.domain.models.Zona
import edu.ucne.tasktally.domain.repository.ZonaRepository
import javax.inject.Inject

class GetZonaByMentorUseCase @Inject constructor(
    private val repository: ZonaRepository
) {
    suspend operator fun invoke(mentorId: String): Zona? =
        repository.getZonaByMentor(mentorId)
}
