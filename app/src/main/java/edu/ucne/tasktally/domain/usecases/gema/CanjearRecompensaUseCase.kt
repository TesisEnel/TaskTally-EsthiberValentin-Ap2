package edu.ucne.tasktally.domain.usecases.gema

import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.repository.GemaRepository
import edu.ucne.tasktally.domain.repository.RecompensaRepository
import javax.inject.Inject

class CanjearRecompensaUseCase @Inject constructor(
    private val gemaRepository: GemaRepository,
    private val recompensaRepository: RecompensaRepository
) {
    suspend operator fun invoke(gemaId: String, recompensaId: String): Resource<Unit> {
        return try {
            val gema = gemaRepository.getGema(gemaId)
            val recompensa = recompensaRepository.getRecompensa(recompensaId)

            if (gema == null || recompensa == null)
                return Resource.Error("Datos inválidos para canje")

            if (gema.puntosActuales < recompensa.precio)
                return Resource.Error("No tienes suficientes puntos")

            // Deduct points from gema
            val nuevaGema = gema.copy(puntosActuales = gema.puntosActuales - recompensa.precio)
            gemaRepository.upsert(nuevaGema)

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error al canjear recompensa: ${e.message}")
        }
    }
}