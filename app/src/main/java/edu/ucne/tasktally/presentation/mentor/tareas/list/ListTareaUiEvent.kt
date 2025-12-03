package edu.ucne.tasktally.presentation.mentor.tareas.list

sealed interface ListTareaUiEvent {
    data object Load : ListTareaUiEvent
    data class Delete(val id: Int) : ListTareaUiEvent
    data object CreateNew : ListTareaUiEvent
    data class Edit(val id: Int) : ListTareaUiEvent
    data class ShowMessage(val message: String) : ListTareaUiEvent
}