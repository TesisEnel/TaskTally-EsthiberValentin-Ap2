package edu.ucne.tasktally.presentation.mentor.tareas.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.local.preferences.AuthPreferencesManager

import edu.ucne.tasktally.domain.usecases.mentor.tarea.GetTareasMentorLocalUseCase
import edu.ucne.tasktally.data.mappers.toTareaDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListTareaViewModel @Inject constructor(
    private val getTareasMentorLocalUseCase: GetTareasMentorLocalUseCase,
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
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                getTareasMentorLocalUseCase().collect { tareasMentor ->
                    val tareasDto = tareasMentor.map { it.toTareaDto() }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            tareas = tareasDto,
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
        // TODO: Implementar eliminacion local
        _state.update {
            it.copy(
                isDeleting = false,
                tareaToDelete = null,
                error = "Funcion de eliminacion no implementada todavias"
            )
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