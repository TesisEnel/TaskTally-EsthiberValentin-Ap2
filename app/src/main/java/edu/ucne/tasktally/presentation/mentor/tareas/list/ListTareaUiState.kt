package edu.ucne.tasktally.presentation.mentor.tareas.list

import edu.ucne.tasktally.data.remote.DTOs.mentor.TareaDto

data class ListTareaUiState(
    val isLoading: Boolean = false,
    val tareas: List<TareaDto> = emptyList(),
    val error: String? = null,
    val message: String? = null,
    val mentorName: String = "Mentor",

    val navigateToCreate: Boolean = false,
    val navigateToEdit: TareaDto? = null,

    val tareaToDelete: TareaDto? = null,
    val isDeleting: Boolean = false
)