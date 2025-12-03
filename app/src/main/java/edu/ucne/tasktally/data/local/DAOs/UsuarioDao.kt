package edu.ucne.tasktally.data.local.DAOs

import androidx.room.*
import edu.ucne.tasktally.data.local.entidades.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios ORDER BY userId DESC")
    fun observeAll(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE userId = :userId")
    suspend fun getById(userId: Int): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password")
    suspend fun getByEmailAndPassword(email: String, password: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE email = :email")
    suspend fun getByEmail(email: String): UsuarioEntity?

    @Upsert
    suspend fun upsert(usuario: UsuarioEntity)

    @Delete
    suspend fun delete(usuario: UsuarioEntity)

    @Query("DELETE FROM usuarios WHERE userId = :userId")
    suspend fun deleteById(userId: Int)
}
