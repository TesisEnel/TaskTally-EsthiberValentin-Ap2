package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.Mentor
import kotlinx.coroutines.flow.Flow

interface MentorRepository {
    fun observeMentores(): Flow<List<Mentor>>
    suspend fun getMentor(id: String?): Mentor?
    suspend fun getMentorByRemoteId(remoteId: Int?): Mentor?
    suspend fun upsert(mentor: Mentor): String
    suspend fun delete(mentor: Mentor)
    suspend fun deleteById(id: String)
    suspend fun deleteByRemoteId(remoteId: Int)
}