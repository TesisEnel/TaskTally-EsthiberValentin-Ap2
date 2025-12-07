package edu.ucne.tasktally.presentation.gema.tareas

sealed interface GemaTareasUiEvent {
    data object LoadTareas : GemaTareasUiEvent
    data class IniciarTarea(val tareaId: String) : GemaTareasUiEvent
    data class CompletarTarea(val tareaId: String) : GemaTareasUiEvent
    data class FilterByDay(val dia: String?) : GemaTareasUiEvent
    data object RefreshTareas : GemaTareasUiEvent
}