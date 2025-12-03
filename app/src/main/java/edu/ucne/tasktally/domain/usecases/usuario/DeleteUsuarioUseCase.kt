package edu.ucne.tasktally.domain.usecases.usuario

import edu.ucne.tasktally.domain.repository.UsuarioRepository
import edu.ucne.tasktally.data.remote.Resource
import javax.inject.Inject

class DeleteUsuarioUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke(userId: Int): Resource<Unit> {
        return try {
            usuarioRepository.deleteById(userId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error al eliminar usuario: ${e.message}")
        }
    }
}
