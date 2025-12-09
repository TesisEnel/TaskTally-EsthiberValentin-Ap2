package edu.ucne.tasktally.data.remote.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.repository.GemaRepository
import edu.ucne.tasktally.domain.repository.MentorRepository

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val mentorRepository: MentorRepository,
    private val gemaRepository: GemaRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            //region Sincronizar Mentor
            val tareasResult = mentorRepository.postPendingTareas()
            val recompensasResult = mentorRepository.postPendingRecompensas()
            //endregion

            //region SincronizarGema
            val gemaEstadosResult = gemaRepository.postPendingEstadosTareas()
            val gemaRecompensasResult = gemaRepository.postPendingCanjearRecompensas()
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