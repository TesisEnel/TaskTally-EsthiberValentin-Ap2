package edu.ucne.tasktally.presentation.gema.tienda

import edu.ucne.tasktally.domain.models.RecompensaGema

interface GemaTiendaUiEvent {
    data object LoadRecompensas : GemaTiendaUiEvent
    data object RefreshRecompensas : GemaTiendaUiEvent
    data class ShowCanjearConfirmation(val recompensa: RecompensaGema) : GemaTiendaUiEvent
    data object ConfirmCanjear : GemaTiendaUiEvent
    data object DismissCanjearConfirmation : GemaTiendaUiEvent
    data object ClearError : GemaTiendaUiEvent
    data object ClearSuccessMessage : GemaTiendaUiEvent
}