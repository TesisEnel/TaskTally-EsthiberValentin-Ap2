package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.RachaDao
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.domain.models.Racha
import edu.ucne.tasktally.domain.repository.RachaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RachaRepositoryImpl @Inject constructor(
    private val dao: RachaDao
) : RachaRepository {

    override fun observeRachas(): Flow<List<Racha>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getRacha(id: Int?): Racha? =
        dao.getById(id)?.toDomain()

    override suspend fun upsert(racha: Racha): Int {
        dao.upsert(racha.toEntity())
        return racha.rachaId
    }

    override suspend fun delete(racha: Racha) {
        dao.delete(racha.toEntity())
    }

    override suspend fun deleteById(id: Int) {
        dao.deleteById(id)
    }
}
