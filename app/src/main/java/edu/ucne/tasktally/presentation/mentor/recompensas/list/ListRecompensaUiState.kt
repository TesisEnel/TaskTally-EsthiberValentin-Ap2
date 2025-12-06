package edu.ucne.tasktally.presentation.mentor.recompensas.list

import java.util.Objects

data class ListRecompensaUiState(
    val isLoading: Boolean = false,
    val recompensas: List<Objects> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: String? = null
)