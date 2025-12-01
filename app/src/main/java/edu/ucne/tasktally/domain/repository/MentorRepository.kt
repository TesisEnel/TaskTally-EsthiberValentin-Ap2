package edu.ucne.tasktally.domain.repository

import edu.ucne.tasktally.domain.models.Mentor
import kotlinx.coroutines.flow.Flow

interface MentorRepository {
    fun observeMentores(): Flow<List<Mentor>>
    suspend fun getMentor(id: Int?): Mentor?
    suspend fun upsert(mentor: Mentor): Int
    suspend fun delete(mentor: Mentor)
    suspend fun deleteById(id: Int)
}