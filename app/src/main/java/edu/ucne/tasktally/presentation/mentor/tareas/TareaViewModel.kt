package edu.ucne.tasktally.presentation.mentor.tareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.models.Tarea
import edu.ucne.tasktally.domain.usecases.mentor.CreateTareaUseCase
import edu.ucne.tasktally.domain.usecases.mentor.GetTareaByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TareaViewModel @Inject constructor(
    private val createTareaUseCase: CreateTareaUseCase,
    private val getTareaByIdUseCase: GetTareaByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TareaUiState())
    val state: StateFlow<TareaUiState> = _state.asStateFlow()

    init {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        _state.update { it.copy(diaAsignada = currentDate) }
    }

    fun onEvent(event: TareaUiEvent) {
        when (event) {
            is TareaUiEvent.OnTituloChange -> {
                _state.update {
                    it.copy(titulo = event.value, tituloError = null)
                }
            }
            is TareaUiEvent.OnDescripcionChange -> {
                _state.update {
                    it.copy(descripcion = event.value, descripcionError = null)
                }
            }
            is TareaUiEvent.OnPuntosChange -> {
                _state.update {
                    it.copy(puntos = event.value, puntosError = null)
                }
            }
            is TareaUiEvent.OnDiaAsignadaChange -> {
                _state.update {
                    it.copy(diaAsignada = event.value, diaAsignadaError = null)
                }
            }
            is TareaUiEvent.OnEstadoChange -> {
                _state.update { it.copy(estado = event.value) }
            }
            is TareaUiEvent.OnImageSelected -> {
                _state.update {
                    it.copy(
                        imgVector = event.imageName,
                        showImagePicker = false
                    )
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

    private fun loadTarea(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val tarea = getTareaByIdUseCase(id)

            if (tarea != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isEditing = true,
                        tareaId = tarea.id,
                        titulo = tarea.titulo,
                        descripcion = tarea.descripcion,
                        puntos = tarea.puntos.toString(),
                        estado = tarea.estado,
                        diaAsignada = tarea.diaAsignada ?: "",
                        imgVector = tarea.imgVector
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "No se encontró la tarea"
                    )
                }
            }
        }
    }

    private fun onSave() {
        if (!validarCampos()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val tarea = Tarea(
                id = _state.value.tareaId ?: "",
                remoteId = null,
                estado = _state.value.estado,
                titulo = _state.value.titulo.trim(),
                descripcion = _state.value.descripcion.trim(),
                puntos = _state.value.puntos.toDoubleOrNull() ?: 0.0,
                diaAsignada = _state.value.diaAsignada,
                imgVector = _state.value.imgVector,
                isPendingPost = !_state.value.isEditing,
                isPendingUpdate = _state.value.isEditing
            )

            when (val result = createTareaUseCase(tarea)) {
                is Resource.Success -> {
                    val mensaje = if (_state.value.isEditing) {
                        "Tarea actualizada exitosamente"
                    } else {
                        "Tarea creada exitosamente"
                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = mensaje,
                            navigateBack = true
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Error desconocido"
                        )
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
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

        if (_state.value.descripcion.isBlank()) {
            _state.update { it.copy(descripcionError = "La descripción es requerida") }
            isValid = false
        }

        val puntos = _state.value.puntos.toDoubleOrNull()
        if (puntos == null || puntos <= 0) {
            _state.update { it.copy(puntosError = "Los puntos deben ser un número válido mayor a 0") }
            isValid = false
        }

        if (_state.value.diaAsignada.isBlank()) {
            _state.update { it.copy(diaAsignadaError = "El día asignado es requerido") }
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
}