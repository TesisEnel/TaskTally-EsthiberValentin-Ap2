package edu.ucne.tasktally.presentation.mentor.recompensas.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.domain.usecases.mentor.DeleteRecompensaUseCase
import edu.ucne.tasktally.domain.usecases.mentor.ObserveRecompensaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListaRecompensaViewModel @Inject constructor(
    private val observeRecompensaUseCase: ObserveRecompensaUseCase,
    private val deleteRecompensaUseCase: DeleteRecompensaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListaRecompensaUiState(isLoading = true))
    val state: StateFlow<ListaRecompensaUiState> = _state.asStateFlow()

    init {
        onEvent(ListaRecompensaUiEvent.Load)
    }

    fun onEvent(event: ListaRecompensaUiEvent) {
        when (event) {
            ListaRecompensaUiEvent.Load -> observe()
            is ListaRecompensaUiEvent.Delete -> onDelete(event.id)
            ListaRecompensaUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is ListaRecompensaUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is ListaRecompensaUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
        }
    }

    private fun observe() {
        viewModelScope.launch {
            observeRecompensaUseCase().collectLatest { list ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        recompensas = list,
                        message = null
                    )
                }
            }
        }
    }

    private fun onDelete(id: String) {
        viewModelScope.launch {
            deleteRecompensaUseCase(id)
            onEvent(ListaRecompensaUiEvent.ShowMessage("Recompensa eliminada"))
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