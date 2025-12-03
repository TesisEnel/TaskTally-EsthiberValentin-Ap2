package edu.ucne.tasktally.presentation.mentor.recompensas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.models.Recompensa
import edu.ucne.tasktally.domain.usecases.mentor.CreateRecompensaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RecompensaViewModel @Inject constructor(
    private val createRecompensaUseCase: CreateRecompensaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecompensaUiState())
    val state: StateFlow<RecompensaUiState> = _state.asStateFlow()

    init {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        _state.update { it.copy(fechaCreacion = currentDate) }
    }

    fun onEvent(event: RecompensaUiEvent) {
        when (event) {
            is RecompensaUiEvent.OnTituloChange -> {
                _state.update {
                    it.copy(titulo = event.value, tituloError = null)
                }
            }
            is RecompensaUiEvent.OnDescripcionChange -> {
                _state.update {
                    it.copy(descripcion = event.value, descripcionError = null)
                }
            }
            is RecompensaUiEvent.OnPrecioChange -> {
                _state.update {
                    it.copy(precio = event.value, precioError = null)
                }
            }
            is RecompensaUiEvent.OnIsDisponibleChange -> {
                _state.update { it.copy(isDisponible = event.value) }
            }
            // ← NUEVOS EVENTOS PARA IMAGEN
            is RecompensaUiEvent.OnImageSelected -> {
                _state.update {
                    it.copy(
                        imgVector = event.imageName,
                        showImagePicker = false
                    )
                }
            }
            RecompensaUiEvent.OnShowImagePicker -> {
                _state.update { it.copy(showImagePicker = true) }
            }
            RecompensaUiEvent.OnDismissImagePicker -> {
                _state.update { it.copy(showImagePicker = false) }
            }
            RecompensaUiEvent.Save -> onSave()
        }
    }

    private fun onSave() {
        if (!validarCampos()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val recompensa = Recompensa(
                recompensaId = 0,
                createdBy = 1, // TODO: Obtener el mentorId de la sesión actual
                titulo = _state.value.titulo.trim(),
                descripcion = _state.value.descripcion.trim(),
                precio = _state.value.precio.toDoubleOrNull() ?: 0.0,
                isDisponible = _state.value.isDisponible,
                fechaCreacion = _state.value.fechaCreacion
                // TODO: Agregar imgVector al modelo Recompensa si lo necesitas
            )

            when (val result = createRecompensaUseCase(recompensa)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            message = "Recompensa creada exitosamente",
                            navigateBack = true
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Error desconocido al crear la recompensa"
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

        val precio = _state.value.precio.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            _state.update { it.copy(precioError = "El precio debe ser un número válido mayor a 0") }
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