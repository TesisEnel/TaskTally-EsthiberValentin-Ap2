package edu.ucne.tasktally.presentation.mentor.tareas.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.domain.usecases.auth.GetCurrentUserUseCase
import edu.ucne.tasktally.domain.usecases.mentor.tarea.DeleteTareaMentorUseCase
import edu.ucne.tasktally.domain.usecases.mentor.tarea.ObserveTareasByMentorIdLocalUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListTareaViewModel @Inject constructor(
    private val observeTareasByMentorIdLocalUseCase: ObserveTareasByMentorIdLocalUseCase,
    private val deleteTareaMentorUseCase: DeleteTareaMentorUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListTareaUiState())
    val state: StateFlow<ListTareaUiState> = _state.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val userData = getCurrentUserUseCase().first()
            val username = userData.username ?: "Mentor"
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
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val userData = getCurrentUserUseCase().first()
                val mentorId = userData.mentorId

                if (mentorId == null) {
                    _state.update { it.copy(isLoading = false, error = "No se encontró ID mentor") }
                    return@launch
                }

                observeTareasByMentorIdLocalUseCase(mentorId).collect { tareasMentor ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            tareas = tareasMentor,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error al cargar las tareas locales"
                    )
                }
            }
        }
    }

    private fun confirmDelete() {
        val tarea = _state.value.tareaToDelete ?: return

        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }

            try {
                deleteTareaMentorUseCase(tarea)
                _state.update {
                    it.copy(
                        isDeleting = false,
                        tareaToDelete = null,
                        message = "Tarea eliminada correctamente"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        tareaToDelete = null,
                        error = e.message ?: "Error al eliminar la tarea"
                    )
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