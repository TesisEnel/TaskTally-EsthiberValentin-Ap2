package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.MentorDao
import edu.ucne.tasktally.data.mappers.toDomain
import edu.ucne.tasktally.data.mappers.toEntity
import edu.ucne.tasktally.domain.models.Mentor
import edu.ucne.tasktally.domain.repository.MentorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MentorRepositoryImpl @Inject constructor(
    private val dao: MentorDao
) : MentorRepository {

    override fun observeMentores(): Flow<List<Mentor>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getMentor(id: String?): Mentor? =
        dao.getById(id)?.toDomain()

    override suspend fun getMentorByRemoteId(remoteId: Int?): Mentor? =
        dao.getByRemoteId(remoteId)?.toDomain()

    override suspend fun upsert(mentor: Mentor): String {
        dao.upsert(mentor.toEntity())
        return mentor.mentorId
    }

    override suspend fun delete(mentor: Mentor) {
        dao.delete(mentor.toEntity())
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    override suspend fun deleteByRemoteId(remoteId: Int) {
        dao.deleteByRemoteId(remoteId)
    }
}