package edu.ucne.tasktally.domain.usecases.auth

import kotlinx.coroutines.flow.Flow
import edu.ucne.tasktally.data.repositories.AuthRepository
import javax.inject.Inject

class GetAuthStatusUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return authRepository.isLoggedIn()
    }
}
