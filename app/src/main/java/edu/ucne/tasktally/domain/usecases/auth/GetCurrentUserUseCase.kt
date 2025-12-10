package edu.ucne.tasktally.domain.usecases.auth

import kotlinx.coroutines.flow.Flow
import edu.ucne.tasktally.data.repositories.AuthRepository
import edu.ucne.tasktally.data.repositories.UserData
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<UserData> {
        return authRepository.getCurrentUser()
    }
}
