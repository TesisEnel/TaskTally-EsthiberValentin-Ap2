package edu.ucne.tasktally.domain.usecases.mentor.zona

import edu.ucne.tasktally.data.remote.RemoteDataSource
import edu.ucne.tasktally.domain.repository.ZonaRepository
import javax.inject.Inject

class UpdateZoneCodeRemoteUseCase @Inject constructor(
    private val repository: ZonaRepository,
    private val remoteDataSource: RemoteDataSource
) {
    suspend operator fun invoke(zoneId: Int, mentorId: Int) {
        val response = remoteDataSource.updateZoneCode(mentorId)
        repository.updateZoneCode(zoneId, response.zoneCode)
    }
}