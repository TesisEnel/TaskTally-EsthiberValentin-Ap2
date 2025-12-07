package edu.ucne.tasktally.presentation.zona

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.mappers.toGemaDomain
import edu.ucne.tasktally.data.mappers.toZonaDomain
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.usecases.GetZonaByMentorUseCase
import edu.ucne.tasktally.domain.usecases.UpsertZonaUseCase
import edu.ucne.tasktally.domain.usecases.GenerateNewJoinCodeUseCase
import edu.ucne.tasktally.domain.usecases.gema.zona.GetZoneInfoGemaUseCase
import edu.ucne.tasktally.domain.usecases.mentor.zona.GetZoneInfoMentorUseCase
import edu.ucne.tasktally.domain.usecases.mentor.zona.UpdateZoneCodeUseCase
import edu.ucne.tasktally.domain.usecases.mentor.zona.UpdateZoneNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ZonaViewModel @Inject constructor(
    private val getZonaByMentorUseCase: GetZonaByMentorUseCase,
    private val upsertZonaUseCase: UpsertZonaUseCase,
    private val generateNewJoinCodeUseCase: GenerateNewJoinCodeUseCase,
    private val getZoneInfoMentorUseCase: GetZoneInfoMentorUseCase,
    private val getZoneInfoGemaUseCase: GetZoneInfoGemaUseCase,
    private val updateZoneNameUseCase: UpdateZoneNameUseCase,
    private val updateZoneCodeUseCase: UpdateZoneCodeUseCase
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

                when (val result = getZoneInfoMentorUseCase(mentorId)) {
                    is Resource.Success -> {
                        val zoneInfo = result.data
                        if (zoneInfo != null) {
                            val zona = zoneInfo.toZonaDomain().copy(mentorId = userId)
                            val gemas = zoneInfo.gemas.map { it.toGemaDomain() }

                            _state.update {
                                it.copy(
                                    zona = zona,
                                    gemas = gemas,
                                    isLoading = false
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    userMessage = "No se pudo obtener información de la zona"
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                userMessage = result.message
                                    ?: "Error al cargar información de la zona"
                            )
                        }
                    }
                    is Resource.Loading -> {}
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

                when (val result = getZoneInfoGemaUseCase(gemaId)) {
                    is Resource.Success -> {
                        val zoneInfoList = result.data
                        if (!zoneInfoList.isNullOrEmpty()) {
                            val zoneInfo = zoneInfoList.first()
                            val zona = zoneInfo.toZonaDomain()
                            val gemas = zoneInfo.gemas.map { it.toGemaDomain() }

                            _state.update {
                                it.copy(
                                    zona = zona,
                                    gemas = gemas,
                                    isLoading = false
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    userMessage = "No estás en ninguna zona de estudio"
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                userMessage = result.message
                                    ?: "Error al cargar información de las zonas"
                            )
                        }
                    }

                    is Resource.Loading -> {}
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
        val mentorId = currentState.currentUserId?.toIntOrNull()

        if (zona == null || newName.isEmpty() || mentorId == null) {
            _state.update {
                it.copy(
                    userMessage = "Nombre de zona no válido"
                )
            }
            return@launch
        }

        updateZoneNameUseCase(mentorId, newName).collectLatest { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }

                is Resource.Success -> {
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
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = resource.message ?: "Error al actualizar el nombre"
                        )
                    }
                }
            }
        }
    }

    private fun generateNewJoinCode() = viewModelScope.launch {
        val currentState = _state.value
        val zona = currentState.zona
        val mentorId = currentState.currentUserId?.toIntOrNull()

        if (zona == null || mentorId == null) {
            _state.update {
                it.copy(userMessage = "No hay zona disponible")
            }
            return@launch
        }

        updateZoneCodeUseCase(mentorId).collectLatest { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }

                is Resource.Success -> {
                    loadZona(currentState.currentUserId!!, currentState.isMentor)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "Código de acceso actualizado"
                        )
                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = resource.message ?: "Error al generar nuevo código"
                        )
                    }
                }
            }
        }
    }
}
