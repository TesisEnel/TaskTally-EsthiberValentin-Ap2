package edu.ucne.tasktally.domain.usecases.mentor.zona

import edu.ucne.tasktally.data.remote.DTOs.mentor.zone.ZoneInfoMentorResponse
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.repository.ZonaRepository
import javax.inject.Inject

class GetZoneInfoMentorUseCase @Inject constructor(
    private val zonaRepository: ZonaRepository
) {
    suspend operator fun invoke(mentorId: Int): Resource<ZoneInfoMentorResponse> {
        return zonaRepository.getZoneInfoMentor(mentorId)
    }
}
