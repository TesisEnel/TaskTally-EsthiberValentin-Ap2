package edu.ucne.tasktally.domain.usecases.auth

import edu.ucne.tasktally.data.repositories.AuthRepository
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.remote.DTOs.auth.LoginResponse
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Resource<LoginResponse> {
        if (username.isBlank()) {
            return Resource.Error("Username cannot be empty")
        }

        if (password.isBlank()) {
            return Resource.Error("Password cannot be empty")
        }

        if (password.length < 6) {
            return Resource.Error("Password must be at least 6 characters long")
        }

        return authRepository.login(username.trim(), password)
    }
}
