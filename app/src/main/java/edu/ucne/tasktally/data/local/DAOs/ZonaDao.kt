package edu.ucne.tasktally.data.local.DAOs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tasktally.data.local.entidades.ZonaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ZonaDao {
    @Query("SELECT * FROM zonas ORDER BY nombre ASC")
    fun observeAll(): Flow<List<ZonaEntity>>

    @Query("SELECT * FROM zonas WHERE zonaId = :id")
    suspend fun getById(id: Int?): ZonaEntity?

    @Query("SELECT * FROM zonas WHERE mentorId = :mentorId")
    suspend fun getByMentorId(mentorId: String): ZonaEntity?

    @Query("SELECT * FROM zonas WHERE joinCode = :joinCode")
    suspend fun getByJoinCode(joinCode: String): ZonaEntity?

    @Upsert
    suspend fun upsert(zona: ZonaEntity)

    @Delete
    suspend fun delete(zona: ZonaEntity)

    @Query("DELETE FROM zonas WHERE zonaId = :id")
    suspend fun deleteById(id: Int)

    @Query("UPDATE zonas SET joinCode = :newJoinCode WHERE zonaId = :zonaId")
    suspend fun updateJoinCode(zonaId: Int, newJoinCode: String)

    @Query("UPDATE zonas SET nombre = :newName WHERE zonaId = :zonaId")
    suspend fun updateZoneName(zonaId: Int, newName: String)
}
