package edu.ucne.tasktally.domain.usecases.usuario

import edu.ucne.tasktally.domain.models.Usuario
import edu.ucne.tasktally.domain.repository.UsuarioRepository
import edu.ucne.tasktally.data.remote.Resource
import javax.inject.Inject

class GetUsuarioByEmailAndPasswordUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<Usuario> {
        return try {
            val usuario = usuarioRepository.getUsuarioByEmailAndPassword(email, password)
            if (usuario != null) {
                Resource.Success(usuario)
            } else {
                Resource.Error("Credenciales inválidas")
            }
        } catch (e: Exception) {
            Resource.Error("Error al buscar usuario: ${e.message}")
        }
    }
}
