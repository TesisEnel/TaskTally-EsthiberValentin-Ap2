package edu.ucne.tasktally.domain.usecases.usuario

import edu.ucne.tasktally.domain.models.Usuario
import edu.ucne.tasktally.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUsuariosUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    operator fun invoke(): Flow<List<Usuario>> {
        return usuarioRepository.observeUsuarios()
    }
}
