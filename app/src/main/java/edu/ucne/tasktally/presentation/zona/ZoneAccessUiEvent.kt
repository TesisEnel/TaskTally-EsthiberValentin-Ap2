package edu.ucne.tasktally.presentation.zona

sealed interface ZoneAccessUiEvent {
    data class OnZoneCodeChange(val zoneCode: String) : ZoneAccessUiEvent
    object JoinZone : ZoneAccessUiEvent
    object UserMessageShown : ZoneAccessUiEvent
}
