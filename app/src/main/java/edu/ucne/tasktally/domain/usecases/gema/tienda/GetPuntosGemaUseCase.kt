package edu.ucne.tasktally.domain.usecases.gema.tienda

import edu.ucne.tasktally.data.local.preferences.AuthPreferencesManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetPuntosGemaUseCase @Inject constructor(
    private val authPreferencesManager: AuthPreferencesManager
) {
    suspend operator fun invoke(gemaId: Int, zoneId: Int): Int {
        return authPreferencesManager.puntosDisponibles.first() ?: 0
    }
}