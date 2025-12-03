package edu.ucne.tasktally.presentation.mentor.tareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.models.Tarea
import edu.ucne.tasktally.domain.usecases.mentor.CreateTareaUseCase
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
    private val createTareaUseCase: CreateTareaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TareaUiState())
    val state: StateFlow<TareaUiState> = _state.asStateFlow()

    init {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        _state.update { it.copy(fechaCreacion = currentDate) }
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
            is TareaUiEvent.OnFechaCreacionChange -> {
                _state.update {
                    it.copy(fechaCreacion = event.value, fechaCreacionError = null)
                }
            }
            is TareaUiEvent.OnZonaIdChange -> {
                _state.update { it.copy(zonaId = event.value) }
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
        }
    }

    private fun onSave() {
        if (!validarCampos()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val tarea = Tarea(
                tareaId = 0,
                createdBy = 1,
                zonaId = _state.value.zonaId ?: 1,
                estadoId = 1,
                titulo = _state.value.titulo.trim(),
                descripcion = _state.value.descripcion.trim(),
                puntos = _state.value.puntos.toDoubleOrNull() ?: 0.0,
                diaAsignada = null,
                recurrente = null,
                imgVector = _state.value.imgVector,
                fechaCreacion = _state.value.fechaCreacion
            )

            when (val result = createTareaUseCase(tarea)) {
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
                            error = result.message ?: "Error desconocido al crear la tarea"
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

        if (_state.value.fechaCreacion.isBlank()) {
            _state.update { it.copy(fechaCreacionError = "La fecha de creación es requerida") }
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