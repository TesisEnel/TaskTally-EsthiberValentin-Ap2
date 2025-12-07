package edu.ucne.tasktally.presentation.gema.tareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.domain.usecases.gema.tareas.CompletarTareaUseCase
import edu.ucne.tasktally.domain.usecases.gema.tareas.GetTareasGemaLocalUseCase
import edu.ucne.tasktally.domain.usecases.gema.tareas.IniciarTareaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GemaTareasViewModel @Inject constructor(
    private val getTareasGemaLocalUseCase: GetTareasGemaLocalUseCase,
    private val iniciarTareaUseCase: IniciarTareaUseCase,
    private val completarTareaUseCase: CompletarTareaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GemaTareasUiState())
    val uiState: StateFlow<GemaTareasUiState> = _uiState.asStateFlow()

    fun onEvent(event: GemaTareasUiEvent) {
        when (event) {
            is GemaTareasUiEvent.LoadTareas -> loadTareas()
            is GemaTareasUiEvent.IniciarTarea -> iniciarTarea(event.tareaId)
            is GemaTareasUiEvent.CompletarTarea -> completarTarea(event.tareaId)
            is GemaTareasUiEvent.FilterByDay -> filterByDay(event.dia)
            is GemaTareasUiEvent.RefreshTareas -> refreshTareas()
        }
    }

    private fun loadTareas() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val tareas = getTareasGemaLocalUseCase(
                    gemaId = _uiState.value.gemaId,
                    dia = _uiState.value.selectedDay
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        tareas = tareas,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    private fun iniciarTarea(tareaId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(processingTaskId = tareaId) }
            try {
                iniciarTareaUseCase(tareaId)

                loadTareas()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al iniciar tarea: ${e.message}",
                        processingTaskId = null
                    )
                }
            } finally {
                _uiState.update { it.copy(processingTaskId = null) }
            }
        }
    }

    private fun completarTarea(tareaId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(processingTaskId = tareaId) }
            try {
                completarTareaUseCase(tareaId)
                // Recargar tareas para mostrar el cambio de estado
                loadTareas()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al completar tarea: ${e.message}",
                        processingTaskId = null
                    )
                }
            } finally {
                _uiState.update { it.copy(processingTaskId = null) }
            }
        }
    }

    private fun filterByDay(dia: String?) {
        _uiState.update { it.copy(selectedDay = dia) }
        loadTareas()
    }

    private fun refreshTareas() {
        loadTareas()
    }

    fun setGemaId(gemaId: Int) {
        _uiState.update { it.copy(gemaId = gemaId) }
        loadTareas()
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
