package edu.ucne.tasktally.presentation.mentor.recompensas

sealed interface RecompensaUiEvent {
    data class OnTituloChange(val value: String) : RecompensaUiEvent
    data class OnDescripcionChange(val value: String) : RecompensaUiEvent
    data class OnPrecioChange(val value: String) : RecompensaUiEvent
    data class OnImageSelected(val imageName: String) : RecompensaUiEvent
    data object OnShowImagePicker : RecompensaUiEvent
    data object OnDismissImagePicker : RecompensaUiEvent
    data object Save : RecompensaUiEvent
    data class LoadRecompensa(val id: String) : RecompensaUiEvent
}