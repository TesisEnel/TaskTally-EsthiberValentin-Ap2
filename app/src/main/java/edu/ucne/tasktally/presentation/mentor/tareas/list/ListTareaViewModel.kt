package edu.ucne.tasktally.presentation.mentor.tareas.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.domain.usecases.mentor.DeleteTareaUseCase
import edu.ucne.tasktally.domain.usecases.mentor.ObserveTareasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListTareaViewModel @Inject constructor(
    private val observeTareasUseCase: ObserveTareasUseCase,
    private val deleteTareaUseCase: DeleteTareaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListTareaUiState(isLoading = true))
    val state: StateFlow<ListTareaUiState> = _state.asStateFlow()

    // TODO: Obtener el mentorId de la sesión actual
    private val mentorId: Int = 1

    init {
        onEvent(ListTareaUiEvent.Load)
    }

    fun onEvent(event: ListTareaUiEvent) {
        when (event) {
            ListTareaUiEvent.Load -> observe()
            is ListTareaUiEvent.Delete -> onDelete(event.id)
            ListTareaUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is ListTareaUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is ListTareaUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
        }
    }

    private fun observe() {
        viewModelScope.launch {
            observeTareasUseCase(mentorId).collectLatest { list ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        tareas = list,
                        message = null
                    )
                }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            deleteTareaUseCase(id)
            onEvent(ListTareaUiEvent.ShowMessage("Tarea eliminada"))
        }
    }

    fun onNavigationHandled() {
        _state.update {
            it.copy(
                navigateToCreate = false,
                navigateToEditId = null
            )
        }
    }

    fun onMessageShown() {
        _state.update { it.copy(message = null) }
    }
}