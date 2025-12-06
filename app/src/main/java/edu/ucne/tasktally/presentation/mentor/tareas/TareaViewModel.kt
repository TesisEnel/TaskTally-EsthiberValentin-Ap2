package edu.ucne.tasktally.presentation.mentor.tareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.local.preferences.AuthPreferencesManager
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.data.remote.DTOs.mentor.tareas.CreateTareaRequest
import edu.ucne.tasktally.data.remote.DTOs.mentor.tareas.UpdateTareaRequest
import edu.ucne.tasktally.domain.usecases.mentor.tarea.CreateTareaUseCase
import edu.ucne.tasktally.domain.usecases.mentor.tarea.UpdateTareaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TareaViewModel @Inject constructor(
    private val createTareaUseCase: CreateTareaUseCase,
    private val updateTareaUseCase: UpdateTareaUseCase,
    private val authPreferencesManager: AuthPreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(TareaUiState())
    val state: StateFlow<TareaUiState> = _state.asStateFlow()

    fun onEvent(event: TareaUiEvent) {
        when (event) {
            is TareaUiEvent.OnTituloChange -> {
                _state.update { it.copy(titulo = event.value, tituloError = null) }
            }
            is TareaUiEvent.OnDescripcionChange -> {
                _state.update { it.copy(descripcion = event.value, descripcionError = null) }
            }
            is TareaUiEvent.OnPuntosChange -> {
                _state.update { it.copy(puntos = event.value, puntosError = null) }
            }
            is TareaUiEvent.OnRecurrenteChange -> {
                _state.update { it.copy(recurrente = event.value) }
            }
            is TareaUiEvent.OnDiasChange -> {
                _state.update { it.copy(diasSeleccionados = event.dias) }
            }
            is TareaUiEvent.OnAsignadaChange -> {
                _state.update { it.copy(asignada = event.gemaId) }
            }
            is TareaUiEvent.OnImageSelected -> {
                _state.update {
                    it.copy(imgVector = event.imageName, showImagePicker = false)
                }
            }
            TareaUiEvent.OnShowImagePicker -> {
                _state.update { it.copy(showImagePicker = true) }
            }
            TareaUiEvent.OnDismissImagePicker -> {
                _state.update { it.copy(showImagePicker = false) }
            }
            TareaUiEvent.Save -> onSave()
            is TareaUiEvent.LoadTarea -> loadTarea(event.id)
        }
    }

    private fun loadTarea(id: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isEditing = true,
                    tareaId = id
                )
            }

            // TODO: implementar GetTareaByIdUseCase

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun onSave() {
        if (!validarCampos()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val mentorId = authPreferencesManager.mentorId.first()

            if (mentorId == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "No se encontró el ID del mentor"
                    )
                }
                return@launch
            }

            val currentState = _state.value

            if (currentState.isEditing && currentState.tareaId != null) {
                updateTarea(mentorId, currentState)
            } else {
                createTarea(mentorId, currentState)
            }
        }
    }

    private suspend fun createTarea(mentorId: Int, currentState: TareaUiState) {
        val request = CreateTareaRequest(
            titulo = currentState.titulo.trim(),
            descripcion = currentState.descripcion.trim().ifBlank { null },
            puntos = currentState.puntos.toIntOrNull() ?: 0,
            recurrente = currentState.recurrente,
            dias = currentState.diasSeleccionados.joinToString(",").ifBlank { null },
            nombreImgVector = currentState.imgVector,
            asignada = currentState.asignada
        )

        createTareaUseCase(mentorId, request).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = "Tarea creada exitosamente",
                            navigateBack = true
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Error al crear la tarea"
                        )
                    }
                }
            }
        }
    }

    private suspend fun updateTarea(mentorId: Int, currentState: TareaUiState) {
        val tareaId = currentState.tareaId ?: return

        val request = UpdateTareaRequest(
            tareaId = tareaId,
            titulo = currentState.titulo.trim(),
            descripcion = currentState.descripcion.trim().ifBlank { null },
            puntos = currentState.puntos.toIntOrNull() ?: 0,
            recurrente = currentState.recurrente,
            dias = currentState.diasSeleccionados.joinToString(",").ifBlank { null },
            nombreImgVector = currentState.imgVector,
            asignada = currentState.asignada
        )

        updateTareaUseCase(mentorId, tareaId, request).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = "Tarea actualizada exitosamente",
                            navigateBack = true
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Error al actualizar la tarea"
                        )
                    }
                }
            }
        }
    }

    private fun validarCampos(): Boolean {
        var isValid = true

        if (_state.value.titulo.isBlank()) {
            _state.update { it.copy(tituloError = "El título es requerido") }
            isValid = false
        }

        val puntos = _state.value.puntos.toIntOrNull()
        if (puntos == null || puntos <= 0) {
            _state.update { it.copy(puntosError = "Los puntos deben ser un número válido mayor a 0") }
            isValid = false
        }

        return isValid
    }

    fun onNavigationHandled() {
        _state.update { it.copy(navigateBack = false) }
    }

    fun onMessageShown() {
        _state.update { it.copy(message = null, error = null) }
    }

    fun setTareaParaEditar(
        tareaId: Int,
        titulo: String,
        descripcion: String?,
        puntos: Int,
        recurrente: Boolean,
        dias: String?,
        imgVector: String?,
        asignada: String
    ) {
        _state.update {
            it.copy(
                isEditing = true,
                tareaId = tareaId,
                titulo = titulo,
                descripcion = descripcion ?: "",
                puntos = puntos.toString(),
                recurrente = recurrente,
                diasSeleccionados = dias?.split(",")?.filter { d -> d.isNotBlank() } ?: emptyList(),
                imgVector = imgVector,
                asignada = asignada
            )
        }
    }
}