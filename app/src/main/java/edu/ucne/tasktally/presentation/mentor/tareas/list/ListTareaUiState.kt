package edu.ucne.tasktally.presentation.mentor.tareas.list

data class ListTareaUiState(
    val isLoading: Boolean = false,
    val tareas: List<Tarea> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: String? = null
)
