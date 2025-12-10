package edu.ucne.tasktally.presentation.gema.tareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.domain.usecases.auth.GetCurrentUserUseCase
import edu.ucne.tasktally.domain.usecases.gema.GetTareasRemoteUseCase
import edu.ucne.tasktally.domain.usecases.gema.tareas.CompletarTareaUseCase
import edu.ucne.tasktally.domain.usecases.gema.tareas.GetTareasGemaUseCase
import edu.ucne.tasktally.domain.usecases.gema.tareas.IniciarTareaUseCase
import edu.ucne.tasktally.domain.usecases.sync.TriggerSyncUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GemaTareasViewModel @Inject constructor(
    private val getTareasUseCase: GetTareasGemaUseCase,
    private val iniciarTareaUseCase: IniciarTareaUseCase,
    private val completarTareaUseCase: CompletarTareaUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val triggerSyncUseCase: TriggerSyncUseCase,
    private val getTareasRemoteUseCase: GetTareasRemoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GemaTareasUiState())
    val uiState: StateFlow<GemaTareasUiState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { userData ->
                val gemaId = userData.gemaId
                if (gemaId != null && gemaId != _uiState.value.gemaId) {
                    _uiState.update { it.copy(gemaId = gemaId) }
                    // Primero intentar cargar desde la API
                    loadTareasFromRemote()
                } else if (gemaId == null) {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Usuario no tiene asignado un ID de gema. Contacte al administrador."
                        )
                    }
                }
            }
        }
    }

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
                val tareas = getTareasUseCase(
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

    private fun loadTareasFromRemote() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // Intentar cargar tareas desde la API
                val result = getTareasRemoteUseCase(_uiState.value.gemaId)

                when (result) {
                    is edu.ucne.tasktally.data.remote.Resource.Success -> {
                        // Si fue exitoso, cargar las tareas locales actualizadas
                        loadTareas()
                    }
                    is edu.ucne.tasktally.data.remote.Resource.Error -> {
                        // Si falla, cargar tareas locales existentes y mostrar advertencia
                        _uiState.update {
                            it.copy(
                                errorMessage = "No se pudo sincronizar con el servidor: ${result.message}. Mostrando datos locales."
                            )
                        }
                        loadTareas()
                    }
                    is edu.ucne.tasktally.data.remote.Resource.Loading -> {
                        // Continuar con el loading
                    }
                }
            } catch (e: Exception) {
                // En caso de excepción, cargar tareas locales
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al sincronizar: ${e.message}. Mostrando datos locales."
                    )
                }
                loadTareas()
            }
        }
    }

    private fun iniciarTarea(tareaId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(processingTaskId = tareaId) }
            try {
                iniciarTareaUseCase(tareaId)

                // Sincronizar con el servidor después de iniciar
                triggerSyncUseCase()

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

                // Sincronizar con el servidor después de completar
                triggerSyncUseCase()

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

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
