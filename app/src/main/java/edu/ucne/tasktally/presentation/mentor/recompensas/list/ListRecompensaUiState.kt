package edu.ucne.tasktally.presentation.mentor.recompensas.list

import edu.ucne.tasktally.domain.models.RecompensaMentor

data class ListRecompensaUiState(
    val isLoading: Boolean = false,
    val recompensas: List<RecompensaMentor> = emptyList(),
    val error: String? = null,
    val message: String? = null,
    val mentorName: String = "Mentor",

    val navigateToCreate: Boolean = false,
    val navigateToEdit: RecompensaMentor? = null,

    val recompensaToDelete: RecompensaMentor? = null,
    val isDeleting: Boolean = false
)