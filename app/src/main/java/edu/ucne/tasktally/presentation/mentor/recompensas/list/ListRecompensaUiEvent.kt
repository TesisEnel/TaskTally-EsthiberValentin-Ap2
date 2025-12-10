package edu.ucne.tasktally.presentation.mentor.recompensas.list

import edu.ucne.tasktally.domain.models.RecompensaMentor

sealed interface ListRecompensaUiEvent {
    data object Load : ListRecompensaUiEvent
    data object Refresh : ListRecompensaUiEvent
    data object CreateNew : ListRecompensaUiEvent

    data class Edit(val recompensa: RecompensaMentor) : ListRecompensaUiEvent
    data class ShowDeleteConfirmation(val recompensa: RecompensaMentor) : ListRecompensaUiEvent
    data object ConfirmDelete : ListRecompensaUiEvent
    data object DismissDeleteConfirmation : ListRecompensaUiEvent
}