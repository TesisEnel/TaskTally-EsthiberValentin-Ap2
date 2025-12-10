package edu.ucne.tasktally.domain.usecases.auth

import edu.ucne.tasktally.data.repositories.AuthRepository
import edu.ucne.tasktally.data.remote.Resource
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return authRepository.logout()
    }
}
