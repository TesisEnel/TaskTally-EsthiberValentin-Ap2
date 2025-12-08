package edu.ucne.tasktally.presentation.mentor.tareas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.domain.models.TareaMentor
import edu.ucne.tasktally.domain.usecases.auth.GetCurrentUserUseCase
import edu.ucne.tasktally.domain.usecases.mentor.tarea.CreateTareaMentorLocalUseCase
import edu.ucne.tasktally.domain.usecases.mentor.tarea.GetTareaMentorByIdLocalUseCase
import edu.ucne.tasktally.domain.usecases.mentor.tarea.UpdateTareaMentorLocalUseCase
import edu.ucne.tasktally.domain.usecases.sync.TriggerSyncUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TareaViewModel @Inject constructor(
    private val createTareaMentorLocalUseCase: CreateTareaMentorLocalUseCase,
    private val getTareaByIdUseCase: GetTareaMentorByIdLocalUseCase,
    private val updateTareaMentorUseCase: UpdateTareaMentorLocalUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val triggerSyncUseCase: TriggerSyncUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TareaUiState())
    val state: StateFlow<TareaUiState> = _state.asStateFlow()

    init {
        loadMentorName()
    }

    private fun loadMentorName() {
        viewModelScope.launch {
            val userData = getCurrentUserUseCase().first()
            val username = userData.username ?: "Mentor"
            _state.update { it.copy(mentorName = username) }
        }
    }

    fun onEvent(event: TareaUiEvent) {
        when (event) {
            is TareaUiEvent.OnTituloChange ->
                _state.update { it.copy(titulo = event.value, tituloError = null) }

            is TareaUiEvent.OnDescripcionChange ->
                _state.update { it.copy(descripcion = event.value) }

            is TareaUiEvent.OnPuntosChange ->
                _state.update { it.copy(puntos = event.value, puntosError = null) }

            TareaUiEvent.OnShowImagePicker ->
                _state.update { it.copy(showImagePicker = true) }

            TareaUiEvent.OnDismissImagePicker ->
                _state.update { it.copy(showImagePicker = false) }

            is TareaUiEvent.OnImageSelected ->
                _state.update { it.copy(imgVector = event.imageName, showImagePicker = false) }

            TareaUiEvent.Save -> onSave()
        }
    }

    fun loadTareaParaEdicion(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isEditing = true) }

            try {
                val tarea = getTareaByIdUseCase(id)

                if (tarea != null) {
                    _state.update {
                        it.copy(
                            tareaId = tarea.tareaId,
                            titulo = tarea.titulo,
                            descripcion = tarea.descripcion,
                            puntos = tarea.puntos.toString(),
                            imgVector = tarea.nombreImgVector,
                            isLoading = false
                        )
                    }
                } else {
                    _state.update {
                        it.copy(error = "No se encontró la tarea", isLoading = false)
                    }
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Error al cargar la tarea", isLoading = false)
                }
            }
        }
    }

    private fun onSave() {
        if (!validarCampos()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val userData = getCurrentUserUseCase().first()
            val mentorId = userData.mentorId

            if (mentorId == null) {
                _state.update { it.copy(isLoading = false, error = "No se encontró ID mentor") }
                return@launch
            }

            val st = _state.value

            if (st.isEditing && st.tareaId != null) {
                updateTarea(mentorId, st)
                triggerSyncUseCase()
            } else {
                createTarea(mentorId, st)
                triggerSyncUseCase()
            }
        }
    }

    private suspend fun createTarea(mentorId: Int, st: TareaUiState) {
        try {
            val tarea = TareaMentor(
                titulo = st.titulo.trim(),
                descripcion = st.descripcion.trim(),
                puntos = st.puntos.toInt(),
                nombreImgVector = st.imgVector,
                mentorId = mentorId,
                isPendingCreate = true
            )

            createTareaMentorLocalUseCase(tarea)

            _state.update {
                it.copy(
                    isLoading = false,
                    message = "Guardado localmente",
                    navigateBack = true
                )
            }

        } catch (e: Exception) {
            _state.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    private suspend fun updateTarea(mentorId: Int, st: TareaUiState) {
        try {
            val tarea = TareaMentor(
                tareaId = st.tareaId!!,
                titulo = st.titulo.trim(),
                descripcion = st.descripcion.trim(),
                puntos = st.puntos.toInt(),
                nombreImgVector = st.imgVector,
                mentorId = mentorId,
                isPendingUpdate = true
            )

            updateTareaMentorUseCase(tarea)

            _state.update {
                it.copy(
                    isLoading = false,
                    message = "Tarea actualizada",
                    navigateBack = true
                )
            }

        } catch (e: Exception) {
            _state.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    private fun validarCampos(): Boolean {
        var ok = true
        if (_state.value.titulo.isBlank()) {
            _state.update { it.copy(tituloError = "El título es requerido") }
            ok = false
        }
        if (_state.value.puntos.toIntOrNull() == null || _state.value.puntos.toInt() <= 0) {
            _state.update { it.copy(puntosError = "Puntos inválidos") }
            ok = false
        }
        return ok
    }

    fun onNavigationHandled() {
        _state.update { it.copy(navigateBack = false) }
    }

    fun onMessageShown() {
        _state.update { it.copy(message = null, error = null) }
    }
}