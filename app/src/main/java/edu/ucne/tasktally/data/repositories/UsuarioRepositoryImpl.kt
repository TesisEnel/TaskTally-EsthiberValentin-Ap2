package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.UsuarioDao
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.domain.models.Usuario
import edu.ucne.tasktally.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsuarioRepositoryImpl @Inject constructor(
    private val dao: UsuarioDao
) : UsuarioRepository {

    override fun observeUsuarios(): Flow<List<Usuario>> =
        dao.observeAll().map { list ->
            list.map { usuarioEntity -> usuarioEntity.toDomain() }
        }

    override suspend fun getUsuario(userId: Int): Usuario? =
        dao.getById(userId)?.toDomain()

    override suspend fun getUsuarioByEmailAndPassword(email: String, password: String): Usuario? =
        dao.getByEmailAndPassword(email, password)?.toDomain()

    override suspend fun getUsuarioByEmail(email: String): Usuario? =
        dao.getByEmail(email)?.toDomain()

    override suspend fun upsert(usuario: Usuario): Int {
        dao.upsert(usuario.toEntity())
        return usuario.userId
    }

    override suspend fun delete(usuario: Usuario) {
        dao.delete(usuario.toEntity())
    }

    override suspend fun deleteById(userId: Int) {
        dao.deleteById(userId)
    }
}
