package edu.ucne.tasktally.presentation.gema.tienda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.models.RecompensaGema
import edu.ucne.tasktally.domain.usecases.auth.GetCurrentUserUseCase
import edu.ucne.tasktally.domain.usecases.gema.GetRecompensasRemoteUseCase
import edu.ucne.tasktally.domain.usecases.gema.tienda.CanjearRecompensaUseCase
import edu.ucne.tasktally.domain.usecases.gema.tienda.GetPuntosGemaUseCase
import edu.ucne.tasktally.domain.usecases.gema.tienda.GetRecompensasGemaUseCase
import edu.ucne.tasktally.domain.usecases.sync.TriggerSyncUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GemaTiendaViewModel @Inject constructor(
    private val getRecompensasGemaUseCase: GetRecompensasGemaUseCase,
    private val getRecompensasRemoteUseCase: GetRecompensasRemoteUseCase,
    private val canjearRecompensaUseCase: CanjearRecompensaUseCase,
    private val getPuntosGemaUseCase: GetPuntosGemaUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val triggerSyncUseCase: TriggerSyncUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GemaTiendaUiState())
    val uiState: StateFlow<GemaTiendaUiState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { userData ->
                val gemaId = userData.gemaId
                val zoneId = userData.zoneId
                val gemaName = userData.username ?: "Gema"

                if (gemaId != null && gemaId != _uiState.value.gemaId) {
                    _uiState.update {
                        it.copy(
                            gemaId = gemaId,
                            zoneId = zoneId ?: 0,
                            gemaName = gemaName
                        )
                    }
                    loadRecompensasFromRemote()
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

    fun onEvent(event: GemaTiendaUiEvent) {
        when (event) {
            is GemaTiendaUiEvent.LoadRecompensas -> loadRecompensas()
            is GemaTiendaUiEvent.RefreshRecompensas -> loadRecompensasFromRemote()
            is GemaTiendaUiEvent.ShowCanjearConfirmation -> showCanjearConfirmation(event.recompensa)
            is GemaTiendaUiEvent.ConfirmCanjear -> confirmCanjear()
            is GemaTiendaUiEvent.DismissCanjearConfirmation -> dismissCanjearConfirmation()
            is GemaTiendaUiEvent.ClearError -> clearError()
            is GemaTiendaUiEvent.ClearSuccessMessage -> clearSuccessMessage()
        }
    }

    private fun loadPuntosDisponibles() {
        viewModelScope.launch {
            try {
                val puntos = getPuntosGemaUseCase(
                    gemaId = _uiState.value.gemaId,
                    zoneId = _uiState.value.zoneId
                )
                _uiState.update { it.copy(puntosDisponibles = puntos) }
            } catch (e: Exception) {
            }
        }
    }

    private fun loadRecompensas() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val recompensas = getRecompensasGemaUseCase()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        recompensas = recompensas,
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

    private fun loadRecompensasFromRemote() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = getRecompensasRemoteUseCase(_uiState.value.gemaId)

                when (result) {
                    is Resource.Success -> {
                        loadPuntosDisponibles()
                        loadRecompensas()
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = "No se pudo sincronizar: ${result.message}. Mostrando datos locales."
                            )
                        }
                        loadRecompensas()
                        loadPuntosDisponibles()
                    }
                    is Resource.Loading -> {
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al sincronizar: ${e.message}. Mostrando datos locales."
                    )
                }
                loadRecompensas()
                loadPuntosDisponibles()
            }
        }
    }

    private fun showCanjearConfirmation(recompensa: RecompensaGema) {
        if (_uiState.value.puntosDisponibles < recompensa.precio) {
            _uiState.update {
                it.copy(
                    errorMessage = "No tienes suficientes puntos. Necesitas ${recompensa.precio} puntos y tienes ${_uiState.value.puntosDisponibles}."
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                recompensaToCanjear = recompensa,
                showConfirmDialog = true
            )
        }
    }

    private fun confirmCanjear() {
        val recompensa = _uiState.value.recompensaToCanjear ?: return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    processingRecompensaId = recompensa.recompensaId,
                    showConfirmDialog = false
                )
            }

            try {
                canjearRecompensaUseCase(
                    recompensaId = recompensa.recompensaId,
                    gemaId = _uiState.value.gemaId
                )

                triggerSyncUseCase()

                _uiState.update {
                    it.copy(
                        processingRecompensaId = null,
                        recompensaToCanjear = null,
                        successMessage = "¡Has canjeado \"${recompensa.titulo}\" exitosamente!",
                        puntosDisponibles = it.puntosDisponibles - recompensa.precio
                    )
                }

                loadRecompensas()

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al canjear recompensa: ${e.message}",
                        processingRecompensaId = null,
                        recompensaToCanjear = null
                    )
                }
            }
        }
    }

    private fun dismissCanjearConfirmation() {
        _uiState.update {
            it.copy(
                recompensaToCanjear = null,
                showConfirmDialog = false
            )
        }
    }

    private fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }
}