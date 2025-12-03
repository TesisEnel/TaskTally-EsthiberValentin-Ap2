package edu.ucne.tasktally.presentation.mentor.recompensas.list

sealed interface ListaRecompensaUiEvent {
    data object Load : ListaRecompensaUiEvent
    data class Delete(val id: String) : ListaRecompensaUiEvent
    data object CreateNew : ListaRecompensaUiEvent
    data class Edit(val id: String) : ListaRecompensaUiEvent
    data class ShowMessage(val message: String) : ListaRecompensaUiEvent
}