package edu.ucne.tasktally.presentation.mentor.recompensas.list

data class ListRecompensaUiState(
    val isLoading: Boolean = false,
    val recompensas: List<Recompensa> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: String? = null
)