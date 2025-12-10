package edu.ucne.tasktally.presentation.mentor.tareas.list

import edu.ucne.tasktally.domain.models.TareaMentor

sealed interface ListTareaUiEvent {
    data object Load : ListTareaUiEvent
    data object Refresh : ListTareaUiEvent
    data object CreateNew : ListTareaUiEvent

    data class Edit(val tarea: TareaMentor) : ListTareaUiEvent
    data class ShowDeleteConfirmation(val tarea: TareaMentor) : ListTareaUiEvent
    data object ConfirmDelete : ListTareaUiEvent
    data object DismissDeleteConfirmation : ListTareaUiEvent
}