package edu.ucne.tasktally.presentation.mentor.tareas.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.usecases.auth.GetCurrentUserUseCase
import edu.ucne.tasktally.domain.usecases.mentor.GetTareasRecompensasMentorRemoteUseCase
import edu.ucne.tasktally.domain.usecases.mentor.tarea.DeleteTareaMentorUseCase
import edu.ucne.tasktally.domain.usecases.mentor.tarea.ObserveTareasByMentorIdLocalUseCase
import edu.ucne.tasktally.domain.usecases.sync.TriggerSyncUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListTareaViewModel @Inject constructor(
    private val observeAllUseCaseRemote: GetTareasRecompensasMentorRemoteUseCase,
    private val observeTareasByMentorIdLocalUseCase: ObserveTareasByMentorIdLocalUseCase,
    private val deleteTareaMentorUseCase: DeleteTareaMentorUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val triggerSyncUseCase: TriggerSyncUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListTareaUiState())
    val state: StateFlow<ListTareaUiState> = _state.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val userData = withContext(Dispatchers.IO) {
                getCurrentUserUseCase().first()
            }
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
                val userData = withContext(Dispatchers.IO) {
                    getCurrentUserUseCase().first()
                }
                val mentorId = userData.mentorId

                if (mentorId == null) {
                    _state.update { it.copy(isLoading = false, error = "No se encontró ID mentor") }
                    return@launch
                }

                val syncResult = withContext(Dispatchers.IO) {
                    observeAllUseCaseRemote(mentorId = mentorId)
                }

                withContext(Dispatchers.IO) {
                    observeTareasByMentorIdLocalUseCase(mentorId).collect { tareasMentor ->
                        val (message, error) = when (syncResult) {
                            is Resource.Success -> {
                                Pair("Datos sincronizados correctamente", null)
                            }
                            is Resource.Error -> {
                                Pair(null, "Error de sincronización: ${syncResult.message}. Mostrando datos locales.")
                            }
                            else -> Pair(null, null)
                        }

                        withContext(Dispatchers.Main) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    tareas = tareasMentor,
                                    message = message,
                                    error = error
                                )
                            }
                        }
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
                withContext(Dispatchers.IO) {
                    deleteTareaMentorUseCase(tarea)
                    triggerSyncUseCase()
                }
                _state.update {
                    it.copy(
                        isDeleting = false,
                        tareaToDelete = null,
                        message = "Tarea eliminada y sincronizada correctamente"
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