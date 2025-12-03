package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.Usuario
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    fun observeUsuarios(): Flow<List<Usuario>>
    suspend fun getUsuario(userId: Int): Usuario?
    suspend fun getUsuarioByEmailAndPassword(email: String, password: String): Usuario?
    suspend fun getUsuarioByEmail(email: String): Usuario?
    suspend fun upsert(usuario: Usuario): Int
    suspend fun delete(usuario: Usuario)
    suspend fun deleteById(userId: Int)
}
