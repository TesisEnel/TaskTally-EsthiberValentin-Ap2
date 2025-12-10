package edu.ucne.tasktally.presentation.gema.tareas

import edu.ucne.tasktally.domain.models.TareaGema

data class GemaTareasUiState(
    val isLoading: Boolean = false,
    val tareas: List<TareaGema> = emptyList(),
    val errorMessage: String? = null,
    val selectedDay: String? = null,
    val gemaId: Int = 0,
    val processingTaskId: String? = null
)
