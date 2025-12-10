package edu.ucne.tasktally.presentation.gema.tienda

import edu.ucne.tasktally.domain.models.RecompensaGema

data class GemaTiendaUiState(
    val isLoading: Boolean = false,
    val recompensas: List<RecompensaGema> = emptyList(),
    val errorMessage: String? = null,
    val gemaId: Int = 0,
    val zoneId: Int = 0,
    val gemaName: String = "Gema",
    val puntosDisponibles: Int = 0,

    val processingRecompensaId: String? = null,
    val recompensaToCanjear: RecompensaGema? = null,
    val showConfirmDialog: Boolean = false,

    val successMessage: String? = null
)
