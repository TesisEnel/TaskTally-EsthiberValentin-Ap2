package edu.ucne.tasktally.presentation.mentor.recompensas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.models.Recompensa
import edu.ucne.tasktally.domain.usecases.mentor.CreateRecompensaUseCase
import edu.ucne.tasktally.domain.usecases.mentor.GetRecompensaByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecompensaViewModel @Inject constructor(
    private val createRecompensaUseCase: CreateRecompensaUseCase,
    private val getRecompensaByIdUseCase: GetRecompensaByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RecompensaUiState())
    val state: StateFlow<RecompensaUiState> = _state.asStateFlow()

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
            is RecompensaUiEvent.LoadRecompensa -> loadRecompensa(event.id)
        }
    }

    private fun loadRecompensa(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val recompensa = getRecompensaByIdUseCase(id)

            if (recompensa != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isEditing = true,
                        recompensaId = recompensa.id,
                        titulo = recompensa.titulo,
                        descripcion = recompensa.descripcion,
                        precio = recompensa.precio.toString(),
                        imgVector = recompensa.imgVector
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "No se encontró la recompensa"
                    )
                }
            }
        }
    }

    private fun onSave() {
        if (!validarCampos()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val recompensa = Recompensa(
                id = _state.value.recompensaId ?: "",
                remoteId = null,
                titulo = _state.value.titulo.trim(),
                descripcion = _state.value.descripcion.trim(),
                precio = _state.value.precio.toDoubleOrNull() ?: 0.0,
                imgVector = _state.value.imgVector,
                isPendingPost = !_state.value.isEditing,
                isPendingUpdate = _state.value.isEditing
            )

            when (val result = createRecompensaUseCase(recompensa)) {
                is Resource.Success -> {
                    val mensaje = if (_state.value.isEditing) {
                        "Recompensa actualizada exitosamente"
                    } else {
                        "Recompensa creada exitosamente"
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