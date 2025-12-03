package edu.ucne.tasktally.presentation.mentor.recompensas.list

sealed interface ListRecompensaUiEvent {
    data object Load : ListRecompensaUiEvent
    data class Delete(val id: String) : ListRecompensaUiEvent
    data object CreateNew : ListRecompensaUiEvent
    data class Edit(val id: String) : ListRecompensaUiEvent
    data class ShowMessage(val message: String) : ListRecompensaUiEvent
}