package edu.ucne.tasktally.presentation.zona

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.domain.usecases.mentor.zona.UpdateZoneCodeRemoteUseCase
import edu.ucne.tasktally.domain.usecases.zona.UpdateZoneNameLocalUseCase
import edu.ucne.tasktally.domain.usecases.auth.GetCurrentUserUseCase
import edu.ucne.tasktally.domain.usecases.gema.zona.GetGemaZonaByIdUseCase
import edu.ucne.tasktally.domain.usecases.mentor.zona.GetMentorZonaByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ZonaViewModel @Inject constructor(
    private val getMentorZonaByIdUseCase: GetMentorZonaByIdUseCase,
    private val getGemaZonaByIdUseCase: GetGemaZonaByIdUseCase,
    private val updateZoneNameUseCase: UpdateZoneNameLocalUseCase,
    private val updateZoneCodeUseCase: UpdateZoneCodeRemoteUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ZonaUiState())
    val state: StateFlow<ZonaUiState> = _state.asStateFlow()

    fun onEvent(event: ZonaUiEvent) {
        when (event) {
            is ZonaUiEvent.LoadZona -> {
                loadZona(event.userId, event.isMentor)
            }

            is ZonaUiEvent.OnZonaNameChange -> {
                _state.update { it.copy(zonaName = event.name) }
            }

            is ZonaUiEvent.StartEditingName -> {
                _state.update {
                    it.copy(
                        isEditingName = true,
                        zonaName = it.zona?.nombre ?: ""
                    )
                }
            }

            is ZonaUiEvent.SaveZonaName -> {
                saveZonaName()
            }

            is ZonaUiEvent.CancelEditingName -> {
                _state.update {
                    it.copy(
                        isEditingName = false,
                        zonaName = ""
                    )
                }
            }

            is ZonaUiEvent.GenerateNewJoinCode -> {
                generateNewJoinCode()
            }

            is ZonaUiEvent.ShowJoinCodeDialog -> {
                _state.update { it.copy(showJoinCodeDialog = true) }
            }

            is ZonaUiEvent.HideJoinCodeDialog -> {
                _state.update { it.copy(showJoinCodeDialog = false) }
            }

            is ZonaUiEvent.UserMessageShown -> {
                _state.update { it.copy(userMessage = null) }
            }

            is ZonaUiEvent.Refresh -> {
                val currentState = _state.value
                if (currentState.currentUserId != null) {
                    loadZona(currentState.currentUserId, currentState.isMentor)
                }
            }
        }
    }

    private fun loadZona(userId: String, isMentor: Boolean) = viewModelScope.launch {
        _state.update {
            it.copy(
                isLoading = true,
                currentUserId = userId,
                isMentor = isMentor
            )
        }

        try {
            if (isMentor) {
                val mentorId = userId.toIntOrNull()
                if (mentorId == null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "ID de mentor inválido"
                        )
                    }
                    return@launch
                }
                val userData = getCurrentUserUseCase().first()
                val zoneId = userData.zoneId

                if (zoneId == null || zoneId <= 0) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "No tienes una zona asignada"
                        )
                    }
                    return@launch
                }

                val zona = getMentorZonaByIdUseCase(zoneId)
                _state.update {
                    it.copy(
                        zona = zona,
                        gemas = zona?.gemas ?: emptyList(),
                        isLoading = false
                    )
                }
            } else {
                val gemaId = userId.toIntOrNull()
                if (gemaId == null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "ID de gema inválido"
                        )
                    }
                    return@launch
                }

                val zona = getGemaZonaByIdUseCase(gemaId)
                _state.update {
                    it.copy(
                        zona = zona,
                        gemas = zona?.gemas ?: emptyList(),
                        isLoading = false
                    )
                }
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    userMessage = "Error al cargar la zona: ${e.message}"
                )
            }
        }
    }

    private fun saveZonaName() = viewModelScope.launch {
        val currentState = _state.value
        val zona = currentState.zona
        val newName = currentState.zonaName.trim()
        val zoneId = zona?.zonaId

        if (zona == null || newName.isEmpty() || zoneId == null) {
            _state.update {
                it.copy(
                    userMessage = "Nombre de zona no válido"
                )
            }
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        try {
            updateZoneNameUseCase.invoke(zoneId, newName)
            val updatedZona = zona.copy(nombre = newName)
            _state.update {
                it.copy(
                    zona = updatedZona,
                    isEditingName = false,
                    zonaName = "",
                    isLoading = false,
                    userMessage = "Nombre de zona actualizado"
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    userMessage = "Error al actualizar el nombre: ${e.message}"
                )
            }
        }
    }

    private fun generateNewJoinCode() = viewModelScope.launch {
        val currentState = _state.value
        val zona = currentState.zona
        val mentorId = currentState.currentUserId?.toIntOrNull()

        if (zona == null || mentorId == null || zona.zonaId == 0) {
            _state.update {
                it.copy(userMessage = "No hay zona disponible")
            }
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        try {
            updateZoneCodeUseCase(zona.zonaId, mentorId)
            loadZona(currentState.currentUserId, currentState.isMentor)
            _state.update {
                it.copy(
                    isLoading = false,
                    userMessage = "Código de acceso actualizado"
                )
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    userMessage = "Error al generar nuevo código: ${e.message}"
                )
            }
        }
    }
}
