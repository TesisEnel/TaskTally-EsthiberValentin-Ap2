package edu.ucne.tasktally.domain.usecases.usuario

import edu.ucne.tasktally.domain.models.Usuario
import edu.ucne.tasktally.domain.repository.UsuarioRepository
import edu.ucne.tasktally.data.remote.Resource
import javax.inject.Inject

class CreateUsuarioUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(usuario: Usuario): Resource<Int> {
        return try {
            // Check if user with email already exists
            val existingUser = usuarioRepository.getUsuarioByEmail(usuario.email)
            if (existingUser != null) {
                return Resource.Error("Ya existe un usuario con este email")
            }

            val userId = usuarioRepository.upsert(usuario)
            Resource.Success(userId)
        } catch (e: Exception) {
            Resource.Error("Error al crear usuario: ${e.message}")
        }
    }
}
