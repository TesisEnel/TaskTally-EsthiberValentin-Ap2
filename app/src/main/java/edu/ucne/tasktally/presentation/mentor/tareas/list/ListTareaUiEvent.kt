package edu.ucne.tasktally.presentation.mentor.tareas.list

import edu.ucne.tasktally.data.remote.DTOs.mentor.TareaDto

sealed interface ListTareaUiEvent {
    data object Load : ListTareaUiEvent
    data object Refresh : ListTareaUiEvent
    data object CreateNew : ListTareaUiEvent

    data class Edit(val tarea: TareaDto) : ListTareaUiEvent
    data class ShowDeleteConfirmation(val tarea: TareaDto) : ListTareaUiEvent
    data object ConfirmDelete : ListTareaUiEvent
    data object DismissDeleteConfirmation : ListTareaUiEvent
}