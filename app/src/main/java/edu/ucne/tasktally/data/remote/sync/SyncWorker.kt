package edu.ucne.tasktally.data.remote.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.repositories.AuthRepository
import edu.ucne.tasktally.domain.repository.GemaRepository
import edu.ucne.tasktally.domain.repository.MentorRepository
import kotlinx.coroutines.flow.first

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val mentorRepository: MentorRepository,
    private val gemaRepository: GemaRepository,
    private val authRepository: AuthRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            // userData
            val userData = authRepository.getCurrentUser().first()
            val zoneId = userData.zoneId ?: 0;
            val mentorId = userData.mentorId ?: 0;
            val gemaId = userData.gemaId ?: 0;


            //region Sincronizar Mentor
            val tareasResult = mentorRepository.postPendingTareas(zoneId,mentorId)
            val recompensasResult = mentorRepository.postPendingRecompensas(zoneId,mentorId)
            //endregion

            //region SincronizarGema
            val gemaEstadosResult = gemaRepository.postPendingEstadosTareas(gemaId)
            val gemaRecompensasResult = gemaRepository.postPendingCanjearRecompensas(gemaId)
            //endregion

            val allSuccessful = listOf(
                tareasResult,
                recompensasResult,
                gemaEstadosResult,
                gemaRecompensasResult
            ).all { it is Resource.Success }
            when {
                allSuccessful -> Result.success()
                listOf(tareasResult, recompensasResult, gemaEstadosResult, gemaRecompensasResult)
                    .any { it is Resource.Error } -> Result.retry()

                else -> Result.failure()
            }
        } catch (_: Exception) {
            Result.failure()
        }
    }
}