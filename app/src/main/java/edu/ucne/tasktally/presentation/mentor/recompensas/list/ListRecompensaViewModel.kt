package edu.ucne.tasktally.presentation.mentor.recompensas.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.domain.usecases.auth.GetCurrentUserUseCase
import edu.ucne.tasktally.domain.usecases.mentor.recompensa.DeleteRecompensaMentorLocalUseCase
import edu.ucne.tasktally.domain.usecases.mentor.recompensa.ObserveRecompensasByMentorIdLocalUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListRecompensaViewModel @Inject constructor(
    private val observeRecompensasByMentorIdUseCase: ObserveRecompensasByMentorIdLocalUseCase,
    private val deleteRecompensaMentorUseCase: DeleteRecompensaMentorLocalUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListRecompensaUiState())
    val state: StateFlow<ListRecompensaUiState> = _state.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            getCurrentUserUseCase().first().let { userData ->
                val username = userData.username ?: "Mentor"
                _state.update { it.copy(mentorName = username) }
                onEvent(ListRecompensaUiEvent.Load)
            }
        }
    }

    fun onEvent(event: ListRecompensaUiEvent) {
        when (event) {
            ListRecompensaUiEvent.Load -> loadRecompensas()
            ListRecompensaUiEvent.Refresh -> loadRecompensas()
            ListRecompensaUiEvent.CreateNew -> {
                _state.update { it.copy(navigateToCreate = true) }
            }
            is ListRecompensaUiEvent.Edit -> {
                _state.update { it.copy(navigateToEdit = event.recompensa) }
            }
            is ListRecompensaUiEvent.ShowDeleteConfirmation -> {
                _state.update { it.copy(recompensaToDelete = event.recompensa) }
            }
            ListRecompensaUiEvent.ConfirmDelete -> confirmDelete()
            ListRecompensaUiEvent.DismissDeleteConfirmation -> {
                _state.update { it.copy(recompensaToDelete = null) }
            }
        }
    }

    private fun loadRecompensas() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val userData = getCurrentUserUseCase().first()
                val mentorId = userData.mentorId

                if (mentorId == null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "No se encontró el ID del mentor"
                        )
                    }
                    return@launch
                }

                observeRecompensasByMentorIdUseCase(mentorId).collect { recompensasMentor ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            recompensas = recompensasMentor,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error al cargar las recompensas locales"
                    )
                }
            }
        }
    }

    private fun confirmDelete() {
        val recompensa = _state.value.recompensaToDelete ?: return

        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }

            try {
                deleteRecompensaMentorUseCase(recompensa)
                _state.update {
                    it.copy(
                        isDeleting = false,
                        recompensaToDelete = null,
                        message = "Recompensa eliminada correctamente"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        recompensaToDelete = null,
                        error = e.message ?: "Error al eliminar la recompensa"
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