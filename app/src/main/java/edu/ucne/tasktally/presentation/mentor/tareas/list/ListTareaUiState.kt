package edu.ucne.tasktally.presentation.mentor.tareas.list

import java.util.Objects

data class ListTareaUiState(
    val isLoading: Boolean = false,
    val tareas: List<Objects> = emptyList(),
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: String? = null
)
