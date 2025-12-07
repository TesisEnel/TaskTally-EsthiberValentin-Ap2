package edu.ucne.tasktally.presentation.zona

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.usecases.auth.GetCurrentUserUseCase
import edu.ucne.tasktally.domain.usecases.gema.JoinZoneUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ZoneAccessViewModel @Inject constructor(
    private val joinZoneUseCase: JoinZoneUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ZoneAccessUiState())
    val state: StateFlow<ZoneAccessUiState> = _state.asStateFlow()

    private var currentGemaId: Int? = null

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().collectLatest { userData ->
                currentGemaId = userData.gemaId
            }
        }
    }

    fun onEvent(event: ZoneAccessUiEvent) {
        when (event) {
            is ZoneAccessUiEvent.OnZoneCodeChange -> {
                _state.update { it.copy(zoneCode = event.zoneCode.uppercase()) }
            }

            is ZoneAccessUiEvent.JoinZone -> {
                joinZone()
            }

            is ZoneAccessUiEvent.UserMessageShown -> {
                _state.update { it.copy(userMessage = null) }
            }
        }
    }

    private fun joinZone() {
        val gemaId = currentGemaId
        if (gemaId == null) {
            _state.update { it.copy(userMessage = "Error: No se pudo identificar el usuario") }
            return
        }

        val zoneCode = _state.value.zoneCode.trim()
        if (zoneCode.isBlank()) {
            _state.update { it.copy(userMessage = "Por favor ingresa un código de zona válido") }
            return
        }

        viewModelScope.launch {
            joinZoneUseCase(gemaId, zoneCode).collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }

                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                hasZoneAccess = true,
                                userMessage = "¡Te has unido a la zona exitosamente!"
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                userMessage = resource.message ?: "Error al unirse a la zona"
                            )
                        }
                    }
                }
            }
        }
    }
}
