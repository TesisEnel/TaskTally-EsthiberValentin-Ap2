package edu.ucne.tasktally.presentation.zona

import edu.ucne.tasktally.domain.models.Gema
import edu.ucne.tasktally.domain.models.Zona

data class ZonaUiState(
    val isLoading: Boolean = false,
    val userMessage: String? = null,

    val zona: Zona? = null,
    val gemas: List<Gema> = emptyList(),

    val zonaName: String = "",
    val isEditingName: Boolean = false,
    val showJoinCodeDialog: Boolean = false,

    val isMentor: Boolean = false,
    val currentUserId: String? = null
)
