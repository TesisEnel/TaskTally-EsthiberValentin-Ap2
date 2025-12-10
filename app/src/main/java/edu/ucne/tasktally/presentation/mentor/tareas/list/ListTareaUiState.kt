package edu.ucne.tasktally.presentation.mentor.tareas.list

import edu.ucne.tasktally.domain.models.TareaMentor

data class ListTareaUiState(
    val isLoading: Boolean = false,
    val tareas: List<TareaMentor> = emptyList(),
    val error: String? = null,
    val message: String? = null,
    val mentorName: String = "Mentor",

    val navigateToCreate: Boolean = false,
    val navigateToEdit: TareaMentor? = null,

    val tareaToDelete: TareaMentor? = null,
    val isDeleting: Boolean = false
)