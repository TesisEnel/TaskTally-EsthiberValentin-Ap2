package edu.ucne.tasktally.data.repositories

import edu.ucne.tasktally.data.local.DAOs.MentorDao
import edu.ucne.tasktally.data.mappers.toMentorDomain
import edu.ucne.tasktally.data.mappers.toMentorEntity
import edu.ucne.tasktally.domain.models.Mentor
import edu.ucne.tasktally.domain.repository.MentorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MentorRepositoryImpl @Inject constructor(
    private val dao: MentorDao
) : MentorRepository {

    override fun observeMentores(): Flow<List<Mentor>> =
        dao.observeAll().map { list -> list.map { it.toMentorDomain() } }

    override suspend fun getMentor(id: String?): Mentor? =
        dao.getById(id)?.toMentorDomain()

    override suspend fun getMentorByRemoteId(remoteId: Int?): Mentor? =
        dao.getByRemoteId(remoteId)?.toMentorDomain()

    override suspend fun upsert(mentor: Mentor): String {
        dao.upsert(mentor.toMentorEntity())
        return mentor.mentorId
    }

    override suspend fun delete(mentor: Mentor) {
        dao.delete(mentor.toMentorEntity())
    }

    override suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

}