package edu.ucne.tasktally.presentation.mentor.tareas.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.local.preferences.AuthPreferencesManager
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.usecases.mentor.GetTareasRecompensasMentorUseCase
import edu.ucne.tasktally.domain.usecases.mentor.tarea.DeleteTareaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListTareaViewModel @Inject constructor(
    private val getTareasRecompensasMentorUseCase: GetTareasRecompensasMentorUseCase,
    private val deleteTareaUseCase: DeleteTareaUseCase,
    private val authPreferencesManager: AuthPreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(ListTareaUiState())
    val state: StateFlow<ListTareaUiState> = _state.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val username = authPreferencesManager.username.first() ?: "Mentor"
            _state.update { it.copy(mentorName = username) }
            onEvent(ListTareaUiEvent.Load)
        }
    }

    fun onEvent(event: ListTareaUiEvent) {
        when (event) {
            ListTareaUiEvent.Load -> loadTareas()
            ListTareaUiEvent.Refresh -> loadTareas()
            ListTareaUiEvent.CreateNew -> {
                _state.update { it.copy(navigateToCreate = true) }
            }
            is ListTareaUiEvent.Edit -> {
                _state.update { it.copy(navigateToEdit = event.tarea) }
            }
            is ListTareaUiEvent.ShowDeleteConfirmation -> {
                _state.update { it.copy(tareaToDelete = event.tarea) }
            }
            ListTareaUiEvent.ConfirmDelete -> confirmDelete()
            ListTareaUiEvent.DismissDeleteConfirmation -> {
                _state.update { it.copy(tareaToDelete = null) }
            }
        }
    }

    private fun loadTareas() {
        viewModelScope.launch {
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

            getTareasRecompensasMentorUseCase(mentorId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        val allTareas = result.data?.flatMap { it.tareas } ?: emptyList()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                tareas = allTareas,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Error al cargar las tareas"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun confirmDelete() {
        val tareaToDelete = _state.value.tareaToDelete ?: return

        viewModelScope.launch {
            val mentorId = authPreferencesManager.mentorId.first()

            if (mentorId == null) {
                _state.update {
                    it.copy(
                        tareaToDelete = null,
                        error = "No se encontró el ID del mentor"
                    )
                }
                return@launch
            }

            _state.update { it.copy(isDeleting = true) }

            deleteTareaUseCase(mentorId, tareaToDelete.tareasGroupId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isDeleting = true) }
                    }
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isDeleting = false,
                                tareaToDelete = null,
                                message = "Tarea eliminada exitosamente"
                            )
                        }
                        loadTareas()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isDeleting = false,
                                tareaToDelete = null,
                                error = result.message ?: "Error al eliminar la tarea"
                            )
                        }
                    }
                }
            }
        }
    }

    fun onNavigationHandled() {
        _state.update {
            it.copy(
                navigateToCreate = false,
                navigateToEdit = null
            )
        }
    }

    fun onMessageShown() {
        _state.update { it.copy(message = null, error = null) }
    }
}