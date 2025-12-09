package edu.ucne.tasktally.domain.usecases.mentor.zona

import edu.ucne.tasktally.domain.repository.MentorRepository
import javax.inject.Inject

class GetMentorZonaByIdUseCase @Inject constructor(
    private val repo: MentorRepository
) {
    suspend operator fun invoke(zoneId: Int) = repo.getZoneInfo(zoneId)
}